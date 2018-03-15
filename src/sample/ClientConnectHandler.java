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
    private String selection = "";


    public ClientConnectHandler(Socket socket, String path) throws IOException{
        this.socket = socket;
        this.path = path;
    }

    @Override
    public void run(){
        try{
            // Serializes an array of files name and sends it back to client
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            selection = in.readLine();
            socket.shutdownInput();

            switch(selection) {
                case "Upload":
                    // TODO Setup Upload
                    break;
                case "Download":
                    sendFiles();
                    break;
                case "DIR":
                    sendFileNames();
                default:
                    break;
            }
            socket.close();

        } catch (Exception e){
            e.printStackTrace();
        }
    }
    private void sendFileNames() throws IOException{
        objectOut = new ObjectOutputStream(this.socket.getOutputStream());
        objectOut.writeObject(new SerializableFile(this.path, this.socket).getServerFiles());
        objectOut.close();
    }

    private void recieveFiles() throws IOException{
        // TODO Setup Upload
    }

    private void sendFiles() throws IOException{
        // TODO Setup Download
        String line;
        while ((line = in.readLine()) != null){
            if (line == "Download") {
                selection = line;
            }
        }
        System.out.println(line);
    }
}
