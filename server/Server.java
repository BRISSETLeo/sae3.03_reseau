package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    public static void main(String[] args) {
        
        try {
            ServerSocket serverSocket = new ServerSocket(3030);

            while(!serverSocket.isClosed()){
                Socket socket = serverSocket.accept();
                Scanner input = new Scanner(socket.getInputStream());
                System.out.println("Message reçu : " + input.nextLine());
                socket.close();
                serverSocket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}