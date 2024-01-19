package client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;

import caches.Compte;
import caches.Publication;
import client.utilitaire.IMAGE;
import client.vues.Accueil;
import client.vues.Connexion;
import client.vues.Message;
import client.vues.Navigation;
import client.vues.Profil;
import client.vues.MonProfil;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {

    private BorderPane root;
    private Connexion pageDeConnexion;
    private Navigation pageDeNavigation;
    private MonProfil pageDeMonProfil;
    private Accueil pageDAccueil;
    private Profil pageDeProfil;
    private client.vues.Publication pageDeNewPublication;
    private Message pageDeMessage;

    private boolean clientEstSurLaPageDesMessages;

    private Client client;
    private Compte monCompte;

    @Override
    public void init() throws Exception {

        this.root = new BorderPane();
        this.pageDeConnexion = new Connexion(this);
        this.pageDeNavigation = new Navigation(this);
        this.pageDeMonProfil = new MonProfil(this);
        this.pageDeProfil = new Profil(this);
        this.pageDAccueil = new Accueil(this);
        this.pageDeNewPublication = new client.vues.Publication(this);
        this.pageDeMessage = new Message(this);
        this.clientEstSurLaPageDesMessages = false;

        this.root.centerProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                this.clientEstSurLaPageDesMessages = false;
                if (newValue instanceof Accueil)
                    this.pageDAccueil.redimenssionerScroll();
                else if (newValue instanceof client.vues.Publication)
                    this.pageDeNewPublication.clearAll();
                else if (newValue instanceof Message) {
                    this.pageDeMessage.redimenssionerScroll();
                    this.clientEstSurLaPageDesMessages = true;
                }
            }
        });

    }

    @Override
    public void start(Stage stage) throws Exception {

        this.root.setCenter(this.pageDeConnexion);

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        Scene scene = new Scene(this.root);

        stage.setOnCloseRequest(event -> {
            if (this.client != null && this.client.isConnected()) {
                this.client.closeSocket();
            }
        });

        double windowWidthFraction = 0.8;
        double windowHeightFraction = 0.8;

        stage.setWidth(bounds.getWidth() * windowWidthFraction);
        stage.setHeight(bounds.getHeight() * windowHeightFraction);
        stage.setX((bounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((bounds.getHeight() - stage.getHeight()) / 2);
        stage.setScene(scene);
        stage.setTitle("SysX");
        stage.getIcons().add(new Image(IMAGE.LOGO.getChemin()));
        stage.show();

    }

    public boolean seConnecter(String pseudo, String ip) {
        this.client = new Client(this, pseudo, ip, false);
        boolean isConnected = this.client.isConnected();
        if (isConnected) {
            this.client.start();
        }
        return isConnected;
    }

    public void afficherLaPageCreerCompte() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Vous n'avez pas de compte");
            alert.setContentText("Voulez-vous vous inscrire ?");
            alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {
                this.client.accepterDeCreerCompte();
            } else {
                this.client.closeSocket();
            }
        });
    }

    public void seDeconnecter() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Vous allez être déconnecté");
            alert.setContentText("Voulez-vous vous déconnecter ?");
            alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {
                System.exit(0);
            }
        });
    }

    public void supprimerMessage(int idMessage) {
        Platform.runLater(() -> this.pageDeMessage.supprimerMessage(idMessage));
    }

    public void deconnexion() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Votre compte a été supprimé de la base de donnée");
            alert.setContentText("Vous allez être déconnecté");
            alert.showAndWait();
            System.exit(0);
        });
    }

    public void afficherPageMessage() {
        this.root.setCenter(this.pageDeMessage);
    }

    public void initialiser() {
        Platform.runLater(() -> {
            this.pageDeNavigation.init();
            this.pageDeMonProfil.init();
            this.root.setLeft(this.pageDeNavigation);
            this.root.setCenter(this.pageDAccueil);
        });
    }

    public void afficherPageDAccueil() {
        this.root.setCenter(this.pageDAccueil);
    }

    public void afficherPageDeNewPublication() {
        this.root.setCenter(this.pageDeNewPublication);
    }

    public Client getClient() {
        return this.client;
    }

    public void afficherLaPageDeProfil(Compte compte) {
        Platform.runLater(() -> {
            this.pageDeProfil.init(compte);
            this.root.setCenter(this.pageDeProfil);
        });
    }

    public static Region createRegion() {
        Region region = new Region();
        VBox.setVgrow(region, Priority.ALWAYS);
        HBox.setHgrow(region, Priority.ALWAYS);
        return region;
    }

    public void afficherPageDeMonProfil() {
        this.root.setCenter(this.pageDeMonProfil);
    }

    public void mettreAJourPhoto() {
        Platform.runLater(() -> {
            this.pageDeNavigation.changerImage();
            this.pageDAccueil.getPageDeRecherche().changerImage(this.monCompte);
        });
    }

    public void resultatDeLaRecherche(List<Compte> comptes) {
        Platform.runLater(() -> {
            this.pageDAccueil.getPageDeRecherche().afficherResultatDeLaRecherche(comptes);
        });
    }

    public void miseAJourProfil(Compte compte) {
        Platform.runLater(() -> {
            this.pageDAccueil.mettreAJourProfilPublication(compte);
            this.pageDAccueil.getPageDeRecherche().changerImage(compte);
        });
    }

    public void afficherHistorique(List<caches.Message> messages) {
        Platform.runLater(() -> {
            this.pageDeMessage.afficherHistorique(messages);
        });
    }

    public void mettreAJourLeSuiviProfil(int nbAbonnes, boolean estCeQueJeLeSuis) {
        Platform.runLater(() -> {
            this.pageDeProfil.mettreAJourLeSuiviProfil(nbAbonnes, estCeQueJeLeSuis);
        });
    }

    public void mettreAJourMonProfilAbonnements(int nbAbonnements) {
        Platform.runLater(() -> {
            this.pageDeMonProfil.mettreAJourMonProfilAbonnements(nbAbonnements);
        });
    }

    public void afficherMessagePrive(List<caches.Message> messages, String personneConcerne) {
        Platform.runLater(() -> {
            this.pageDeMessage.afficherMessagePrive(messages, personneConcerne);
        });
    }

    public void ajouterMessage(caches.Message message) {
        Platform.runLater(() -> {
            this.pageDeMessage.ajouterMessage(message);
        });
    }

    public void serverEtteind() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Le serveur vient de s'arrêter");
            alert.setContentText("Voulez-vous vous tenter une reconnexion ?");
            alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {
                this.reconnexion();
            } else {
                this.client.closeSocket();
                System.exit(0);
            }
        });
    }

    public Message getPageDeMessage() {
        return this.pageDeMessage;
    }

    public void removePublications(String pseudo) {
        Platform.runLater(() -> {
            this.pageDAccueil.removePublications(pseudo);
        });
    }

    public void ajouterNouvellesPublications(List<Publication> publications) {
        Platform.runLater(() -> {
            this.pageDAccueil.ajouterPublications(publications);
        });
    }

    public void reconnexion() {
        this.client = new Client(this, this.monCompte.getPseudo(), this.client.getIp(), true);
        if (this.client.isConnected()) {
            this.client.start();
        } else {
            this.serverEtteind();
        }
    }

    public void removePublication(int idPublication) {
        Platform.runLater(() -> {
            this.pageDAccueil.removePublication(idPublication);
        });
    }

    public void mettreAJourLeLike(int idPublication, int nbLike, boolean jaiLike, boolean hasLike) {
        Platform.runLater(() -> {
            this.pageDAccueil.mettreAJourLeLike(idPublication, nbLike, jaiLike, hasLike);
        });
    }

    public void setDisplayPublications(List<Publication> publications) {
        Platform.runLater(() -> {
            this.pageDAccueil.setDisplayPublications(publications);
        });
    }

    public void mettreAJourProfilAbonnements(String pseudo, int nbAbonnements) {
        Platform.runLater(() -> {
            this.pageDeProfil.mettreAJourProfilAbonnements(pseudo, nbAbonnements);
        });
    }

    public void mettreAJourProfilAbonnes(String pseudo, int nbAbonnes) {
        Platform.runLater(() -> {
            this.pageDeProfil.mettreAJourAbonnes(pseudo, nbAbonnes);
        });
    }

    public void mettreAJourMonProfilAbonnes(String pseudo2, int nbAbonnes) {
        Platform.runLater(() -> {
            this.pageDeMonProfil.mettreAJourMonProfilAbonnes(pseudo2, nbAbonnes);
        });
    }

    public void setMonCompte(Compte compte) {
        this.monCompte = compte;
    }

    public Compte getMonCompte() {
        return this.monCompte;
    }

    public byte[] imageToByteArray(Image image) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                InputStream is = this.getImageInputStream(image)) {

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = is.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }

            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private InputStream getImageInputStream(Image image) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", out);
        return new ByteArrayInputStream(out.toByteArray());
    }

    public boolean clientEstSurLaPageDesMessages() {
        return this.clientEstSurLaPageDesMessages;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
