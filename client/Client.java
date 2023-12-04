import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    
    public static void main(String[] args) {

        String pseudo = null;
        String motDePasse = null;

        while(pseudo == null){
            System.out.print("Entrer votre pseudo : ");
            pseudo = new Scanner(System.in).nextLine();
        }
        while(motDePasse == null){
            System.out.print("Entrer votre mot de passe : ");
            motDePasse = new Scanner(System.in).nextLine();
        }

        Socket socket = null;
        
        while(socket == null){
            try {
                socket = new Socket("localhost", 3030);
                Scanner scanner = new Scanner(System.in);
                DataOutputStream dataOutput = new DataOutputStream(socket.getOutputStream());
                dataOutput.writeUTF(pseudo);
                dataOutput.writeUTF(motDePasse);
                dataOutput.flush();
                while(true){
                    String message = scanner.nextLine();
                    System.out.println("Message envoyé : " + message);
                    dataOutput.writeUTF(message);
                    dataOutput.flush();
                    if(message.equals("QUITTER")) break;
                }
                dataOutput.close();
                scanner.close();
                socket.close();
            } catch (IOException ignored) {
                System.out.println("En attente du serveur");
            }
        }

    }

}
