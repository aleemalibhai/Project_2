package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.DirectoryChooser;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Main extends Application {

    private Button _conBtn;
    private TextField _address;
    private TextField _port;
    private TextField _serverDir;
    private CheckBox _serverCreateCheck;
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;
    private String address;
    private static int port;
    private static boolean serverCreateCheck = true;
    private TextField _path;
    private static Server startServer;
    private static String commandArg = null;
    private static String commandFile = null;


    public static void main(String[] args){
        if (args.length > 0) {
            if (!args[0].isEmpty()) {
                commandArg = args[0];
            }
        }
        if (args.length > 1) {
            if (!args[1].isEmpty()) {
                commandFile = args[1];
            }
        }

        launch(args);
        try{
            startServer.closeServer();
        } catch (Exception e){
            System.out.println("no server started");
        }
    }

    public void start(Stage primaryStage){
            primaryStage.setTitle("Address and Port");
            GridPane layout2 = new GridPane();
            _conBtn = new Button("Connect");
            _conBtn.setPadding(new Insets(5));
            _address = new TextField();
            _address.setPromptText("Specify Address");

            GridPane dirChooser2 = new GridPane();
            dirChooser2.setHgap(5);
            btn5 = new Button("Choose Folder");
            btn5.setPadding( new Insets(10));
            _serverDir = new TextField();
            _serverDir.setPadding(new Insets(10));
            _serverDir.setPromptText("Choose Server Folder");

            dirChooser2.add(btn5,0,0);
            dirChooser2.add(_serverDir, 1,0);

            btn5.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    DirectoryChooser directoryChooser = new DirectoryChooser();
                    directoryChooser.setInitialDirectory(new File("."));
                    File file = directoryChooser.showDialog(primaryStage);
                    if (file == null) {
                        _serverDir.setText("You didn't select a path");
                    } else {
                        _serverDir.setText(file.getPath());
                        Server.serverPath = file.getPath();
                    }
                }
            });

            _port = new TextField();
            _port.setPromptText("Specify Port");

            _serverCreateCheck = new CheckBox();
            _serverCreateCheck.setText("Create new Server on Startup");
            _serverCreateCheck.setSelected(true);


            layout2.add(_address,0,0);
            layout2.add(_port,0,1);
            layout2.add(_conBtn,0,2);
            layout2.add(_serverCreateCheck,0,3);
            layout2.add(dirChooser2,0,4);
            layout2.setAlignment(Pos.CENTER);

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

            Scene scene = new Scene(layout2, 300, 200);
            primaryStage.setScene(scene);
            primaryStage.show();
        }

    public class SecondStage extends Stage {

        private ObservableList<String> fileNames = null;
        private ArrayList<File> files;
        private ArrayList<String> serverFiles;
        private String path = null;
        private ListView<String> list1 = new ListView<>();
        private ListView<String> list2 = new ListView<>();
        private String fileToUpload;
        private String fileToDownload;
        private ObjectInputStream objectIn;
        private Stage secondStage = this;
        private GridPane tables;
        private GridPane bottom;

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
            tables = new GridPane();
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
            bottom = new GridPane();
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

            if (commandArg != null) {
                commandArg = commandArg.toLowerCase();
                if (commandArg.equals("upload")) {
                    upload(commandFile);
                    connect();
                } else if (commandArg.equals("download")) {
                    download(commandFile);
                } else if (commandArg.equals("dir")) {
                    connect();
                }
            }


            // buttons
            // upload
            btn1.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    // TODO upload(fileToUpload)
                    fileToUpload = list1.getSelectionModel().getSelectedItem();
                    upload(fileToUpload);
                }
            });

            //download
            btn2.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    // TODO download(fileToDownload)
                    fileToDownload = list2.getSelectionModel().getSelectedItem();
                    download(fileToDownload);
                }
            });

            // connect to port
            btn3.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    // TODO implement connection
                    connect();
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
                        System.err.println("You didn't select a file path");
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

        // Connects to server and receives a list of file names located on the server
        public void connect(){
            try {
                Socket socket = new Socket(address, port);
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                out.println("DIR");
                out.flush();
                socket.shutdownOutput();
                objectIn = new ObjectInputStream(socket.getInputStream());
                try {
                    serverFiles = new ArrayList<>((ArrayList<String>)objectIn.readObject());
                } catch (Exception e1){
                    e1.printStackTrace();
                }
                list2 = new ListView<>(FXCollections.observableArrayList(serverFiles));
                tables.add(list2,1,0);
                socket.shutdownInput();
                socket.close();
                if(btn3.getText() == "Connect") {
                    btn3.setText("Refresh");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /*
            Connects to the server and receives character by character data
            and prints the data to a File.
        */
        public void download(String fileDownload){
            if (fileDownload == null){
                System.err.println("Error, no file selected");
            } else {
                try {
                    String wholePath;
                    ArrayList<String> splitter = new ArrayList<>();
                    for (String retrieval: fileDownload.split("/")){
                        splitter.add(retrieval);
                    }
                    Socket socket = new Socket(address, port);
                    PrintWriter out = new PrintWriter(socket.getOutputStream());
                    out.println("Download");
                    out.println(splitter.get(splitter.size() - 1));
                    out.flush();
                    socket.shutdownOutput();
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    if (path != null){
                        wholePath = path + "/" + fileDownload;
                    } else {
                        wholePath = fileDownload;
                    }
                    PrintWriter outFile = new PrintWriter(wholePath);
                    int c;
                    char ch;
                    while ((c = in.read()) != -1) {
                        ch = (char) c;
                        outFile.print(ch);
                    }
                    outFile.flush();
                    outFile.close();
                    socket.shutdownInput();
                    socket.close();
                    String pathSplit = wholePath.replaceAll("/" + splitter.get(splitter.size() - 1), "");
                    list1 = new ListView<>(getLocalFiles(pathSplit));
                    tables.add(list1, 0, 0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void upload(String fileUpload){
            if (fileUpload == null){
                System.err.println("Error, no file selected");
            } else {
                try {
                    String wholePath;
                    ArrayList<String> splitter = new ArrayList<>();
                    for (String retrieval: fileUpload.split("/")){
                        splitter.add(retrieval);
                    }
                    Socket socket = new Socket(address, port);
                    PrintWriter out = new PrintWriter(socket.getOutputStream());
                    out.println("Upload");
                    out.println(splitter.get(splitter.size() - 1));
                    if (path != null){
                        wholePath = path + "/" + fileUpload;
                    } else {
                        wholePath = fileUpload;
                    }
                    BufferedReader input = new BufferedReader(new FileReader(wholePath));
                    int c;
                    char ch;
                    while ((c = input.read()) != -1) {
                        ch = (char) c;
                        out.print(ch);
                    }
                    out.flush();
                    input.close();
                    socket.shutdownOutput();
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                connect();
            }
        }
    }
}


