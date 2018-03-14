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
import javafx.stage.DirectoryChooser;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class Main extends Application {

    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private TextField _address;
    private TextField _port;
    private TextField _path;
    private ObservableList<String> fileNames = null;
    private ArrayList<File> files;
    public String path = "/home/taabish/Desktop/Project_2/Client";
    private ListView<String> list1 = new ListView<>();
    private ListView<String> list2 = new ListView<>();
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
        primaryStage.setTitle("Project 2");

        // main display
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(10));

        //gridpane for the two List views
        GridPane tables = new GridPane();
        tables.setPadding(new Insets(10));
        tables.setHgap(10);
        tables.setVgap(10);

        // Client Lists
        tables.add(list1,0,0);
        // Server list
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
        APPane.setVgap(10);
        APPane.setHgap(10);

        // Adding path button and text field
        GridPane left = new GridPane();
        left.setPadding(new Insets(10, 10, 10 ,10));

        _path = new TextField();
        left.add(_path, 1, 0);
        _path.setMinWidth(300);
        _path.setPromptText("Select the path");

        btn4 = new Button("Open File path");
        btn4.setPadding(new Insets(10, 10, 10, 10));
        left.add(btn4, 0, 0);

        left.setHgap(10);
        left.setVgap(10);

        bottom.setLeft(left);
        bottom.setRight(APPane);
        layout.setBottom(bottom);

        primaryStage.setScene(new Scene(layout, 750, 500));
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
                // TODO implement connection
                connection();
            }
        });

        // directory chooser
        btn4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                DirectoryChooser directoryChooser = new DirectoryChooser();
                directoryChooser.setInitialDirectory(new File("."));
                File file = directoryChooser.showDialog(primaryStage);
                if (file == null) {
                    _path.setText("");
                    _path.setText("You didn't select a file path");
                    list1 = new ListView<>();
                    // temporary
                    list2 = new ListView<>();
                } else {
                    _path.setText("");
                    _path.setText(file.getPath());
                    path = file.getPath().toString();
                    list1 = new ListView<>(getLocalFiles(path));
                    // temporary
                    list2 = new ListView<>(getLocalFiles(path));
                }
                // Client Lists
                tables.add(list1,0,0);

                // TODO: Set this in Connection
                // Server list
                tables.add(list2,1,0);
            }
        });
    }

    public void connection(){
        try {
            int port = Integer.parseInt(_port.getText());
            String address = _address.getText();
            System.out.println(address);
            new Thread(new Server(port)).start();
            Socket socket = new Socket(address, port);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
