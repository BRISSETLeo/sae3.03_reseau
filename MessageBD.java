import java.sql.*;

import graphique.BD.ConnexionMySQL;

public class MessageBD {
    ConnexionMySQL laConnexion;
    Statement st;

    public MessageBD(ConnexionMySQL laConnexion){
        this.laConnexion=laConnexion;
    }

    public int insererMessage(Message message) throws SQLException {
        String query = "INSERT INTO MESSAGE (CONTENU, AUTEUR, DATE, NBLIKES) VALUES ('" + message.getContenu() + "', '" + message.getAuteur() + "', '" + message.getDate() + "', '" + message.getNbLikes() + "')";
        st = laConnexion.createStatement();
        int result = st.executeUpdate(query);
        return result;
    }

    
}
