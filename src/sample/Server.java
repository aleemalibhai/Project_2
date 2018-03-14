package sample;

import java.net.*;
import java.io.*;

public class Server implements Runnable{

    private ServerSocket serverSocket = null;

    Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    @Override
    public void run() {
        System.out.println("Server is Up, Listening for connections");
        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                try {
                    ClientConnectHandler handler = new ClientConnectHandler(clientSocket);
                    Thread thread = new Thread(handler);
                    thread.start();
                    serverSocket.close();
                } catch (IOException e){
                    clientSocket.close();
                }
            }
        } catch (IOException e){
            try {
                serverSocket.close();
            } catch (IOException e1){
                e1.printStackTrace();
            }
        }
    }
}



