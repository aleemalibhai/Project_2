package sample;

import java.io.File;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class SerializableFile implements Serializable {

    private String path;
    private ArrayList<String> fileNames;


    SerializableFile(String path) {
        this.path = path;
        this.fileNames = new ArrayList<>();
    }

    public String getPath() {
        return this.path;
    }

    public ArrayList<String> getFileNames() {
        return this.fileNames;
    }

    public ArrayList<String> getServerFiles() {
        File folder = new File(getPath());
        for (File file : folder.listFiles()) {
            getFileNames().add(file.getName());
        }
        return getFileNames();
    }
}
