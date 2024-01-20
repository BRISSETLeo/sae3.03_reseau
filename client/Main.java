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

    /**
    * Initialise l'application en créant les composants nécessaires et en définissant les écouteurs.
    * Cette méthode est appelée lors de l'initialisation de l'application.
    *
    * @throws Exception En cas d'erreur pendant l'initialisation.
    */
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
    /**
    * Démarre l'application en configurant la fenêtre principale et en affichant la page de connexion.
    *
    * @param stage La fenêtre principale de l'application.
    * @throws Exception En cas d'erreur lors du démarrage de l'application.
    */
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

    /**
    * Tente de connecter l'utilisateur en créant un objet Client et démarrant le processus de connexion.
    *
    * @param pseudo Le pseudo de l'utilisateur.
    * @param ip L'adresse IP du serveur.
    * @return Vrai si la connexion a réussi, sinon faux.
    */
    public boolean seConnecter(String pseudo, String ip) {
        this.client = new Client(this, pseudo, ip, false);
        boolean isConnected = this.client.isConnected();
        if (isConnected) {
            this.client.start();
        }
        return isConnected;
    }

    /**
    * Affiche une boîte de dialogue pour informer l'utilisateur qu'il n'a pas de compte
    * et lui propose de s'inscrire.
    * En cas de réponse positive, envoie un message pour accepter la création du compte.
    * En cas de réponse négative, ferme la connexion.
    */
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

    /**
    * Affiche une boîte de dialogue pour informer l'utilisateur qu'il va être déconnecté
    * et lui propose de confirmer cette action.
    * En cas de confirmation, quitte l'application.
    */
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

    /**
    * Supprime un message dans l'interface utilisateur en fonction de son ID.
    *
    * @param idMessage L'ID du message à supprimer.
    */
    public void supprimerMessage(int idMessage) {
        Platform.runLater(() -> this.pageDeMessage.supprimerMessage(idMessage));
    }

    /**
    * Affiche une boîte de dialogue pour informer l'utilisateur que son compte a été supprimé
    * de la base de données et qu'il va être déconnecté.
    * En cas de confirmation, quitte l'application.
    */
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

    /**
    * Affiche la page de messages dans l'interface utilisateur.
    */
    public void afficherPageMessage() {
        this.root.setCenter(this.pageDeMessage);
    }

    /**
    * Initialise l'interface utilisateur en plaçant les composants dans la disposition souhaitée.
    */
    public void initialiser() {
        Platform.runLater(() -> {
            this.pageDeNavigation.init();
            this.pageDeMonProfil.init();
            this.root.setLeft(this.pageDeNavigation);
            this.root.setCenter(this.pageDAccueil);
        });
    }

    /**
    * Affiche la page d'accueil dans l'interface utilisateur.
    */
    public void afficherPageDAccueil() {
        this.root.setCenter(this.pageDAccueil);
    }

    /**
    * Affiche la page de nouvelle publication dans l'interface utilisateur.
    */
    public void afficherPageDeNewPublication() {
        this.root.setCenter(this.pageDeNewPublication);
    }

    /**
    * Récupère l'objet client associé à l'interface utilisateur.
    *
    * @return L'objet client.
    */
    public Client getClient() {
        return this.client;
    }

    /**
    * Affiche la page de profil dans l'interface utilisateur.
    *
    * @param compte Le compte pour lequel afficher la page de profil.
    */
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

    /**
    * Affiche la page de profil de l'utilisateur actuel dans l'interface utilisateur.
    */
    public void afficherPageDeMonProfil() {
        this.root.setCenter(this.pageDeMonProfil);
    }

    /**
    * Met à jour la photo de profil dans l'interface utilisateur.
    */
    public void mettreAJourPhoto() {
        Platform.runLater(() -> {
            this.pageDeNavigation.changerImage();
            this.pageDAccueil.getPageDeRecherche().changerImage(this.monCompte);
        });
    }

    /**
    * Affiche les résultats d'une recherche dans l'interface utilisateur.
    *
    * @param comptes La liste des comptes résultat de la recherche.
    */
    public void resultatDeLaRecherche(List<Compte> comptes) {
        Platform.runLater(() -> {
            this.pageDAccueil.getPageDeRecherche().afficherResultatDeLaRecherche(comptes);
        });
    }

    /**
    * Met à jour le profil dans l'interface utilisateur.
    *
    * @param compte Le compte à mettre à jour.
    */
    public void miseAJourProfil(Compte compte) {
        Platform.runLater(() -> {
            this.pageDAccueil.mettreAJourProfilPublication(compte);
            this.pageDAccueil.getPageDeRecherche().changerImage(compte);
        });
    }

    /**
    * Affiche l'historique des messages dans l'interface utilisateur.
    *
    * @param messages La liste des messages à afficher dans l'historique.
    */
    public void afficherHistorique(List<caches.Message> messages) {
        Platform.runLater(() -> {
            this.pageDeMessage.afficherHistorique(messages);
        });
    }

    /**
    * Met à jour les informations de suivi du profil dans l'interface utilisateur.
    *
    * @param nbAbonnes Le nombre d'abonnés du profil.
    * @param estCeQueJeLeSuis Indique si l'utilisateur actuel suit le profil.
    */
    public void mettreAJourLeSuiviProfil(int nbAbonnes, boolean estCeQueJeLeSuis) {
        Platform.runLater(() -> {
            this.pageDeProfil.mettreAJourLeSuiviProfil(nbAbonnes, estCeQueJeLeSuis);
        });
    }

    /**
    * Met à jour le nombre d'abonnements dans l'interface utilisateur pour le profil de l'utilisateur actuel.
    *
    * @param nbAbonnements Le nombre d'abonnements du profil de l'utilisateur actuel.
    */
    public void mettreAJourMonProfilAbonnements(int nbAbonnements) {
        Platform.runLater(() -> {
            this.pageDeMonProfil.mettreAJourMonProfilAbonnements(nbAbonnements);
        });
    }

    /**
    * Affiche des messages privés dans l'interface utilisateur.
    *
    * @param messages La liste des messages privés à afficher.
    * @param personneConcerne La personne concernée par les messages privés.
    */
    public void afficherMessagePrive(List<caches.Message> messages, String personneConcerne) {
        Platform.runLater(() -> {
            this.pageDeMessage.afficherMessagePrive(messages, personneConcerne);
        });
    }

    /**
    * Ajoute un message à l'interface utilisateur.
    *
    * @param message Le message à ajouter.
    */
    public void ajouterMessage(caches.Message message) {
        Platform.runLater(() -> {
            this.pageDeMessage.ajouterMessage(message);
        });
    }

    /**
    * Gère le cas où le serveur s'arrête en affichant une boîte de dialogue avec des options.
    * Permet à l'utilisateur de tenter une reconnexion ou de quitter l'application.
    */
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

    /**
    * Récupère l'objet représentant la page de messages.
    *
    * @return L'objet représentant la page de messages.
    */    
    public Message getPageDeMessage() {
        return this.pageDeMessage;
    }

    /**
    * Supprime les publications associées à un utilisateur de l'interface utilisateur.
    *
    * @param pseudo Le pseudo de l'utilisateur dont les publications doivent être supprimées.
    */    
    public void removePublications(String pseudo) {
        Platform.runLater(() -> {
            this.pageDAccueil.removePublications(pseudo);
        });
    }

    /**
    * Ajoute de nouvelles publications à l'interface utilisateur.
    *
    * @param publications La liste des nouvelles publications à ajouter.
    */
    public void ajouterNouvellesPublications(List<Publication> publications) {
        Platform.runLater(() -> {
            this.pageDAccueil.ajouterPublications(publications);
        });
    }

    /**
    * Effectue une tentative de reconnexion au serveur.
    */
    public void reconnexion() {
        this.client = new Client(this, this.monCompte.getPseudo(), this.client.getIp(), true);
        if (this.client.isConnected()) {
            this.client.start();
        } else {
            this.serverEtteind();
        }
    }

    /**
    * Supprime une publication de l'interface utilisateur.
    *
    * @param idPublication L'ID de la publication à supprimer.
    */
    public void removePublication(int idPublication) {
        Platform.runLater(() -> {
            this.pageDAccueil.removePublication(idPublication);
        });
    }

    /**
    * Met à jour les informations de like d'une publication dans l'interface utilisateur.
    *
    * @param idPublication L'ID de la publication à mettre à jour.
    * @param nbLike Le nombre total de likes.
    * @param jaiLike Indique si l'utilisateur actuel a aimé la publication.
    * @param hasLike Indique si la publication a des likes.
    */
    public void mettreAJourLeLike(int idPublication, int nbLike, boolean jaiLike, boolean hasLike) {
        Platform.runLater(() -> {
            this.pageDAccueil.mettreAJourLeLike(idPublication, nbLike, jaiLike, hasLike);
        });
    }

    /**
    * Définit l'affichage des publications dans l'interface utilisateur.
    *
    * @param publications La liste des publications à afficher.
    */
    public void setDisplayPublications(List<Publication> publications) {
        Platform.runLater(() -> {
            this.pageDAccueil.setDisplayPublications(publications);
        });
    }

    /**
    * Met à jour les informations d'abonnements dans l'interface utilisateur pour un profil spécifié.
    *
    * @param pseudo Le pseudo du profil à mettre à jour.
    * @param nbAbonnements Le nombre d'abonnements du profil.
    */
    public void mettreAJourProfilAbonnements(String pseudo, int nbAbonnements) {
        Platform.runLater(() -> {
            this.pageDeProfil.mettreAJourProfilAbonnements(pseudo, nbAbonnements);
        });
    }

    /**
    * Met à jour les informations d'abonnés dans l'interface utilisateur pour un profil spécifié.
    *
    * @param pseudo Le pseudo du profil à mettre à jour.
    * @param nbAbonnes Le nombre d'abonnés du profil.
    */
    public void mettreAJourProfilAbonnes(String pseudo, int nbAbonnes) {
        Platform.runLater(() -> {
            this.pageDeProfil.mettreAJourAbonnes(pseudo, nbAbonnes);
        });
    }

    /**
    * Met à jour les informations d'abonnés dans l'interface utilisateur pour le profil de l'utilisateur actuel.
    *
    * @param pseudo2 Le pseudo du profil de l'utilisateur actuel.
    * @param nbAbonnes Le nombre d'abonnés du profil de l'utilisateur actuel.
    */
    public void mettreAJourMonProfilAbonnes(String pseudo2, int nbAbonnes) {
        Platform.runLater(() -> {
            this.pageDeMonProfil.mettreAJourMonProfilAbonnes(pseudo2, nbAbonnes);
        });
    }
    /**
    * Définit le compte de l'utilisateur.
    *
    * @param compte Le compte à définir.
    */
    public void setMonCompte(Compte compte) {
        this.monCompte = compte;
    }

    /**
    * Récupère le compte de l'utilisateur.
    *
    * @return Le compte de l'utilisateur.
    */
    public Compte getMonCompte() {
        return this.monCompte;
    }

    /**
    * Convertit une image en tableau d'octets.
    *
    * @param image L'image à convertir.
    * @return Le tableau d'octets représentant l'image.
    */
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

    /**
    * Obtient un flux d'entrée pour une image.
    *
    * @param image L'image à traiter.
    * @return Le flux d'entrée pour l'image.
    * @throws IOException En cas d'erreur d'entrée/sortie.
    */
    private InputStream getImageInputStream(Image image) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", out);
        return new ByteArrayInputStream(out.toByteArray());
    }

    /**
    * Vérifie si le client est actuellement sur la page des messages.
    *
    * @return true si le client est sur la page des messages, sinon false.
    */
    public boolean clientEstSurLaPageDesMessages() {
        return this.clientEstSurLaPageDesMessages;
    }

    /**
    * Point d'entrée de l'application JavaFX.
    *
    * @param args Les arguments de la ligne de commande.
    */
    public static void main(String[] args) {
        launch(args);
    }

}
