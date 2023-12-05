import java.sql.*;
import java.util.ArrayList;

public class UtilisateurBD {
	ConnexionMySQL laConnexion;
	Statement st;

	public UtilisateurBD(ConnexionMySQL laConnexion){
		this.laConnexion=laConnexion;
	}

	public int insererUtilisateur(Utilisateur user) throws SQLException {
		String query = "INSERT INTO UTILISATEUR (NOM_USER) VALUES ('" + user.getNom() + "')";
		st = laConnexion.createStatement();
		int result = st.executeUpdate(query);
		return result;
	}
	
	public void effacerJoueur(String nom) throws SQLException {
		String query = "DELETE FROM UTILISATEUR WHERE NOM_USER = " + nom;
		st = laConnexion.createStatement();
		st.executeUpdate(query);
	}


	public ArrayList<Utilisateur> listeDesJoueurs() throws SQLException{
        return null; // TODO
	}

}

