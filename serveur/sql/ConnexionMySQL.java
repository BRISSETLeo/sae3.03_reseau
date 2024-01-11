package serveur.sql;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import caches.Commentaire;
import caches.Compte;
import caches.MessageC;
import caches.Notification;
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

    public boolean isConnected() {
        return this.connection != null;
    }

    public List<Publication> getPublicationsForUserAndFollowers(String pseudoToCheck, int limite) {
        List<Publication> publications = new ArrayList<>();

        try {

            String sqlQuery = "SELECT" +
                    " p.id_publication," +
                    " p.pseudo," +
                    " p.content," +
                    " p.vocal," +
                    " p.date," +
                    " p.photo," +
                    " COUNT(l.pseudo) AS total_likes" +
                    " FROM" +
                    " publications p" +
                    " LEFT JOIN" +
                    " likes l ON p.id_publication = l.id_publication" +
                    " WHERE" +
                    " p.pseudo = ?" +
                    " OR p.pseudo IN (SELECT pseudo_follow FROM follows WHERE pseudo = ?)" +
                    " GROUP BY" +
                    " p.id_publication" +
                    " ORDER BY" +
                    " p.date DESC" +
                    " LIMIT ?;";

            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {

                statement.setString(1, pseudoToCheck);
                statement.setString(2, pseudoToCheck);
                statement.setInt(3, limite);

                try (ResultSet resultSet = statement.executeQuery()) {

                    while (resultSet.next()) {

                        int idPublication = resultSet.getInt("id_publication");
                        String pseudo = resultSet.getString("pseudo");
                        Compte compte = this.getCompteByPseudo(pseudo);
                        String content = resultSet.getString("content");
                        Blob vocal = resultSet.getBlob("vocal");
                        Timestamp date = resultSet.getTimestamp("date");
                        Blob photo = resultSet.getBlob("photo");
                        int likes = resultSet.getInt("total_likes");
                        boolean callerIsLiker = this.hasLikePublication(pseudoToCheck, idPublication);
                        List<Commentaire> commentaires = this.getCommentairesForPublication(idPublication);

                        Publication publication = new Publication(idPublication, compte, content, vocal, date, photo,
                                likes,
                                callerIsLiker, commentaires);
                        publications.add(publication);

                    }

                }

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return publications;
    }

    private List<Commentaire> getCommentairesForPublication(int idPublication) {
        List<Commentaire> commentaires = new ArrayList<>();

        try {

            String sqlQuery = "SELECT" +
                    " *" +
                    " FROM" +
                    " commentaires" +
                    " WHERE" +
                    " id_publication = ?" +
                    " ORDER BY" +
                    " date ASC;";

            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {

                statement.setInt(1, idPublication);

                try (ResultSet resultSet = statement.executeQuery()) {

                    while (resultSet.next()) {

                        int idCommentaire = resultSet.getInt("id_commentaire");
                        String pseudo = resultSet.getString("pseudo");
                        String content = resultSet.getString("content");
                        byte vocal = resultSet.getByte("vocal");
                        Timestamp date = resultSet.getTimestamp("date");
                        Blob photo = resultSet.getBlob("photo");

                        Commentaire commentaire = new Commentaire(idCommentaire, pseudo, idPublication, content, vocal,
                                date, photo);
                        commentaires.add(commentaire);

                    }

                }

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return commentaires;
    }

    public boolean hasLikePublication(String pseudoToCheck, int idPublicationToCheck) {
        try {

            String sqlQuery = "SELECT * FROM likes WHERE pseudo = ? and id_publication = ?;";

            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {

                statement.setString(1, pseudoToCheck);
                statement.setInt(2, idPublicationToCheck);

                try (ResultSet resultSet = statement.executeQuery()) {

                    return resultSet.next();

                }

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return false;
    }

    public boolean isPseudoAlreadyExists(String pseudoToCheck) {
        try {

            String sqlQuery = "SELECT COUNT(*) AS count FROM comptes WHERE pseudo = ?;";

            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {

                statement.setString(1, pseudoToCheck);

                try (ResultSet resultSet = statement.executeQuery()) {

                    if (resultSet.next()) {

                        int count = resultSet.getInt("count");
                        return count > 0;

                    }

                }

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return false;
    }

    public void saveNewCompte(String pseudo) {
        try {

            if (isPseudoAlreadyExists(pseudo))
                return;

            String sqlQuery = "INSERT INTO comptes (pseudo) VALUES (?);";

            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {

                statement.setString(1, pseudo);
                statement.executeUpdate();

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }
    }

    public Compte getCompteByPseudo(String pseudo) {
        try {

            String sqlQuery = "SELECT * FROM comptes WHERE pseudo = ?;";

            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {

                statement.setString(1, pseudo);

                try (ResultSet resultSet = statement.executeQuery()) {

                    if (resultSet.next()) {

                        Blob image = resultSet.getBlob("image");

                        return new Compte(pseudo, image);

                    }

                }

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return null;
    }

    public void likePublication(String pseudo, int idPublication) {

        if (this.hasLikePublication(pseudo, idPublication))
            return;

        try {

            String sqlQuery = "INSERT INTO likes (pseudo, id_publication) VALUES (?, ?);";

            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {

                statement.setString(1, pseudo);
                statement.setInt(2, idPublication);
                statement.executeUpdate();

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }
    }

    public void unlikePublication(String pseudo, int idPublication) {
        try {

            String sqlQuery = "DELETE FROM likes WHERE pseudo = ? AND id_publication = ?;";

            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {

                statement.setString(1, pseudo);
                statement.setInt(2, idPublication);
                statement.executeUpdate();

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }
    }

    public boolean hasFollowToSenderPublication(String pseudo, int idPublication) {
        try {

            String sqlQuery = "select f.pseudo, p.pseudo as pseudo_follow from follows f join publications p on p.pseudo = f.pseudo_follow where f.pseudo = ? and id_publication = ?;";

            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {

                statement.setString(1, pseudo);
                statement.setInt(2, idPublication);

                try (ResultSet resultSet = statement.executeQuery()) {

                    return resultSet.next();

                }

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return false;
    }

    public boolean isOwnerPublication(String pseudo, int idPublication) {
        try {

            String sqlQuery = "SELECT * FROM publications WHERE pseudo = ? AND id_publication = ?;";

            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {

                statement.setString(1, pseudo);
                statement.setInt(2, idPublication);

                try (ResultSet resultSet = statement.executeQuery()) {

                    return resultSet.next();

                }

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return false;
    }

    public int nbLikePublications(int idPublication) {

        try {

            String sqlQuery = "SELECT COUNT(*) AS count FROM likes WHERE id_publication = ?;";

            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {

                statement.setInt(1, idPublication);

                try (ResultSet resultSet = statement.executeQuery()) {

                    if (resultSet.next()) {

                        int count = resultSet.getInt("count");
                        return count;

                    }

                }

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return 0;
    }

    public void enregistrerProfil(String pseudo, Blob image){

        try {

            String sqlQuery = "UPDATE comptes SET image = ? WHERE pseudo = ?;";

            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {

                statement.setBlob(1, image);
                statement.setString(2, pseudo);
                statement.executeUpdate();

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

    }

    public Publication publierPublication(String pseudo, String text, byte[] vocal) {

        try {

            String sqlQuery = "INSERT INTO publications (pseudo, content, vocal, date, photo) VALUES (?, ?, ?, ?, ?);";

            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {

                statement.setString(1, pseudo);
                statement.setString(2, text);
                statement.setBytes(3, vocal);
                statement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
                statement.setBytes(5, null);
                statement.executeUpdate();

            }

            return this.getPublication();

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return null;

    }

    public Publication getPublication() {

        try {

            String sqlQuery = "SELECT * FROM publications WHERE id_publication = (SELECT MAX(id_publication) FROM publications);";

            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {

                try (ResultSet resultSet = statement.executeQuery()) {

                    if (resultSet.next()) {

                        int idPublication = resultSet.getInt("id_publication");
                        String pseudo = resultSet.getString("pseudo");
                        Compte compte = this.getCompteByPseudo(pseudo);
                        String content = resultSet.getString("content");
                        Blob vocal = resultSet.getBlob("vocal");
                        Timestamp date = resultSet.getTimestamp("date");
                        Blob photo = resultSet.getBlob("photo");
                        int likes = 0;
                        boolean callerIsLiker = false;
                        List<Commentaire> commentaires = Arrays.asList();

                        Publication publication = new Publication(idPublication, compte, content, vocal, date, photo,
                                likes,
                                callerIsLiker, commentaires);
                        return publication;

                    }

                }

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return null;
    }

    public boolean hasFollowTo(String pseudo, String pseudoToCheck) {
        try {

            String sqlQuery = "SELECT * FROM follows WHERE pseudo = ? AND pseudo_follow = ?;";

            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {

                statement.setString(1, pseudo);
                statement.setString(2, pseudoToCheck);

                try (ResultSet resultSet = statement.executeQuery()) {

                    return resultSet.next();

                }

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return false;
    }

    public List<Compte> getFollow(String pseudo) {
        List<Compte> comptes = new ArrayList<>();

        try {

            String sqlQuery = "SELECT" +
                    " c.pseudo," +
                    " c.image" +
                    " FROM" +
                    " comptes c" +
                    " WHERE" +
                    " c.pseudo IN (SELECT pseudo_follow FROM follows WHERE pseudo = ?)" +
                    " OR c.pseudo IN (SELECT pseudo_dest FROM messages WHERE pseudo = ?)" +
                    " OR c.pseudo IN (SELECT pseudo FROM messages WHERE pseudo_dest = ?)" +
                    " ORDER BY" +
                    " c.pseudo ASC;";

            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {

                statement.setString(1, pseudo);
                statement.setString(2, pseudo);
                statement.setString(3, pseudo);

                try (ResultSet resultSet = statement.executeQuery()) {

                    while (resultSet.next()) {

                        String pseud = resultSet.getString("pseudo");
                        Blob image = resultSet.getBlob("image");

                        Compte compte = new Compte(pseud, image);
                        comptes.add(compte);

                    }

                }

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return comptes;
    }

    public void supprimerPublication(int idPublication) {
        try {

            String sqlQuery = "DELETE FROM publications WHERE id_publication = ?;";

            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {

                statement.setInt(1, idPublication);
                statement.executeUpdate();

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }
    }

    public List<MessageC> getMessages(String pseudo, String pseud) {
        List<MessageC> messages = new ArrayList<>();

        try {

            String sqlQuery = "SELECT" +
                    " m.id_message," +
                    " m.pseudo," +
                    " m.pseudo_dest," +
                    " m.content," +
                    " m.vocal," +
                    " m.date," +
                    " m.photo" +
                    " FROM" +
                    " messages m" +
                    " WHERE" +
                    " (m.pseudo = ? AND m.pseudo_dest = ?)" +
                    " OR (m.pseudo_dest = ? AND m.pseudo = ?)" +
                    " ORDER BY" +
                    " m.date ASC;";

            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {

                statement.setString(1, pseudo);
                statement.setString(2, pseud);
                statement.setString(3, pseudo);
                statement.setString(4, pseud);

                try (ResultSet resultSet = statement.executeQuery()) {

                    while (resultSet.next()) {

                        int idMessage = resultSet.getInt("id_message");
                        String pseudo1 = resultSet.getString("pseudo");
                        String pseudo2 = resultSet.getString("pseudo_dest");
                        String content = resultSet.getString("content");
                        Blob vocal = resultSet.getBlob("vocal");
                        Timestamp date = resultSet.getTimestamp("date");
                        Blob photo = resultSet.getBlob("photo");

                        MessageC message = new MessageC(idMessage, pseudo1, pseudo2, content, vocal, date, photo);
                        messages.add(message);

                    }

                }

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return messages;
    }

    public List<Notification> getNotifications(String pseudo) {
        List<Notification> notifications = new ArrayList<>();

        try {

            String sqlQuery = "SELECT" +
                    " *" +
                    " FROM" +
                    " notifications" +
                    " WHERE" +
                    " pseudo = ?" +
                    " ORDER BY" +
                    " date DESC;";

            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {

                statement.setString(1, pseudo);

                try (ResultSet resultSet = statement.executeQuery()) {

                    while (resultSet.next()) {

                        int idNotification = resultSet.getInt("id_notification");
                        String pseudo1 = resultSet.getString("pseudo");
                        String pseudoDest = resultSet.getString("pseudo_notif");
                        String type = resultSet.getString("type");
                        int id = resultSet.getInt("id");
                        Timestamp date = resultSet.getTimestamp("date");
                        boolean lu = resultSet.getBoolean("lu");

                        Notification notification = new Notification(idNotification, pseudo1, pseudoDest, type, id,
                                date, lu);
                        notifications.add(notification);

                    }

                }

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return notifications;
    }

    public synchronized MessageC envoyerMessage(MessageC message) {

        try {

            String sqlQuery = "INSERT INTO messages (pseudo, pseudo_dest, content, vocal, date, photo) VALUES (?, ?, ?, ?, ?, ?);";

            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {

                statement.setString(1, message.getPseudoExpediteur());
                statement.setString(2, message.getPseudoDestinataire());
                statement.setString(3, message.getContent());
                statement.setBlob(4, message.getVocal());
                statement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
                statement.setBlob(6, message.getPhoto());
                statement.executeUpdate();

                return this.getMessage();

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return null;

    }

    public MessageC getMessage() {

        try {

            String sqlQuery = "SELECT * FROM messages WHERE id_message = (SELECT MAX(id_message) FROM messages);";

            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {

                try (ResultSet resultSet = statement.executeQuery()) {

                    if (resultSet.next()) {

                        int idMessage = resultSet.getInt("id_message");
                        String pseudo1 = resultSet.getString("pseudo");
                        String pseudo2 = resultSet.getString("pseudo_dest");
                        String content = resultSet.getString("content");
                        Blob vocal = resultSet.getBlob("vocal");
                        Timestamp date = resultSet.getTimestamp("date");
                        Blob photo = resultSet.getBlob("photo");

                        MessageC message = new MessageC(idMessage, pseudo1, pseudo2, content, vocal, date, photo);
                        return message;

                    }

                }

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return null;
    }

}
