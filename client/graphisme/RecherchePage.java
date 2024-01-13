package client.graphisme;

import java.util.HashMap;
import java.util.Map;

import caches.Compte;
import client.Main;
import client.graphisme.affichage.LabelF;
import javafx.scene.layout.VBox;

public class RecherchePage extends VBox {

    private Main main;
    private Map<String, CompteBox> comptes;

    public RecherchePage(Main main) {
        this.main = main;
        this.comptes = new HashMap<>();
    }

    public void insertCompte(Compte compte) {
        this.comptes.put(compte.getPseudo().toLowerCase(), new CompteBox(this.main, compte, false));
    }

    public void addCompteSimilar(String[] pseudos) {
        super.getChildren().clear();
        for (String pseudo : pseudos) {
            if (this.comptes.containsKey(pseudo)) {
                super.getChildren().add(this.comptes.get(pseudo));
            }
        }
        if (super.getChildren().size() == 0) {
            super.getChildren().add(new LabelF("Aucun résultat trouvés."));
        }
    }

}
