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
    }

    @Override
    public void run(){
        try{
            String selection = "";

            // Serializes an array of files name and sends it back to client
            new SerializableFile(path, this.socket).sendFileNames();

            switch(selection) {
                case "Upload":
                    // TODO Setup Upload
                    break;
                case "Download":
                    // TODO Setup Download
                    break;
                default:
                    break;
            }

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e){
                System.err.println("Error");
                e.printStackTrace();
            }
        }
    }
}
