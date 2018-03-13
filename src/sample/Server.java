package sample;

import java.net.*;
import java.io.*;

public class Server {

    public void runServer(int port) throws IOException{

        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server is Up, Listening for connections");

        while (true){
            Socket socket = serverSocket.accept();
            Thread thread = new Thread(new ClientConnectHandler(socket));
            thread.start();
        }

    }
}
