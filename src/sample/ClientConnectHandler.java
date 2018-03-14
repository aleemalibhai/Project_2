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
    private ObjectOutputStream objectOut;
    private ObjectInputStream objectIn;
    private ObservableList<String> serverFileNames;
    private String path = "/home/taabish/Desktop/Project_2/Server";


    public ClientConnectHandler(Socket socket) throws IOException{
        this.socket = socket;
        in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream());
        objectOut = new ObjectOutputStream(socket.getOutputStream());

    }

    @Override
    public void run(){
        try{
            // TODO: Setup DIR, Upload, Download
            out.println("Hey, You've connected, we're going to send you the list of files");
            out.flush();

            String selection = "";
            serverFileNames = new SerializizableFile(path).getLocalFiles();
            objectOut.writeObject(serverFileNames);

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
