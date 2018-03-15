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
    private String path;
    private ObjectOutputStream objectOut;


    public ClientConnectHandler(Socket socket, String path) throws IOException{
        this.socket = socket;
        this.path = path;
    }

    @Override
    public void run(){
        try{
            String selection = "";

            // Serializes an array of files name and sends it back to client
            sendFileNames();


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
    private void sendFileNames() throws IOException{
        objectOut = new ObjectOutputStream(this.socket.getOutputStream());
        objectOut.writeObject(new SerializableFile(this.path, this.socket).getServerFiles());
        objectOut.flush();
        objectOut.close();
    }

    private void recieveFiles() throws IOException{
        // TODO Setup Upload
    }

    private void sendFiles() throws IOException{
        // TODO Setup Download
    }



}
