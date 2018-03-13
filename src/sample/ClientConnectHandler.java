package sample;

import java.net.*;
import java.io.*;

public class ClientConnectHandler implements Runnable{

    private Socket socket;
    public ClientConnectHandler(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run(){
        try{
            while(true){
                // TODO: Setup DIR, Upload, Download
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                out.println("Hey, You've connected");
            }
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            try {
                if (socket != null){
                    socket.close();
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
