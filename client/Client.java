package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

import caches.ByteManager;
import caches.Compte;
import caches.Publication;
import javafx.application.Platform;
import javafx.scene.image.Image;
import requete.RequeteSocket;

public class Client extends Thread {

    private final Main main;
    private final String ip;
    private final boolean reconnexion;

    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;

    /**
    * Initialise un nouveau client avec les paramètres spécifiés.
    *
    * @param main L'objet Main associé au client.
    * @param pseudo Le pseudo du client.
    * @param ip L'adresse IP du serveur auquel se connecter.
    * @param reconnexion Indicateur indiquant s'il s'agit d'une tentative de reconnexion.
    */
    public Client(Main main, String pseudo, String ip, boolean reconnexion) {

        this.main = main;
        this.ip = ip;
        this.reconnexion = reconnexion;

        try {

            this.socket = new Socket(ip, 25565);
            this.out = new DataOutputStream(socket.getOutputStream());
            this.in = new DataInputStream(socket.getInputStream());

            this.out.writeUTF(pseudo);
            this.out.writeBoolean(this.reconnexion);
            this.out.flush();

        } catch (IOException ignored) {
        }

    }

    @Override
    public void run() {

        String reponse;

        while (true) {

            try {

                reponse = this.in.readUTF();

                if (reponse.equals(RequeteSocket.COMPTE_CREE.getRequete())) {
                    int taille = this.in.readInt();
                    byte[] infos = new byte[taille];
                    this.in.readFully(infos, 0, taille);
                    this.main.setMonCompte(ByteManager.fromBytes(infos, Compte.class));
                    if (!this.reconnexion) {
                        taille = this.in.readInt();
                        infos = new byte[taille];
                        this.in.readFully(infos, 0, taille);
                        this.main.setDisplayPublications(ByteManager.convertBytesToList(infos, Publication.class));
                        this.main.initialiser();
                    }
                } else if (reponse.equals(RequeteSocket.DEMANDER_CREER_COMPTE.getRequete())) {
                    this.main.afficherLaPageCreerCompte();
                } else if (reponse.equals(RequeteSocket.SAUVEGARDER_PROFIL.getRequete())) {
                    this.main.mettreAJourPhoto();
                    this.main.miseAJourProfil(this.main.getMonCompte());
                } else if (reponse.equals(RequeteSocket.RECHERCHER_COMPTES.getRequete())) {
                    int taille = this.in.readInt();
                    byte[] infos = new byte[taille];
                    this.in.readFully(infos, 0, taille);
                    this.main.resultatDeLaRecherche(ByteManager.convertBytesToList(infos, Compte.class));
                } else if (reponse.equals(RequeteSocket.MISE_A_JOUR_PROFIL.getRequete())) {
                    int taille = this.in.readInt();
                    byte[] infos = new byte[taille];
                    this.in.readFully(infos, 0, taille);
                    this.main.miseAJourProfil(ByteManager.fromBytes(infos, Compte.class));
                } else if (reponse.equals(RequeteSocket.DEMANDER_PROFIL.getRequete())) {
                    int taille = this.in.readInt();
                    byte[] infos = new byte[taille];
                    this.in.readFully(infos, 0, taille);
                    this.main.afficherLaPageDeProfil(ByteManager.fromBytes(infos, Compte.class));
                } else if (reponse.equals(RequeteSocket.DEMANDER_SUIVRE.getRequete())
                        || reponse.equals(RequeteSocket.DEMANDER_PLUS_SUIVRE.getRequete())) {
                    int nbAbonnes = this.in.readInt();
                    boolean estCeQueJeLeSuis = this.in.readBoolean();
                    this.main.mettreAJourMonProfilAbonnements(this.in.readInt());
                    this.main.mettreAJourLeSuiviProfil(nbAbonnes, estCeQueJeLeSuis);

                    if (reponse.equals(RequeteSocket.DEMANDER_SUIVRE.getRequete())) {
                        int taille = this.in.readInt();
                        byte[] infos = new byte[taille];
                        this.in.readFully(infos, 0, taille);

                        List<Publication> publications = ByteManager.convertBytesToList(infos, Publication.class);
                        this.main.ajouterNouvellesPublications(publications);
                    } else {
                        this.main.removePublications(this.in.readUTF());
                    }

                } else if (reponse.equals(RequeteSocket.METTRE_A_JOUR_SUIVI.getRequete())) {
                    String pseudo = this.in.readUTF();
                    String pseudo2 = this.in.readUTF();
                    int nbAbonnes = this.in.readInt();
                    int nbAbonnements = this.in.readInt();
                    this.main.mettreAJourProfilAbonnements(pseudo, nbAbonnements);
                    this.main.mettreAJourProfilAbonnes(pseudo2, nbAbonnes);
                    this.main.mettreAJourMonProfilAbonnes(pseudo2, nbAbonnes);
                } else if (reponse.equals(RequeteSocket.LIKER_PUBLICATION.getRequete())
                        || reponse.equals(RequeteSocket.DISLIKER_PUBLICATION.getRequete())) {
                    int idPublication = this.in.readInt();
                    int nbLike = this.in.readInt();
                    boolean jaiLike = this.in.readBoolean();
                    boolean hasLike = this.in.readBoolean();
                    this.main.mettreAJourLeLike(idPublication, nbLike, jaiLike, hasLike);
                } else if (reponse.equals(RequeteSocket.SUPPRIMER_PUBLICATION.getRequete())) {
                    this.main.removePublication(this.in.readInt());
                } else if (reponse.equals(RequeteSocket.PUBLIER_PUBLICATION.getRequete())) {
                    int taille = this.in.readInt();
                    byte[] infos = new byte[taille];
                    this.in.readFully(infos, 0, taille);
                    this.main.ajouterNouvellesPublications(
                            Arrays.asList(ByteManager.fromBytes(infos, Publication.class)));
                    Platform.runLater(() -> this.main.afficherPageDAccueil());
                } else if (reponse.equals(RequeteSocket.AVOIR_DERNIER_MESSAGE.getRequete())) {
                    int taille = this.in.readInt();
                    byte[] infos = new byte[taille];
                    this.in.readFully(infos, 0, taille);
                    Platform.runLater(() -> this.main.afficherPageMessage());
                    this.main.afficherHistorique(ByteManager.convertBytesToList(infos, caches.Message.class));
                } else if (reponse.equals(RequeteSocket.DEMANDE_MESSAGE.getRequete())) {
                    int taille = this.in.readInt();
                    byte[] infos = new byte[taille];
                    this.in.readFully(infos, 0, taille);
                    this.main.afficherMessagePrive(ByteManager.convertBytesToList(infos, caches.Message.class),
                            this.in.readUTF());
                } else if (reponse.equals(RequeteSocket.ENVOYER_MESSAGE.getRequete())
                        || reponse.equals(RequeteSocket.ENVOYER_VOCAL.getRequete())) {
                    this.main.ajouterMessage(
                            ByteManager.fromBytes(this.in.readNBytes(this.in.readInt()), caches.Message.class));
                } else if (reponse.equals(RequeteSocket.DECONNEXION.getRequete())) {
                    this.main.deconnexion();
                } else if (reponse.equals(RequeteSocket.SUPPRIMER_MESSAGE.getRequete())) {
                    this.main.supprimerMessage(this.in.readInt());
                }

            } catch (IOException e) {

                if (this.socket.isClosed())
                    break;

                if (e.getMessage() != null) {
                    if (e.getMessage().equals("Connection reset")) {
                        this.main.serverEtteind();
                        break;
                    } else {
                        System.out.println(e.getMessage());
                    }

                } else {
                    e.printStackTrace();
                    break;
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }

        this.closeSocket();

    }

    /**
    * Vérifie si la connexion socket est établie.
    *
    * @return true si la connexion socket est établie, sinon false.
    */
    public boolean isConnected() {
        return this.socket != null && this.socket.isConnected();
    }

    /**
    * Ferme la connexion socket, ainsi que les flux de sortie et d'entrée associés.
    */
    public void closeSocket() {
        try {
            this.socket.close();
            this.out.close();
            this.in.close();
        } catch (IOException ignored) {
        }
    }

    /**
    * Envoie une requête au serveur pour accepter de créer un compte.
    */
    public void accepterDeCreerCompte() {
        try {
            this.out.writeUTF(RequeteSocket.ACCEPTER_CREER_COMPTE.getRequete());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
    * Envoie une requête au serveur pour sauvegarder le profil de l'utilisateur actuel.
    */
    public void sauvegarderProfil() {
        try {
            this.out.writeUTF(RequeteSocket.SAUVEGARDER_PROFIL.getRequete());
            byte[] compteBytes = ByteManager.toBytes(this.main.getMonCompte());
            this.out.writeInt(compteBytes.length);
            this.out.write(compteBytes);
            this.out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
    * Envoie une requête au serveur pour rechercher des comptes d'utilisateurs en fonction d'un critère de recherche.
    *
    * @param recherche Le critère de recherche pour les comptes d'utilisateurs.
    */
    public void rechercherComptes(String recherche) {
        try {
            this.out.writeUTF(RequeteSocket.RECHERCHER_COMPTES.getRequete());
            this.out.writeUTF(recherche);
            this.out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
    * Envoie une requête au serveur pour demander le profil d'un utilisateur spécifié.
    *
    * @param pseudo Le pseudo de l'utilisateur dont on souhaite obtenir le profil.
    */
    public void demanderProfil(String pseudo) {
        try {
            this.out.writeUTF(RequeteSocket.DEMANDER_PROFIL.getRequete());
            this.out.writeUTF(pseudo);
            this.out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
    * Envoie une requête au serveur pour demander à suivre un utilisateur spécifié.
    *
    * @param pseudo Le pseudo de l'utilisateur que l'on souhaite suivre.
    */
    public void demanderSuivre(String pseudo) {
        try {
            this.out.writeUTF(RequeteSocket.DEMANDER_SUIVRE.getRequete());
            this.out.writeUTF(pseudo);
            this.out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
    * Envoie une requête au serveur pour demander de ne plus suivre un utilisateur spécifié.
    *
    * @param pseudo Le pseudo de l'utilisateur que l'on souhaite ne plus suivre.
    */
    public void demanderNePlusSuivre(String pseudo) {
        try {
            this.out.writeUTF(RequeteSocket.DEMANDER_PLUS_SUIVRE.getRequete());
            this.out.writeUTF(pseudo);
            this.out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
    * Envoie une requête au serveur pour indiquer que l'utilisateur aime une publication spécifiée.
    *
    * @param idPublication L'ID de la publication à liker.
    */
    public void likerPublication(int idPublication) {
        try {
            this.out.writeUTF(RequeteSocket.LIKER_PUBLICATION.getRequete());
            this.out.writeInt(idPublication);
            this.out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
    * Envoie une requête au serveur pour indiquer que l'utilisateur n'aime plus une publication spécifiée.
    *
    * @param idPublication L'ID de la publication à disliker.
    */
    public void dislikerPublication(int idPublication) {
        try {
            this.out.writeUTF(RequeteSocket.DISLIKER_PUBLICATION.getRequete());
            this.out.writeInt(idPublication);
            this.out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
    * Envoie une requête au serveur pour supprimer une publication spécifiée par son ID.
    *
    * @param idPublication L'ID de la publication à supprimer.
    */
    public void supprimerPublication(int idPublication) {

        try {
            this.out.writeUTF(RequeteSocket.SUPPRIMER_PUBLICATION.getRequete());
            this.out.writeInt(idPublication);
            this.out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
    * Envoie une requête au serveur pour publier une nouvelle publication avec du contenu et éventuellement une image.
    *
    * @param contenuPublication Le contenu textuel de la publication.
    * @param imagePublication L'image associée à la publication (peut être null s'il n'y a pas d'image).
    */
    public void publierPublication(String contenuPublication, Image imagePublication) {
        try {
            this.out.writeUTF(RequeteSocket.PUBLIER_PUBLICATION.getRequete());
            this.out.writeUTF(contenuPublication);
            if (imagePublication != null) {
                byte[] imageBytes = this.main.imageToByteArray(imagePublication);
                this.out.writeInt(imageBytes.length);
                this.out.write(imageBytes);
            } else {
                this.out.writeInt(0);
            }
            this.out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Envoie une requête au serveur pour afficher le dernier message d'un utilisateur spécifié.
     *
     * @param pseudo Le pseudo de l'utilisateur dont le dernier message doit être affiché (peut être null).
     */
    public synchronized void afficherMessage(String pseudo) {
        try {
            this.out.writeUTF(RequeteSocket.AVOIR_DERNIER_MESSAGE.getRequete());
            if (pseudo == null) {
                this.out.writeUTF("");
            } else {
                this.out.writeUTF(pseudo);
            }
            this.out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
    * Envoie une requête au serveur pour supprimer un message spécifié par son ID.
    *
    * @param idMessage L'ID du message à supprimer.
    */
    public void supprimerMessage(int idMessage) {
        try {
            this.out.writeUTF(RequeteSocket.SUPPRIMER_MESSAGE.getRequete());
            this.out.writeInt(idMessage);
            this.out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Demande au serveur l'envoi de messages pour un pseudo spécifié.
     *
     * @param pseudo Le pseudo du destinataire pour lequel les messages sont demandés.
    */
    public synchronized void demanderMessage(String pseudo) {
        try {
            this.out.writeUTF(RequeteSocket.DEMANDE_MESSAGE.getRequete());
            this.out.writeUTF(pseudo);
            this.out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
    * Renvoie l'adresse IP associée à cette connexion.
    */
    public String getIp() {
        return this.ip;
    }

    /**
     * Envoie un message à un destinataire spécifié avec le contenu textuel et éventuellement une image.
     *
     * @param receiver Le destinataire du message.
     * @param contenu Le contenu textuel du message.
     * @param image Un tableau d'octets représentant une image associée au message (peut être null s'il n'y a pas d'image).
     */
    public void envoyerMessage(String receiver, String contenu, byte[] image) {
        try {
            this.out.writeUTF(RequeteSocket.ENVOYER_MESSAGE.getRequete());
            this.out.writeUTF(receiver);
            this.out.writeUTF(contenu);
            if (image != null) {
                this.out.writeInt(image.length);
                this.out.write(image);
            } else {
                this.out.writeInt(0);
            }
            this.out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Envoie un message vocal à un destinataire spécifié.
     *
     * @param receiver Le destinataire du message vocal.
     * @param vocal Un tableau d'octets représentant les données vocales du message.
     */
    public void envoyerVocal(String receiver, byte[] vocal) {
        try {
            this.out.writeUTF(RequeteSocket.ENVOYER_VOCAL.getRequete());
            this.out.writeUTF(receiver);
            this.out.writeInt(vocal.length);
            this.out.write(vocal);
            this.out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
