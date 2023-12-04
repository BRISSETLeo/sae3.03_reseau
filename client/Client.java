import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    
    public static void main(String[] args) {
        
        try {
            Socket socket = new Socket("localhost", 3030);
            Scanner scanner = new Scanner(System.in);
            DataOutputStream dataOutput = new DataOutputStream(socket.getOutputStream());
            dataOutput.writeUTF(scanner.nextLine());
            scanner.close();
            dataOutput.flush();
            dataOutput.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
