package serveur.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import caches.Compte;
import caches.Message;
import caches.Publication;

public class ConnexionMySQL {

    private Connection connection;

    public ConnexionMySQL() {
        String nomBD = InformationsBD.URLBD.getValeur();
        String nomBase = InformationsBD.NOMBASE.getValeur();
        String user = InformationsBD.NOMUTILISATEUR.getValeur();
        String password = InformationsBD.MOTDEPASSE.getValeur();
        String url = "jdbc:mysql://" + nomBD + ":3306/" + nomBase;

        try {

            this.connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connexion réussie.");

        } catch (SQLException e) {

            e.printStackTrace();

        }
    }

    public Connection getConnection() {
        return this.connection;
    }

    public Compte getCompte(String monPseudo, String pseudo) {

        try (PreparedStatement ps = this.connection.prepareStatement("SELECT * FROM Compte WHERE pseudo = ?")) {

            ps.setString(1, pseudo);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                String pseud = rs.getString("pseudo");
                byte[] photo = rs.getBytes("photo_profil");

                return new Compte(pseud, photo, this.getNbPublications(pseud), this.getNbFollowers(pseud),
                        this.getNbFollowings(pseud),
                        this.estCeQueJeSuisAbonne(monPseudo, pseud));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

    }

    public List<Publication> getPublicationsCanDisplay(String monPseudo) {

        List<Publication> publications = new ArrayList<>();

        try (PreparedStatement ps = this.connection.prepareStatement(
                "SELECT * FROM Publication WHERE pseudo = ? OR pseudo IN (SELECT pseudo_follow FROM Follow WHERE pseudo = ?) ORDER BY date_publication DESC;;")) {

            ps.setString(1, monPseudo);
            ps.setString(2, monPseudo);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                int idPublication = rs.getInt("id_publication");
                String pseudo = rs.getString("pseudo");
                String contenu = rs.getString("contenu");
                Timestamp datePublication = rs.getTimestamp("date_publication");
                byte[] image = rs.getBytes("image");

                publications.add(new Publication(idPublication, this.getCompte(monPseudo, pseudo),
                        contenu, datePublication, image,
                        this.estCeQueJaiLike(monPseudo, idPublication), this.getNbLike(idPublication)));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return publications;

    }

    public List<Compte> getFollowings(String monPseudo) {

        List<Compte> comptes = new ArrayList<>();

        try (PreparedStatement ps = this.connection.prepareStatement("SELECT * FROM Follow WHERE pseudo = ?;")) {

            ps.setString(1, monPseudo);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String pseud = rs.getString("pseudo_follow");

                comptes.add(this.getCompte(monPseudo, pseud));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return comptes;

    }

    public String likerPublication(String monPseudo, int idPublication) {

        try (PreparedStatement ps = this.connection
                .prepareStatement("INSERT INTO `Like` (id_publication, pseudo) VALUES (?,?);")) {

            ps.setInt(1, idPublication);
            ps.setString(2, monPseudo);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return this.getSenderPublication(idPublication);

    }

    public String getSenderPublication(int idPublication) {

        try (PreparedStatement ps = this.connection
                .prepareStatement("SELECT pseudo FROM Publication WHERE id_publication = ?;")) {

            ps.setInt(1, idPublication);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("pseudo");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

    }

    public void supprimerPublication(int idPublication) {

        try (PreparedStatement ps = this.connection
                .prepareStatement("DELETE FROM Publication WHERE id_publication = ?;")) {

            ps.setInt(1, idPublication);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (PreparedStatement ps = this.connection.prepareStatement("DELETE FROM `Like` WHERE id_publication = ?;")) {

            ps.setInt(1, idPublication);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public Message getMessageById(int idMessage) {

        Message message = null;

        try (PreparedStatement ps = this.connection.prepareStatement("SELECT * FROM Message WHERE id_message = ?;")) {

            ps.setInt(1, idMessage);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                String pseudo = rs.getString("pseudo");
                String pseudoDestinateur = rs.getString("pseudo_destinataire");
                String contenu = rs.getString("contenu");
                byte[] vocal = rs.getBytes("vocal");
                byte[] image = rs.getBytes("image");
                Timestamp dateMessage = rs.getTimestamp("date_message");
                boolean lu = rs.getBoolean("lu");

                message = new Message(idMessage, this.getCompte("", pseudo), this.getCompte("", pseudoDestinateur),
                        contenu, vocal, image,
                        dateMessage, lu);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return message;

    }

    public void supprimerMessage(int idPublication) {

        try (PreparedStatement ps = this.connection.prepareStatement("DELETE FROM Message WHERE id_message = ?;")) {

            ps.setInt(1, idPublication);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Publication publierPublication(String monPseudo, String contenuPublication, byte[] imagePublication) {

        try (PreparedStatement ps = this.connection.prepareStatement(
                "INSERT INTO Publication (pseudo, contenu, image) VALUES (?,?,?);")) {

            ps.setString(1, monPseudo);
            ps.setString(2, contenuPublication);
            ps.setBytes(3, imagePublication);

            ps.executeUpdate();

            return this.getLastPublicationFromPseudo(monPseudo);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

    }

    public List<Message> getDerniersMessages(String monPseudo) {

        List<Message> messages = new ArrayList<>();

        try (PreparedStatement ps = this.connection.prepareStatement(
                "SELECT m1.* FROM message m1 JOIN (SELECT CASE WHEN pseudo = ? THEN pseudo_destinataire ELSE pseudo END AS autre_pseudo, MAX(date_message) AS max_date_message FROM message WHERE pseudo = ? OR pseudo_destinataire = ? GROUP BY autre_pseudo) m2 ON ((m1.pseudo = ? AND m1.pseudo_destinataire = m2.autre_pseudo) OR (m1.pseudo_destinataire = ? AND m1.pseudo = m2.autre_pseudo)) AND m1.date_message = m2.max_date_message ORDER BY date_message DESC;")) {

            ps.setString(1, monPseudo);
            ps.setString(2, monPseudo);
            ps.setString(3, monPseudo);
            ps.setString(4, monPseudo);
            ps.setString(5, monPseudo);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                int idMessage = rs.getInt("id_message");
                String pseudo = rs.getString("pseudo");
                String pseudoDestinateur = rs.getString("pseudo_destinataire");
                String contenu = rs.getString("contenu");
                byte[] vocal = rs.getBytes("vocal");
                byte[] image = rs.getBytes("image");
                Timestamp dateMessage = rs.getTimestamp("date_message");
                boolean lu = rs.getBoolean("lu");

                messages.add(new Message(idMessage, this.getCompte("", pseudo), this.getCompte("", pseudoDestinateur),
                        contenu, vocal, image,
                        dateMessage, lu));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return messages;

    }

    private Publication getLastPublicationFromPseudo(String monPseudo) {

        Publication publication = null;

        try (PreparedStatement ps = this.connection
                .prepareStatement(
                        "SELECT * FROM Publication WHERE pseudo = ? AND id_publication = (SELECT MAX(id_publication) FROM Publication WHERE pseudo=?);")) {

            ps.setString(1, monPseudo);
            ps.setString(2, monPseudo);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                int idPublication = rs.getInt("id_publication");
                String contenu = rs.getString("contenu");
                Timestamp datePublication = rs.getTimestamp("date_publication");
                byte[] image = rs.getBytes("image");

                publication = new Publication(idPublication, this.getCompte(rs.getString("pseudo"), monPseudo),
                        contenu, datePublication, image,
                        this.estCeQueJaiLike(monPseudo, idPublication), this.getNbLike(idPublication));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return publication;

    }

    public List<Message> getMessages(String monPseudo, String pseudo) {

        List<Message> messages = new ArrayList<>();

        try (PreparedStatement ps = this.connection
                .prepareStatement(
                        "SELECT * FROM Message WHERE (pseudo = ? AND pseudo_destinataire = ?) OR (pseudo = ? AND pseudo_destinataire = ?) ORDER BY date_message ASC;")) {

            ps.setString(1, monPseudo);
            ps.setString(2, pseudo);
            ps.setString(3, pseudo);
            ps.setString(4, monPseudo);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                int idMessage = rs.getInt("id_message");
                String pseud = rs.getString("pseudo");
                String pseudoDestinateur = rs.getString("pseudo_destinataire");
                String contenu = rs.getString("contenu");
                byte[] vocal = rs.getBytes("vocal");
                byte[] image = rs.getBytes("image");
                Timestamp dateMessage = rs.getTimestamp("date_message");
                boolean lu = rs.getBoolean("lu");

                messages.add(new Message(idMessage, this.getCompte("", pseud), this.getCompte("", pseudoDestinateur),
                        contenu, vocal, image,
                        dateMessage, lu));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return messages;

    }

    public Message envoyerMessage(String monPseudo, String pseudo, String contenu, byte[] image) {

        try (PreparedStatement ps = this.connection
                .prepareStatement(
                        "INSERT INTO Message (pseudo, pseudo_destinataire, contenu, image) VALUES (?,?,?,?);")) {

            ps.setString(1, monPseudo);
            ps.setString(2, pseudo);
            ps.setString(3, contenu);
            ps.setBytes(4, image);

            ps.executeUpdate();

            return this.getLastMessageFromPseudo(monPseudo);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

    }

    public Message envoyerVocal(String monPseudo, String pseudo, byte[] vocal) {

        try (PreparedStatement ps = this.connection
                .prepareStatement("INSERT INTO Message (pseudo, pseudo_destinataire, vocal) VALUES (?,?,?);")) {

            ps.setString(1, monPseudo);
            ps.setString(2, pseudo);
            ps.setBytes(3, vocal);

            ps.executeUpdate();

            return this.getLastMessageFromPseudo(monPseudo);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

    }

    public void setAllMessagesNonLuEnLu(String monPseudo, String pseudo) {

        try (PreparedStatement ps = this.connection
                .prepareStatement(
                        "UPDATE Message SET lu = 1 WHERE pseudo = ? AND pseudo_destinataire = ? AND lu = 0;")) {

            ps.setString(1, pseudo);
            ps.setString(2, monPseudo);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public Message getLastMessageFromPseudo(String monPseudo) {

        try (PreparedStatement ps = this.connection.prepareStatement(
                "SELECT * FROM Message WHERE pseudo = ? AND id_message = (SELECT MAX(id_message) FROM Message);")) {

            ps.setString(1, monPseudo);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                int idMessage = rs.getInt("id_message");
                String pseudo = rs.getString("pseudo");
                String pseudoDestinateur = rs.getString("pseudo_destinataire");
                String contenu = rs.getString("contenu");
                byte[] vocal = rs.getBytes("vocal");
                byte[] image = rs.getBytes("image");
                Timestamp dateMessage = rs.getTimestamp("date_message");
                boolean lu = rs.getBoolean("lu");

                return new Message(idMessage, this.getCompte("", pseudo), this.getCompte("", pseudoDestinateur),
                        contenu, vocal, image,
                        dateMessage, lu);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

    }

    public boolean hasFollower(String monPseudo, String pseudo) {

        try (PreparedStatement ps = this.connection
                .prepareStatement("SELECT * FROM Follow WHERE pseudo = ? AND pseudo_follow = ?;")) {

            ps.setString(1, pseudo);
            ps.setString(2, monPseudo);

            return ps.executeQuery().next();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;

    }

    public boolean hasFollowing(String monPseudo, String pseudo) {

        try (PreparedStatement ps = this.connection
                .prepareStatement("SELECT * FROM Follow WHERE pseudo = ? AND pseudo_follow = ?;")) {

            ps.setString(1, monPseudo);
            ps.setString(2, pseudo);

            return ps.executeQuery().next();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;

    }

    public String dislikerPublication(String monPseudo, int idPublication) {

        try (PreparedStatement ps = this.connection
                .prepareStatement("DELETE FROM `Like` WHERE id_publication = ? AND pseudo = ?;")) {

            ps.setInt(1, idPublication);
            ps.setString(2, monPseudo);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return this.getSenderPublication(idPublication);

    }

    public List<Publication> getPublications(String monPseudo) {

        List<Publication> publications = new ArrayList<>();

        try (PreparedStatement ps = this.connection
                .prepareStatement("SELECT * FROM Publication WHERE pseudo = ? ORDER BY date_publication DESC;")) {

            ps.setString(1, monPseudo);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                int idPublication = rs.getInt("id_publication");
                String contenu = rs.getString("contenu");
                Timestamp datePublication = rs.getTimestamp("date_publication");
                byte[] image = rs.getBytes("image");

                publications.add(new Publication(idPublication, this.getCompte(rs.getString("pseudo"), monPseudo),
                        contenu, datePublication, image,
                        this.estCeQueJaiLike(monPseudo, idPublication), this.getNbLike(idPublication)));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return publications;

    }

    public int getNbLike(int idPublication) {

        try (PreparedStatement ps = this.connection
                .prepareStatement("SELECT COUNT(*) FROM `Like` WHERE id_publication = ?;")) {

            ps.setInt(1, idPublication);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("COUNT(*)");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;

    }

    public boolean estCeQueJaiLike(String monPseudo, int idPublication) {

        try (PreparedStatement ps = this.connection
                .prepareStatement("SELECT * FROM `Like` WHERE pseudo = ? AND id_publication = ?;")) {

            ps.setString(1, monPseudo);
            ps.setInt(2, idPublication);

            return ps.executeQuery().next();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;

    }

    public List<Compte> getFollowers(String monPseudo) {

        List<Compte> comptes = new ArrayList<>();

        try (PreparedStatement ps = this.connection.prepareStatement("SELECT * FROM Follow WHERE pseudo_follow = ?;")) {

            ps.setString(1, monPseudo);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String pseud = rs.getString("pseudo");

                comptes.add(this.getCompte(pseud, monPseudo));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return comptes;

    }

    public void demanderSuivre(String monPseudo, String pseudo) {

        try (PreparedStatement ps = this.connection
                .prepareStatement("INSERT INTO Follow (pseudo, pseudo_follow) VALUES (?,?);")) {

            ps.setString(1, monPseudo);
            ps.setString(2, pseudo);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void demanderPlusSuivre(String monPseudo, String pseudo) {

        try (PreparedStatement ps = this.connection
                .prepareStatement("DELETE FROM Follow WHERE pseudo = ? and pseudo_follow = ?;")) {

            ps.setString(1, monPseudo);
            ps.setString(2, pseudo);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void modifierCompte(Compte compte) {

        try (PreparedStatement ps = this.connection
                .prepareStatement("UPDATE Compte SET photo_profil = ? WHERE pseudo = ?;")) {

            ps.setBytes(1, compte.getPhotoBytes());
            ps.setString(2, compte.getPseudo());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void sauvegarderCompte(String pseudo) {

        try (PreparedStatement ps = this.connection.prepareStatement("INSERT INTO Compte (pseudo) VALUES (?);")) {

            ps.setString(1, pseudo);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public int getNbPublications(String pseudo) {

        try (PreparedStatement ps = this.connection
                .prepareStatement("SELECT COUNT(*) FROM Publication WHERE pseudo = ?;")) {

            ps.setString(1, pseudo);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("COUNT(*)");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public List<Compte> rechercherComptes(String monPseudo, String recherche) {

        List<Compte> comptes = new ArrayList<>();

        try (PreparedStatement ps = this.connection.prepareStatement("SELECT * FROM Compte WHERE pseudo LIKE ?;")) {

            ps.setString(1, recherche + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String pseud = rs.getString("pseudo");
                byte[] photo = rs.getBytes("photo_profil");

                comptes.add(
                        new Compte(pseud, photo, this.getNbPublications(pseud), this.getNbFollowers(pseud),
                                this.getNbFollowings(pseud),
                                this.estCeQueJeSuisAbonne(monPseudo, pseud)));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return comptes;

    }

    public int getNbFollowings(String pseudo) {

        try (PreparedStatement ps = this.connection
                .prepareStatement("SELECT COUNT(*) FROM Follow WHERE pseudo = ?;")) {

            ps.setString(1, pseudo);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("COUNT(*)");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;

    }

    public int getNbFollowers(String pseudo) {

        try (PreparedStatement ps = this.connection
                .prepareStatement("SELECT COUNT(*) FROM Follow WHERE pseudo_follow = ?;")) {

            ps.setString(1, pseudo);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("COUNT(*)");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;

    }

    public boolean estCeQueJeSuisAbonne(String monPseudo, String pseudo) {

        try (PreparedStatement ps = this.connection
                .prepareStatement("SELECT * FROM Follow WHERE pseudo = ? AND pseudo_follow = ?;")) {

            ps.setString(1, monPseudo);
            ps.setString(2, pseudo);

            return ps.executeQuery().next();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;

    }

    public boolean hasCompte(String pseudo) {

        try (PreparedStatement ps = this.connection.prepareStatement("SELECT * FROM Compte WHERE pseudo = ?;")) {

            ps.setString(1, pseudo);

            return ps.executeQuery().next();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;

    }

    public boolean supprimerCompte(String pseudo) {

        try (PreparedStatement ps = this.connection.prepareStatement("DELETE FROM Compte WHERE pseudo=?;")) {

            ps.setString(1, pseudo);

            ps.executeUpdate();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;

    }

}
