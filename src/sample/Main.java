package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

    private Button btn1;
    private Button btn2;
    public String path = "";


    public ObservableList<File> getLocalFiles(String folderPath){
        File folder = new File(folderPath);
        ObservableList<File> files = FXCollections.observableArrayList();
        for (File file : folder.listFiles()){
            files.add(file);
        }

        return files;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Project 2");

        // main display
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(10));

        //gridpane for the two List views
        GridPane tables = new GridPane();
        tables.setPadding(new Insets(10));

        // Client List
        ListView<File> list1 = new ListView<>(getLocalFiles(path));

        // gridpane for the upload download button
        GridPane upDown = new GridPane();
        // upload/download buttons
        btn1 = new Button("Download");
        btn2 = new Button("Upload");
        upDown.add(btn1,0,0);
        upDown.add(btn2,1,0);

        layout.setTop(upDown);




        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
