package sample;

import java.net.*;
import java.io.*;

public class Server implements Runnable{

    private ServerSocket serverSocket;
    public static String serverPath;

    Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void closeServer(){
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("Server is Up, Listening for connections");
        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                try {
                    ClientConnectHandler handler = new ClientConnectHandler(clientSocket, serverPath);
                    Thread thread = new Thread(handler);
                    thread.start();
                } catch (IOException e){
                    clientSocket.close();
                }
            }
        } catch (Exception e){
            try {
                serverSocket.close();
            } catch (IOException e1){
                e1.printStackTrace();
            }
        }
    }
}



