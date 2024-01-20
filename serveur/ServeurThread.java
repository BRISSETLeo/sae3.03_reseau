package serveur;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import caches.ByteManager;
import caches.Compte;
import caches.Message;
import caches.Publication;
import requete.RequeteSocket;

public class ServeurThread extends Thread {

    private final String pseudo;

    private final Serveur serveur;
    private final Socket socket;
    private final DataOutputStream out;
    private final DataInputStream in;

    private final boolean reconnexion;

    /**
    * Constructeur de la classe ServeurThread.
    *
    * @param serveur  L'instance du serveur auquel le thread est associé.
    * @param socket   La socket associée à la connexion avec le client.
    * @throws IOException En cas d'erreur d'entrée/sortie lors de l'initialisation.
    */
    public ServeurThread(Serveur serveur, Socket socket) throws IOException {
        this.serveur = serveur;
        this.socket = socket;
        this.out = new DataOutputStream(socket.getOutputStream());
        this.in = new DataInputStream(socket.getInputStream());
        this.pseudo = this.in.readUTF();
        this.reconnexion = this.in.readBoolean();
        this.init(this.serveur.getSQL().hasCompte(this.pseudo));
    }

    @Override
    public void run() {

        System.out.println("Un client s'est connecté.");

        try {

            String demande;

            while (true) {

                demande = this.in.readUTF();

                if (demande.equals(RequeteSocket.ACCEPTER_CREER_COMPTE.getRequete())) {
                    this.serveur.getSQL().sauvegarderCompte(this.pseudo);
                    this.init(true);
                } else if (demande.equals(RequeteSocket.SAUVEGARDER_PROFIL.getRequete())) {
                    int taille = this.in.readInt();
                    byte[] infos = new byte[taille];
                    this.in.readFully(infos, 0, taille);
                    this.sauvegarderProfil(ByteManager.fromBytes(infos, Compte.class));
                } else if (demande.equals(RequeteSocket.RECHERCHER_COMPTES.getRequete())) {
                    this.rechercherComptes(this.in.readUTF());
                } else if (demande.equals(RequeteSocket.DEMANDER_PROFIL.getRequete())) {
                    this.demanderProfil(this.in.readUTF());
                } else if (demande.equals(RequeteSocket.DEMANDER_SUIVRE.getRequete())) {
                    this.demanderSuivre(this.in.readUTF());
                } else if (demande.equals(RequeteSocket.DEMANDER_PLUS_SUIVRE.getRequete())) {
                    this.demanderPlusSuivre(this.in.readUTF());
                } else if (demande.equals(RequeteSocket.LIKER_PUBLICATION.getRequete())) {
                    this.likerPublication(this.in.readInt());
                } else if (demande.equals(RequeteSocket.DISLIKER_PUBLICATION.getRequete())) {
                    this.dislikerPublication(this.in.readInt());
                } else if (demande.equals(RequeteSocket.SUPPRIMER_PUBLICATION.getRequete())) {
                    this.supprimerPublication(this.in.readInt());
                } else if (demande.equals(RequeteSocket.PUBLIER_PUBLICATION.getRequete())) {
                    String contenuPublication = this.in.readUTF();
                    int taille = this.in.readInt();
                    byte[] publicationBytes = new byte[taille];
                    this.in.readFully(publicationBytes, 0, taille);
                    this.publierPublication(contenuPublication, publicationBytes);
                } else if (demande.equals(RequeteSocket.AVOIR_DERNIER_MESSAGE.getRequete())) {
                    this.avoirDernierMessage(this.in.readUTF());
                } else if (demande.equals(RequeteSocket.DEMANDE_MESSAGE.getRequete())) {
                    this.demandeMessage(this.in.readUTF());
                } else if (demande.equals(RequeteSocket.ENVOYER_MESSAGE.getRequete())) {
                    String pseudo = this.in.readUTF();
                    String contenu = this.in.readUTF();
                    int taille = this.in.readInt();
                    byte[] image = new byte[taille];
                    this.in.readFully(image, 0, taille);
                    this.envoyerMessage(pseudo, contenu, image);
                } else if (demande.equals(RequeteSocket.ENVOYER_VOCAL.getRequete())) {
                    String pseudo = this.in.readUTF();
                    int taille = this.in.readInt();
                    byte[] vocal = new byte[taille];
                    this.in.readFully(vocal, 0, taille);
                    this.envoyerVocal(pseudo, vocal);
                } else if (demande.equals(RequeteSocket.SUPPRIMER_MESSAGE.getRequete())) {
                    this.supprimerMessage(this.in.readInt());
                }

            }

        } catch (IOException | ClassNotFoundException e) {
            try {
                this.socket.close();
                this.out.close();
                this.in.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        this.serveur.removeClient(this);
        System.out.println("Un client s'est déconnecté.");

    }

    /**
    * Initialise le processus de connexion, en informant le client de la création ou de la demande de création d'un compte.
    * Envoie également les informations du compte et les publications associées.
    *
    * @param hasCompte Indique si le client a un compte existant.
    * @throws IOException En cas d'erreur d'entrée/sortie lors de l'initialisation.
    */
    private void init(boolean hasCompte) throws IOException {

        out.writeUTF((hasCompte) ? RequeteSocket.COMPTE_CREE.getRequete()
                : RequeteSocket.DEMANDER_CREER_COMPTE.getRequete());
        if (hasCompte) {
            byte[] compteBytes = ByteManager.toBytes(this.serveur.getSQL().getCompte(this.pseudo, this.pseudo));
            out.writeInt(compteBytes.length);
            out.write(compteBytes);
            if (!this.reconnexion) {
                byte[] publicationsBytes = ByteManager
                        .convertListToBytes(this.serveur.getSQL().getPublicationsCanDisplay(this.pseudo));
                out.writeInt(publicationsBytes.length);
                out.write(publicationsBytes);
            }
        }
        out.flush();

    }

    /**
    * Sauvegarde le profil du compte spécifié, en informant le serveur SQL de la modification et en envoyant
    * une mise à jour aux autres clients.
    *
    * @param compte Le compte dont le profil doit être sauvegardé.
    * @throws IOException En cas d'erreur d'entrée/sortie lors de la sauvegarde du profil.
    */
    private void sauvegarderProfil(Compte compte) throws IOException {
        this.serveur.getSQL().modifierCompte(compte);
        this.out.writeUTF(RequeteSocket.SAUVEGARDER_PROFIL.getRequete());
        this.out.flush();
        byte[] compteBytes = ByteManager.toBytes(compte);
        for (ServeurThread client : this.serveur.getClients()) {
            if (client != this && client.isAlive()) {
                client.getOut().writeUTF(RequeteSocket.MISE_A_JOUR_PROFIL.getRequete());
                client.getOut().writeInt(compteBytes.length);
                client.getOut().write(compteBytes);
                client.getOut().flush();
            }
        }
    }

    /**
    * Vérifie si le client est connecté au serveur.
    *
    * @return true si le client est connecté, sinon false.
    */
    public boolean isConnected() {
        return this.socket != null && this.socket.isConnected();
    }
    /**
    * Traite la demande de recherche de comptes en fonction d'un critère de recherche.
    *
    * @param recherche La chaîne de recherche.
    * @throws IOException En cas d'erreur d'entrée/sortie lors du traitement de la recherche de comptes.
    */
    private void rechercherComptes(String recherche) throws IOException {

        this.out.writeUTF(RequeteSocket.RECHERCHER_COMPTES.getRequete());
        byte[] comptesBytes = ByteManager
                .convertListToBytes(this.serveur.getSQL().rechercherComptes(this.pseudo, recherche));
        this.out.writeInt(comptesBytes.length);
        this.out.write(comptesBytes);
        this.out.flush();
    }
    /**
    * Traite la demande du profil d'un utilisateur spécifié par son pseudo.
    *
    * @param pseudo Le pseudo de l'utilisateur dont le profil est demandé.
    * @throws IOException En cas d'erreur d'entrée/sortie lors du traitement de la demande de profil.
    */
    private void demanderProfil(String pseudo) throws IOException {
        this.out.writeUTF(RequeteSocket.DEMANDER_PROFIL.getRequete());
        byte[] compteBytes = ByteManager.toBytes(this.serveur.getSQL().getCompte(this.pseudo, pseudo));
        this.out.writeInt(compteBytes.length);
        this.out.write(compteBytes);
        this.out.flush();
    }

    /**
     * Traite la demande de suivre un utilisateur spécifié par son pseudo.
     *
     * @param pseudo Le pseudo de l'utilisateur à suivre.
     * @throws IOException En cas d'erreur d'entrée/sortie lors du traitement de la demande de suivi.
    */
    private void demanderSuivre(String pseudo) throws IOException {
        this.serveur.getSQL().demanderSuivre(this.pseudo, pseudo);
        this.demandeSuivrePlusSuivre(RequeteSocket.DEMANDER_SUIVRE, pseudo);
    }

    /**
    * Traite la demande de suivre ou de ne plus suivre un utilisateur.
    *
    * @param requeteSocket La requête associée à l'action (DEMANDER_SUIVRE ou DEMANDER_PLUS_SUIVRE).
    * @param pseudo        Le pseudo de l'utilisateur cible.
    * @throws IOException En cas d'erreur d'entrée/sortie lors du traitement de la demande.
    */
    private void demanderPlusSuivre(String pseudo) throws IOException {
        this.serveur.getSQL().demanderPlusSuivre(this.pseudo, pseudo);
        this.demandeSuivrePlusSuivre(RequeteSocket.DEMANDER_PLUS_SUIVRE, pseudo);
    }

    private void demandeSuivrePlusSuivre(RequeteSocket requeteSocket, String pseudo) throws IOException {
        this.out.writeUTF(requeteSocket.getRequete());
        int nbAbonnes = this.serveur.getSQL().getNbFollowers(pseudo);
        int nbAbonnements = this.serveur.getSQL().getNbFollowings(this.pseudo);
        this.out.writeInt(nbAbonnes);
        this.out.writeBoolean(this.serveur.getSQL().estCeQueJeSuisAbonne(this.pseudo, pseudo));
        this.out.writeInt(nbAbonnements);
        if (requeteSocket.equals(RequeteSocket.DEMANDER_SUIVRE)) {
            byte[] publicationByte = ByteManager
                    .convertListToBytes(this.serveur.getSQL().getPublications(pseudo));
            this.out.writeInt(publicationByte.length);
            this.out.write(publicationByte);
        } else {
            this.out.writeUTF(pseudo);
        }
        this.out.flush();
        for (ServeurThread client : this.serveur.getClients()) {
            if (!client.getPseudo().equals(this.pseudo)) {
                client.getOut().writeUTF(RequeteSocket.METTRE_A_JOUR_SUIVI.getRequete());
                client.getOut().writeUTF(this.pseudo);
                client.getOut().writeUTF(pseudo);
                client.getOut().writeInt(nbAbonnes);
                client.getOut().writeInt(nbAbonnements);

                client.getOut().flush();
            }
        }
    }

    /**
    * Traite l'action de liker une publication spécifiée par son identifiant.
    *
    * @param idPublication L'identifiant de la publication à liker.
    * @throws IOException En cas d'erreur d'entrée/sortie lors du traitement de l'action de like.
    */
    private void likerPublication(int idPublication) throws IOException {
        String senderPublication = this.serveur.getSQL().likerPublication(this.pseudo, idPublication);
        int nbLike = this.serveur.getSQL().getNbLike(idPublication);
        for (ServeurThread client : this.serveur.getClients()) {
            if (client.getPseudo().equals(senderPublication)
                    || this.serveur.getSQL().hasFollower(senderPublication, client.getPseudo())) {
                client.getOut().writeUTF(RequeteSocket.LIKER_PUBLICATION.getRequete());
                client.getOut().writeInt(idPublication);
                client.getOut().writeInt(nbLike);
                client.getOut().writeBoolean(client.getPseudo().equals(this.pseudo));
                client.getOut().writeBoolean(this.serveur.getSQL().estCeQueJaiLike(client.getPseudo(), idPublication));
                client.getOut().flush();
            }
        }
    }

    /**
    * Dislike une publication spécifiée par son identifiant.
    *
    * @param idPublication L'identifiant de la publication à disliker.
    * @throws IOException En cas d'erreur d'entrée/sortie lors du dislike de la publication.
    */
    private void dislikerPublication(int idPublication) throws IOException {
        String senderPublication = this.serveur.getSQL().dislikerPublication(this.pseudo, idPublication);
        int nbLike = this.serveur.getSQL().getNbLike(idPublication);
        for (ServeurThread client : this.serveur.getClients()) {
            if (client.getPseudo().equals(senderPublication)
                    || this.serveur.getSQL().hasFollower(senderPublication, client.getPseudo())) {
                client.getOut().writeUTF(RequeteSocket.LIKER_PUBLICATION.getRequete());
                client.getOut().writeInt(idPublication);
                client.getOut().writeInt(nbLike);
                client.getOut().writeBoolean(client.getPseudo().equals(this.pseudo));
                client.getOut().writeBoolean(this.serveur.getSQL().estCeQueJaiLike(client.getPseudo(), idPublication));
                client.getOut().flush();
            }
        }
    }

    /**
    * Supprime une publication spécifiée par son identifiant.
    *
    * @param idPublication L'identifiant de la publication à supprimer.
    */
    private void supprimerPublication(int idPublication) {
        this.serveur.getSQL().supprimerPublication(idPublication);
        for (ServeurThread client : this.serveur.getClients()) {
            if (client.getPseudo().equals(this.pseudo)
                    || this.serveur.getSQL().hasFollower(this.pseudo, client.getPseudo())) {
                try {
                    client.getOut().writeUTF(RequeteSocket.SUPPRIMER_PUBLICATION.getRequete());
                    client.getOut().writeInt(idPublication);
                    client.getOut().flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
    * Publie une nouvelle publication avec un contenu et des images spécifiés.
    *
    * @param contenuPublication Le contenu de la publication.
    * @param imagesPublication  Les données des images de la publication.
    * @throws IOException En cas d'erreur d'entrée/sortie lors de la publication de la nouvelle publication.
    */
    public void publierPublication(String contenuPublication, byte[] imagesPublication) throws IOException {
        if (imagesPublication.length == 0) {
            imagesPublication = null;
        }
        Publication publication = this.serveur.getSQL().publierPublication(this.pseudo, contenuPublication,
                imagesPublication);
        if (publication != null) {
            byte[] publicationBytes = ByteManager.toBytes(publication);
            for (ServeurThread client : this.serveur.getClients()) {
                if (client.getPseudo().equals(this.pseudo)
                        || this.serveur.getSQL().hasFollower(this.pseudo, client.getPseudo())) {
                    try {
                        client.getOut().writeUTF(RequeteSocket.PUBLIER_PUBLICATION.getRequete());
                        client.getOut().writeInt(publicationBytes.length);
                        client.getOut().write(publicationBytes);
                        client.getOut().flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
    * Supprime un message spécifié par son identifiant.
    *
    * @param idMessage L'identifiant du message à supprimer.
    * @throws IOException En cas d'erreur d'entrée/sortie lors de la suppression du message.
    */
    public void supprimerMessage(int idMessage) throws IOException {
        Message message = this.serveur.getSQL().getMessageById(idMessage);
        this.serveur.getSQL().supprimerMessage(idMessage);
        if (message != null) {
            for (ServeurThread client : this.serveur.getClients()) {
                if (client.getPseudo().equals(message.getCompte().getPseudo()) ||
                        client.getPseudo().equals(message.getCompteDestinateur().getPseudo())) {
                    try {
                        client.getOut().writeUTF(RequeteSocket.SUPPRIMER_MESSAGE.getRequete());
                        client.getOut().writeInt(idMessage);
                        client.getOut().flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
    * Récupère les derniers messages entre l'utilisateur actuel et un autre utilisateur spécifié.
    *
    * @param pseudo Le pseudo de l'autre utilisateur.
    */
    public void avoirDernierMessage(String pseudo) {
        try {
            if (!pseudo.equals("")) {
                this.serveur.getSQL().setAllMessagesNonLuEnLu(this.pseudo, pseudo);
            }
            this.out.writeUTF(RequeteSocket.AVOIR_DERNIER_MESSAGE.getRequete());
            byte[] messageBytes = ByteManager
                    .convertListToBytes(this.serveur.getSQL().getDerniersMessages(this.pseudo));
            this.out.writeInt(messageBytes.length);
            this.out.write(messageBytes);
            this.out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
    * Demande les messages entre l'utilisateur actuel et un autre utilisateur spécifié.
    *
    * @param pseudo Le pseudo de l'autre utilisateur.
    */
    public void demandeMessage(String pseudo) {
        try {
            this.out.writeUTF(RequeteSocket.DEMANDE_MESSAGE.getRequete());
            byte[] messageBytes = ByteManager
                    .convertListToBytes(this.serveur.getSQL().getMessages(this.pseudo, pseudo));
            this.out.writeInt(messageBytes.length);
            this.out.write(messageBytes);
            this.out.writeUTF(pseudo);
            this.out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
    * Envoie un message à un destinataire spécifié avec un contenu et une image.
    *
    * @param receiver Le pseudo du destinataire.
    * @param contenu  Le contenu du message.
    * @param image    Les données de l'image du message.
    * @throws IOException En cas d'erreur d'entrée/sortie lors de l'envoi du message.
    */
    public void envoyerMessage(String receiver, String contenu, byte[] image) throws IOException {
        Message message = this.serveur.getSQL().envoyerMessage(this.pseudo, receiver, contenu, image);
        byte[] messageBytes = ByteManager.toBytes(message);
        for (ServeurThread client : this.serveur.getClients()) {
            if (client.getPseudo().equals(this.pseudo) || client.getPseudo().equals(receiver)) {
                client.getOut().writeUTF(RequeteSocket.ENVOYER_MESSAGE.getRequete());
                client.getOut().writeInt(messageBytes.length);
                client.getOut().write(messageBytes);
                client.getOut().flush();
            }
        }
    }

    /**
    * Envoie un message vocal à un destinataire spécifié.
    *
    * @param receiver Le pseudo du destinataire.
    * @param vocal    Les données audio du message vocal.
    * @throws IOException En cas d'erreur d'entrée/sortie lors de l'envoi du message.
    */
    public void envoyerVocal(String receiver, byte[] vocal) throws IOException {
        Message message = this.serveur.getSQL().envoyerVocal(this.pseudo, receiver, vocal);
        byte[] messageBytes = ByteManager.toBytes(message);
        for (ServeurThread client : this.serveur.getClients()) {
            if (client.getPseudo().equals(this.pseudo) || client.getPseudo().equals(receiver)) {
                client.getOut().writeUTF(RequeteSocket.ENVOYER_VOCAL.getRequete());
                client.getOut().writeInt(messageBytes.length);
                client.getOut().write(messageBytes);
                client.getOut().flush();
            }
        }
    }

    /**
    * Récupère le flux de sortie associé à ce thread.
    *
    * @return Le flux de sortie associé à ce thread.
    */
    public synchronized DataOutputStream getOut() {
        return this.out;
    }

    /**
    * Récupère le pseudo associé à ce thread.
    *
    * @return Le pseudo associé à ce thread.
    */  
    public String getPseudo() {
        return this.pseudo;
    }

}
