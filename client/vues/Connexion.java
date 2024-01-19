package client.vues;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import client.Main;
import client.controlers.connexion.SeConnecter;
import client.controlers.connexion.SeConnecterEntrer;
import client.utilitaire.CSS;
import client.utilitaire.IMAGE;
import client.vues.nodes.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class Connexion extends StackPane {

    private final TextField pseudoField;
    private final TextField ipField;
    private final Label erreur;

    private final CheckBox seSouvenirDeMoi;

    public Connexion(Main main) {

        this.seSouvenirDeMoi = new CheckBox("Se souvenir de moi");

        String identifiants = this.demanderIdentifiant();
        if (identifiants != null) {
            String[] identifiantsSplit = identifiants.split(":");
            this.ipField = new TextField(identifiantsSplit[0]);
            this.pseudoField = new TextField(identifiantsSplit[1]);
        } else {
            this.ipField = new TextField();
            this.pseudoField = new TextField();
        }

        this.pseudoField.setPromptText("Pseudo (ex: John)");
        this.ipField.setPromptText("Adresse IP (ex: localhost)");

        this.erreur = new Label();
        this.erreur.getStyleClass().add("erreur");

        Button seConnecter = new Button("Se connecter");
        Label copyrigth = new Label("Copyright © 2024 - SysX, Inc");

        SeConnecter seConnecterHandler = new SeConnecter(main, this);
        seConnecter.setOnAction(seConnecterHandler);

        HBox pseudoBox = new HBox(new ImageView(IMAGE.USER.getChemin(), 20), this.pseudoField);
        HBox ipBox = new HBox(new ImageView(IMAGE.IP_ADDRESS.getChemin(), 20), this.ipField);

        super.getChildren()
                .addAll(new VBox(20, pseudoBox, ipBox, this.seSouvenirDeMoi, seConnecter, this.erreur, copyrigth));
        super.getStylesheets().add(CSS.CONNEXION.getChemin());
        super.setOnKeyPressed(new SeConnecterEntrer(seConnecterHandler));

    }

    public TextField getPseudoField() {
        return this.pseudoField;
    }

    public TextField getIpField() {
        return this.ipField;
    }

    public Label getErreur() {
        return this.erreur;
    }

    public CheckBox getSeSouvenirDeMoi() {
        return this.seSouvenirDeMoi;
    }

    private String demanderIdentifiant() {
        try (BufferedReader reader = new BufferedReader(new FileReader("./client/sauvegarde/identification.txt"))) {
            if (!reader.ready())
                return null;
            String ip = reader.readLine().split(": ")[1];
            String pseudo = reader.readLine().split(": ")[1];
            return ip + ":" + pseudo;
        } catch (IOException ignored) {
        }
        return null;
    }

}
