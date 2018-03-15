package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.DirectoryChooser;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Main extends Application {

    Button _conBtn;
    private TextField _address;
    private TextField _port;
    private CheckBox _serverCreateCheck;
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private String address;
    private static int port;
    private static boolean serverCreateCheck = true;
    private TextField _path;
    private static Server startServer;


    public static void main(String[] args) throws IOException{
        launch(args);
        startServer.closeServer();
    }

    public void start(Stage primaryStage){
            Stage secondaryStage = null;
            primaryStage.setTitle("Address and Port");
            GridPane layout2 = new GridPane();
            _conBtn = new Button("Connect");
            _address = new TextField();
            _address.setPromptText("Specify Address");

            _port = new TextField();
            _port.setPromptText("Specify Port");

            _serverCreateCheck = new CheckBox();
            _serverCreateCheck.setText("Create new Server on Startup");
            _serverCreateCheck.setSelected(true);

            layout2.add(_address,0,0);
            layout2.add(_port,0,1);
            layout2.add(_conBtn,0,2);
            layout2.add(_serverCreateCheck,0,3);

            _conBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event){
                    port = Integer.parseInt(_port.getText());
                    address = _address.getText();
                    serverCreateCheck = _serverCreateCheck.isSelected();
                    primaryStage.close();
                    try {
                        new SecondStage();
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }
            });

            Scene scene = new Scene(layout2, 900, 500);
            primaryStage.setScene(scene);
            primaryStage.show();
        }

    public class SecondStage extends Stage {

        private ObservableList<String> fileNames = null;
        private ArrayList<File> files;
        private ArrayList<String> serverFiles;
        public String path = null;
        private ListView<String> list1 = new ListView<>();
        private ListView<String> list2 = new ListView<>();
        private File fileToUpload;
        private String fileToDownload;
        private ObjectInputStream objectIn;
        private Stage secondStage = this;

        SecondStage() throws IOException{
            if(serverCreateCheck){
                startServer = new Server(port);
                Thread thread = new Thread(startServer);
                thread.start();
            }
            secondStage.setTitle("Project 2");

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
            GridPane topLeft = new GridPane();
            // upload/download buttons
            btn1 = new Button("Upload");
            btn1.setPadding(new Insets(10, 10, 10, 10));
            btn2 = new Button("Download");
            btn2.setPadding(new Insets(10, 10, 10, 10));
            topLeft.add(btn1,1,0);
            topLeft.add(btn2,0,0);
            topLeft.setHgap(10);
            layout.setTop(topLeft);
            layout.setAlignment(topLeft, Pos.TOP_LEFT);

            //server port and address bars
            GridPane bottom = new GridPane();
            btn3 = new Button("Connect");
            btn3.setMinWidth(75);
            btn3.setPadding(new Insets(10, 10, 10, 10));
            bottom.add(btn3, 1, 0);

            // Adding path button and text field
            GridPane directoryChooser = new GridPane();
            directoryChooser.setPadding(new Insets(10, 10, 10 ,10));

            _path = new TextField();
            _path.setMinWidth(120);
            directoryChooser.add(_path, 1, 0);
            _path.setPromptText("Select the path");

            btn4 = new Button("Open File path");
            btn4.setMinWidth(120);
            btn4.setPadding(new Insets(10, 10, 10, 10));
            directoryChooser.add(btn4, 0, 0);

            directoryChooser.setHgap(10);
            directoryChooser.setVgap(10);

            bottom.add(directoryChooser,0,0);
            layout.setBottom(bottom);

            secondStage.setScene(new Scene(layout, 540, 500));
            secondStage.show();


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
                    // TODO download(fileToDownload)
                }
            });

            // connect to port
            btn3.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    // TODO implement connection
                    try {
                        System.out.println(address);
                        Socket socket = new Socket(address, port);
                        objectIn = new ObjectInputStream(socket.getInputStream());
                        try {
                            serverFiles = new ArrayList<>((ArrayList<String>)objectIn.readObject());
                        } catch (Exception e1){
                            e1.printStackTrace();
                        }
                        list2 = new ListView<>(FXCollections.observableArrayList(serverFiles));
                        tables.add(list2,1,0);
                        objectIn.close();
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            // directory chooser
            btn4.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                    DirectoryChooser directoryChooser = new DirectoryChooser();
                    directoryChooser.setInitialDirectory(new File("."));
                    File file = directoryChooser.showDialog(secondStage);
                    if (file == null) {
                        _path.setText("");
                        _path.setText("You didn't select a file path");
                        list1 = new ListView<>();
                    } else {
                        _path.setText("");
                        _path.setText(file.getPath());
                        path = file.getPath().toString();
                        list1 = new ListView<>(getLocalFiles(path));
                    }
                    // Client Lists
                    tables.add(list1,0,0);
                }
            });
        }

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

    }
}


