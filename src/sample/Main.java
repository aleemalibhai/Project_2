package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import java.util.ArrayList;

public class Main extends Application {

    private Button btn1;
    private Button btn2;
    private ObservableList<String> fileNames;
    private ArrayList<File> files;
    public String path = "/home/aleem/Documents/2020/Project_2/Client";
    private File fileToUpload;
    private File fileToDownload;

    // populates observable list from folder
    public ObservableList<String> getLocalFiles(String folderPath){
        File folder = new File(folderPath);
        this.files = new ArrayList<>();
        this.fileNames = FXCollections.observableArrayList();

        for (File file : folder.listFiles()){
            this.files.add(file);
            this.fileNames.add(file.getName());
        }
        return this.fileNames;
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
        tables.setHgap(5);

        // Client List
        ListView<String> list1 = new ListView<>(getLocalFiles(path));
        tables.add(list1,0,0);
        // Server list
        ListView<String> list2 = new ListView<>(getLocalFiles(path));
        tables.add(list2,1,0);
        //add list to layout
        layout.setCenter(tables);


        // borderpane for the upload download button
        BorderPane upDown = new BorderPane();
        // upload/download buttons
        btn1 = new Button("Download");
        btn2 = new Button("Upload");
        upDown.setLeft(btn1);
        upDown.setRight(btn2);

        layout.setTop(upDown);

        primaryStage.setScene(new Scene(layout, 500, 200));
        primaryStage.show();


        // buttons
        btn1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                fileToDownload = files.get(list2.getSelectionModel().getSelectedIndex());
                // download(fileToDownload)
            }
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}
