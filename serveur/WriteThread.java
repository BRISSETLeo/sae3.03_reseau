package serveur;

import java.io.IOException;
import java.util.Scanner;

import requete.RequeteSocket;

public class WriteThread extends Thread {

    private final Scanner scanner;
    private final Serveur serveur;

    public WriteThread(Serveur serveur) {
        this.scanner = new Scanner(System.in);
        this.serveur = serveur;
    }

    @Override
    public void run() {

        while (true) {

            String demande = scanner.nextLine();

            if (!demande.contains(" ") || demande.split(" ").length < 2) {
                System.out.println("Veuillez notez que la demande doit être sous la forme /commande argument");
                continue;
            }

            if (demande.startsWith("/remove") || demande.startsWith("/delete")) {

                String id = this.recupererArgument(demande);

                if (id == null)
                    return;

                if (demande.startsWith("/delete")) {
                    try {
                        int idInt = Integer.parseInt(id);
                        this.serveur.getSQL().supprimerPublication(idInt);
                        System.out.println("La publication " + id + " a été supprimé");
                        for (ServeurThread clients : this.serveur.getClients()) {
                            clients.getOut().writeUTF(RequeteSocket.SUPPRIMER_PUBLICATION.getRequete());
                            clients.getOut().writeInt(idInt);
                            clients.getOut().flush();
                        }
                    } catch (NumberFormatException ignored) {
                        System.out.println("Veuillez notez que l'id doit être un entier (int)");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (this.serveur.getSQL().supprimerCompte(id)) {
                        for (ServeurThread clients : this.serveur.getClients()) {
                            if (clients.getPseudo().equals(id)) {
                                try {
                                    clients.getOut().writeUTF(RequeteSocket.DECONNEXION.getRequete());
                                    clients.getOut().flush();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                this.serveur.removeClient(clients);
                            }
                        }
                        System.out.println("Le compte " + id + " a été supprimé");
                    } else {
                        System.out.println("Une erreur est survenu et le compte " + id + " n'a pas pu être supprimé");
                    }
                }

            }

        }

    }

    private String recupererArgument(String demande) {
        return demande.split(" ")[1];
    }

}