package serveur.sql;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import caches.Commentaire;
import caches.Compte;
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

    public synchronized boolean isConnected() {
        return this.connection != null;
    }

    public synchronized List<Publication> getPublicationsForUserAndFollowers(String pseudoToCheck, int limite) {
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
                        String content = resultSet.getString("content");
                        Blob vocal = resultSet.getBlob("vocal");
                        Timestamp date = resultSet.getTimestamp("date");
                        Blob photo = resultSet.getBlob("photo");
                        int likes = resultSet.getInt("total_likes");
                        boolean callerIsLiker = this.hasLikePublication(pseudoToCheck, idPublication);
                        List<Commentaire> commentaires = this.getCommentairesForPublication(idPublication);

                        Publication publication = new Publication(idPublication, pseudo, content, vocal, date, photo,
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

    public synchronized boolean hasLikePublication(String pseudoToCheck, int idPublicationToCheck) {
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

    public synchronized boolean isPseudoAlreadyExists(String pseudoToCheck) {
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

    public synchronized void saveNewCompte(String pseudo) {
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

    public synchronized Compte getCompteByPseudo(String pseudo) {
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

    public synchronized void likePublication(String pseudo, int idPublication) {

        if(this.hasLikePublication(pseudo, idPublication)) return;

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

    public synchronized void unlikePublication(String pseudo, int idPublication) {
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

    public synchronized boolean hasFollowToSenderPublication(String pseudo, int idPublication) {
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

    public synchronized int nbLikePublications(int idPublication){

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

    public void publierPublication(String pseudo, String text, byte[] vocal) {
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

        } catch (SQLException e) {

            e.printStackTrace();

        }

    }

}
