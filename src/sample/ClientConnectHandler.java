package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class ClientConnectHandler implements Runnable{

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private ObjectInputStream objectIn;
    private String path = "/home/taabish/Desktop/Project_2/Server";


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

            String selection = "";
            new SerializableFile(path, this.socket).sendFiles();

            switch(selection) {
                case "Upload":
                    // TODO Setup Upload
                    break;
                case "Download":
                    // TODO Setup Download
                    break;
                default:
                    out.println("You didn't select Upload/Download");
                    break;
            }

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
