package sample;

import javax.xml.crypto.Data;
import java.net.*;
import java.io.*;

public class ClientConnectHandler implements Runnable{

    private Socket socket = null;
    private BufferedReader in = null;
    private PrintWriter out = null;


    public ClientConnectHandler(Socket socket) throws IOException{
        this.socket = socket;
        in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream());

    }

    @Override
    public void run(){
        try{
            // TODO: Setup DIR, Upload, Download
            out.println("Hey, You've connected, we're going to send you the list of files");
            out.flush();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException e){
                System.err.println("Error");
                e.printStackTrace();
            }
        }
    }

}
