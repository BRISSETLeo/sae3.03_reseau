package client.controle;

import client.Main;
import client.lexicographie.Trie;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

public class Recherche implements EventHandler<KeyEvent> {

    private Main main;

    public Recherche(Main main) {
        this.main = main;
    }

    @Override
    public void handle(KeyEvent event) {

        Trie trie = main.getTrie();
        String resultat = this.main.getResultat().toLowerCase();

        if (resultat.equals(""))
            this.main.enleverPageDroite();
        else
            this.main.addCompteSimilar(trie.findAllWordsWithPrefix(resultat).split(" "));

    }

}
