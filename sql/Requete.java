package sql;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import caches.Publications;

public class Requete {

    private Requete() {
    }

    private synchronized static boolean dejaUser(ConnexionMySQL connexionMySQL, String pseudo) {
        String sql = "SELECT * FROM UTILISATEUR WHERE NOM_USER = ?;";
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connexionMySQL.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, pseudo);
            return preparedStatement.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private synchronized static boolean publiExist(ConnexionMySQL connexionMySQL, String idPublication) {
        String sql = "SELECT * FROM MESSAGE WHERE ID_MESSAGE = ?;";
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connexionMySQL.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, idPublication);
            return preparedStatement.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public synchronized static void addUser(ConnexionMySQL connexionMySQL, String pseudo) {
        if (!dejaUser(connexionMySQL, pseudo)) {
            String sql = "INSERT INTO UTILISATEUR (NOM_USER) VALUES (?);";
            PreparedStatement preparedStatement;
            try {
                preparedStatement = connexionMySQL.getConnection().prepareStatement(sql);
                preparedStatement.setString(1, pseudo);
                preparedStatement.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized static List<Publications> getFollowersPublications(ConnexionMySQL connexionMySQL,
            String pseudo) {
        List<Publications> messages = new ArrayList<>();
        List<String> followings = getFollowings(connexionMySQL, pseudo);
        String sql = "SELECT * FROM MESSAGE WHERE NOM_USER IN (";
        for (int i = 0; i < followings.size(); i++) {
            sql += "'" + followings.get(i) + "'";
            if (i < followings.size() - 1) {
                sql += ",";
            }
        }
        sql += ") ORDER BY DATE_MESSAGE DESC;";
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connexionMySQL.getConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                messages.add(new Publications(resultSet.getInt("ID_MESSAGE") + "", resultSet.getString("NOM_USER"),
                        resultSet.getString("CONTENUE"),
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                .format(new Date(resultSet.getTimestamp("DATE_MESSAGE").getTime())),
                        resultSet.getInt("LIKES")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    public synchronized static List<String> getFollowings(ConnexionMySQL connexionMySQL, String pseudo) {
        List<String> followings = new ArrayList<>();
        String sql = "SELECT NOM_FOLLOW FROM FOLLOW WHERE NOM_USER = ?;";
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connexionMySQL.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, pseudo);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String following = resultSet.getString("NOM_FOLLOW");
                followings.add(following);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return followings;
    }

    public synchronized static Publications newLikes(ConnexionMySQL connexionMySQL, String idPublication) {
        if (publiExist(connexionMySQL, idPublication)) {
            String sql = "UPDATE MESSAGE SET LIKES = ? WHERE ID_MESSAGE = ?;";
            PreparedStatement preparedStatement;
            try {
                preparedStatement = connexionMySQL.getConnection().prepareStatement(sql);
                preparedStatement.setInt(1, getLikes(connexionMySQL, idPublication) + 1);
                preparedStatement.setString(2, idPublication);
                preparedStatement.executeUpdate();
                return getPublication(connexionMySQL, idPublication);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private synchronized static Publications getPublication(ConnexionMySQL connexionMySQL, String idPublication) {
        String sql = "SELECT * FROM MESSAGE WHERE ID_MESSAGE = ?;";
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connexionMySQL.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, idPublication);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Publications(resultSet.getInt("ID_MESSAGE") + "", resultSet.getString("NOM_USER"),
                        resultSet.getString("CONTENUE"),
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                .format(new Date(resultSet.getTimestamp("DATE_MESSAGE").getTime())),
                        resultSet.getInt("LIKES"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private synchronized static int getLikes(ConnexionMySQL connexionMySQL, String idPublication) {
        String sql = "SELECT LIKES FROM MESSAGE WHERE ID_MESSAGE = ?;";
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connexionMySQL.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, idPublication);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("LIKES");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
