package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.sound.sampled.AudioFormat;

import caches.Compte;
import caches.MessageC;
import caches.Publication;
import client.graphisme.Accueil;
import client.graphisme.Connexion;
import client.graphisme.Message;
import client.graphisme.Navigation;
import client.son.Son;
import client.graphisme.Messagerie;
import enums.CheminIMG;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import client.graphisme.Barre;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {

    public static String fontPath = "file:./client/css/police/Poppins-Regular.ttf";

    private Client client;
    private BorderPane root;
    private Connexion connexion;
    private Navigation navigation;
    private Barre barre;
    private Accueil accueil;
    private client.graphisme.Publication publication;
    private Message message;
    private Son son;
    private Messagerie messagerie;
    private Image logo;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        this.connexion = new Connexion(this);
        this.accueil = new Accueil(this);
        this.barre = new Barre();
        this.navigation = new Navigation(this);
        this.messagerie = new Messagerie(this);
        this.message = new Message(this);
        this.root = new BorderPane(this.connexion);
        this.logo = new Image(CheminIMG.LOGO.getChemin());
        this.publication = new client.graphisme.Publication(this);
        this.son = new Son(this);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        Scene scene = new Scene(this.root);

        double windowWidthFraction = 0.8;
        double windowHeightFraction = 0.8;

        stage.setWidth(bounds.getWidth() * windowWidthFraction);
        stage.setHeight(bounds.getHeight() * windowHeightFraction);
        stage.setX((bounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((bounds.getHeight() - stage.getHeight()) / 2);
        stage.setScene(scene);
        stage.setTitle("SysX");
        stage.getIcons().add(this.logo);
        stage.show();
    }

    public void connecterLeClient(String adresse, String pseudo) {
        this.client = new Client(this, pseudo, adresse);
    }

    public void erreurDansAdresse() {
        this.connexion.erreurDansAdresse();
    }

    public void erreurDansPseudo() {
        this.connexion.erreurDansPseudo();
    }

    public void demanderCreationDeCompte() {
        Platform.runLater(() -> {
            this.connexion.demanderCreationDeCompte();
        });
    }

    public void creerCompte() {
        this.client.creerCompte();
    }

    public void fermer() {
        this.client.fermer();
    }

    public void mettreLaPageAccueil() {
        Platform.runLater(() -> {
            this.root.setTop(this.barre);
            this.root.setLeft(this.navigation);
            this.root.setCenter(this.accueil);
        });
    }

    public void afficherPublication(Publication publication, boolean hasNewPublication) {
        Platform.runLater(() -> this.accueil.ajouterPublication(publication, hasNewPublication));
    }

    public void sauvegarderIdentifiant(String ip, String pseudo) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("./client/sauvegarde/identification.txt"))) {
            writer.write("ip: " + ip + "\n");
            writer.write("pseudo: " + pseudo);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String demanderIdentifiant() {
        try (BufferedReader reader = new BufferedReader(new FileReader("./client/sauvegarde/identification.txt"))) {
            if (!reader.ready())
                return null;
            String ip = reader.readLine().split(": ")[1];
            String pseudo = reader.readLine().split(": ")[1];
            return ip + ":" + pseudo;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void likePublication(int id) {
        this.client.likePublication(id);
    }

    public void unlikePublication(int id) {
        this.client.unlikePublication(id);
    }

    public void ajouterLike(int id, int like, boolean isMe) {
        Platform.runLater(() -> this.accueil.ajouterLike(id, like, isMe));
    }

    public void removeLike(int id, int like, boolean isMe) {
        Platform.runLater(() -> this.accueil.removeLike(id, like, isMe));
    }

    public void ajouterPagePublication() {
        this.enleverPage();
        this.accueil.ajouterPage(this.publication);
    }

    public void ajouterPageMessage() {
        this.enleverPage();
        this.accueil.ajouterPage(this.message);
    }

    public void enleverPage() {
        this.accueil.enleverPage();
    }

    public void startVocal() {
        this.publication.resetSon();
        this.son.start();
        this.publication.mettreEnregistrementButtonAOn();
    }

    public void aucunSon() {
        Platform.runLater(() -> this.publication.aucunSon());
    }

    public void resetSon() {
        Platform.runLater(() -> this.publication.resetSon());
    }

    public void stopVocal() {
        this.son.stopVocal();
        this.son = new Son(this);
    }

    public void vocalFini() {
        Platform.runLater(() -> this.publication.mettreEnregistrementButtonAOff());
    }

    public void nouveauVocal(List<Double> averages) {
        Platform.runLater(() -> this.publication.messageVocal(averages));
    }

    public void jouerSon() {
        if (!this.son.isEcouteSon()) {
            this.son.jouerSon();
            this.publication.jouerSon();
        }
    }

    public void arreterSon() {
        this.son.arreterSon();
        this.publication.arreterSon();
    }

    public void mettreEnPauseSon() {
        if (this.son.isEcouteSon()) {
            this.son.mettreEnPauseSon();
            this.publication.mettreEnPauseSon();
        }
    }

    public void afficherMessage() {
        this.client.getMessages();
        this.root.setCenter(this.messagerie);
    }

    public void reprendreSon() {
        this.son.reprendreSon();
        this.publication.reprendreSon();
    }

    public AudioFormat getAudioFormat() {
        return this.son.getAudioFormat();
    }

    public void updateSon(int nbSecMax, int nbSecActuel) {
        Platform.runLater(() -> this.publication.updateSon(nbSecMax, nbSecActuel));
    }

    public void publierPublication() {
        String text = this.publication.getPublication().getText();
        byte[] vocal = this.son.getAudioData();
        if (text.length() == 0 && vocal == null) {
            this.publication.erreur();
            return;
        }
        this.client.publierPublication(text, vocal);
    }

    public List<Double> playAudio(byte[] audio) {
        return this.son.playAudio(audio);
    }

    public void afficherCompte(Compte compte) {
        Platform.runLater(() -> this.message.ajouterCompte(compte));
    }

    public void afficherMessage(MessageC message) {
        Platform.runLater(() -> this.messagerie.ajouterMessage(message));
    }

    public String getPseudo() {
        return this.client.getPseudo();
    }

    public static Region createRegion() {
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        VBox.setVgrow(region, Priority.ALWAYS);
        return region;
    }

}
