import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    
    public static void main(String[] args) {

        String ip = args[0];
        String pseudo = args[1];

        while(ip == null){
            System.out.print("Entrer votre ip : ");
            pseudo = new Scanner(System.in).nextLine();
        }
        while(pseudo == null){
            System.out.print("Entrer votre pseudo : ");
            pseudo = new Scanner(System.in).nextLine();
        }

        Socket socket = null;
        
        while(socket == null){
            try {
                socket = new Socket(ip, 3030);
                Scanner scanner = new Scanner(System.in);
                DataOutputStream dataOutput = new DataOutputStream(socket.getOutputStream());
                dataOutput.writeUTF(pseudo);
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
