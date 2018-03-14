package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.Serializable;

public class SerializizableFile implements Serializable{

    private String path;
    private ObservableList<String> fileNames;

    SerializizableFile(String path){
        this.path = path;
        this.fileNames = FXCollections.observableArrayList();
    }

    public String getPath() {
        return this.path;
    }

    public ObservableList<String> getFileNames() {
        return this.fileNames;
    }

    public ObservableList<String> getLocalFiles(){
        File folder = new File(getPath());
        for (File file : folder.listFiles()){
            getFileNames().add(file.getName());
        }
        return getFileNames();
    }
}
