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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

public class Main extends Application {

    private Button btn1;
    private Button btn2;
    private Button btn3;
    private TextField _address;
    private TextField _port;
    private ObservableList<String> fileNames;
    private ArrayList<File> files;
    public String path = "/home/aleem/Documents/2020/Project_2/Client";
    private File fileToUpload;
    private String fileToDownload;

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
        btn1 = new Button("Upload");
        btn2 = new Button("Download");
        upDown.setLeft(btn1);
        upDown.setRight(btn2);
        layout.setTop(upDown);

        //server port and address bars
        BorderPane bottom = new BorderPane();
        GridPane APPane = new GridPane();
        btn3 = new Button("Connect");
        _address = new TextField();
        _address.setPromptText("Address");

        _port = new TextField();
        _port.setPromptText("Port");

        APPane.add(_address,0,0);
        APPane.add(_port,0,1);
        APPane.add(btn3,0,2);

        bottom.setRight(APPane);
        layout.setBottom(bottom);


        primaryStage.setScene(new Scene(layout, 500, 200));
        primaryStage.show();


        // buttons
        // upload
        btn1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                fileToUpload = files.get(list1.getSelectionModel().getSelectedIndex());
                // TODO upload(fileToUpload)
            }
        });
        //download
        btn2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                fileToDownload = list2.getSelectionModel().getSelectedItem();
                // TODO download(fileToUpload)
            }
        });

        // connect to port
        btn3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // TODO implement conection
            }
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}
