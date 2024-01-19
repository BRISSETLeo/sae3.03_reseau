package client.controlers.connexion;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import client.Main;
import client.vues.Connexion;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class SeConnecter implements EventHandler<ActionEvent> {

    private final Main main;
    private final Connexion pageDeConnexion;

    public SeConnecter(Main main, Connexion pageDeConnexion) {
        this.main = main;
        this.pageDeConnexion = pageDeConnexion;
    }

    @Override
    public void handle(ActionEvent event) {

        this.handle();

    }

    public void handle() {
        String pseudo = this.pageDeConnexion.getPseudoField().getText();
        String ip = this.pageDeConnexion.getIpField().getText();

        if (pseudo.isBlank() || ip.isBlank()) {
            this.pageDeConnexion.getErreur().setText("Veuillez remplir tous les champs");
            return;
        } else if (pseudo.length() > 25) {
            this.pageDeConnexion.getErreur().setText("Veuillez choisir un pseudo plus court (<= 25 caractères))");
            return;
        } else if (!this.main.seConnecter(pseudo, ip)) {
            this.pageDeConnexion.getErreur().setText("Impossible de se connecter au serveur.");
            return;
        } else if (this.pageDeConnexion.getSeSouvenirDeMoi().isSelected()) {
            this.sauvegarderLesIdentifiants(pseudo, ip);
        }

    }

    public void sauvegarderLesIdentifiants(String pseudo, String ip) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("./client/sauvegarde/identification.txt"))) {
            writer.write("ip: " + ip + "\n");
            writer.write("pseudo: " + pseudo);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
