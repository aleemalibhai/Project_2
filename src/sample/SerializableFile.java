package sample;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;

public class SerializableFile implements Serializable{

    private String path;
    private ArrayList<String> fileNames;
    private Socket clientSocket;
    private ObjectOutputStream objectOut;

    SerializableFile(String path, Socket clientSocket){
        this.path = path;
        this.fileNames = new ArrayList<>();
        this.clientSocket = clientSocket;
    }

    public String getPath() {
        return this.path;
    }

    public ArrayList<String> getFileNames() {
        return this.fileNames;
    }

    public ArrayList<String> getServerFiles(){
        File folder = new File(getPath());
        for (File file : folder.listFiles()){
            getFileNames().add(file.getName());
        }
        return getFileNames();
    }

    public void sendFiles() throws IOException{
        objectOut = new ObjectOutputStream(this.clientSocket.getOutputStream());
        objectOut.writeObject(getServerFiles());
        objectOut.flush();
        objectOut.close();
    }
}
