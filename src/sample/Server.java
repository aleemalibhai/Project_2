package sample;

import java.net.*;
import java.io.*;

public class Server {
    // Hi
    private ServerSocket serverSocket = null;

    Server (int port) throws IOException{
        serverSocket = new ServerSocket(port);
    }

    public void requests() throws IOException{
        System.out.println("Server is Up, Listening for connections");
        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientConnectHandler handler = new ClientConnectHandler(clientSocket);
                Thread thread = new Thread(handler);
                thread.start();
            }
        } catch (IOException e){
            try{
                serverSocket.close();
            } catch (IOException e1){
                e1.printStackTrace();
            }
        }
    }
}
