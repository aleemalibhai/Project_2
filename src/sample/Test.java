package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.*;



import java.awt.*;
import java.net.*;
import java.io.*;

public class Test extends Application {

    private TextField _text1;
    private TextField _text2;
    private Button _btn1;


    public static void Test(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Server Joiner");

        BorderPane layout = new BorderPane();
        GridPane gp = new GridPane();
        gp.setPadding(new Insets(10, 10, 10, 10));
        layout.setPadding(new Insets(10));


        Label serverIP = new Label("Server IP");
        _text1 = new TextField();
        gp.add(serverIP, 0, 0);
        gp.add(_text1, 1, 0);
        _text1.setPromptText("Enter server IP");

        Label serverPort = new Label("Server Port: ");
        _text2 = new TextField();
        gp.add(serverPort, 0, 1);
        gp.add(_text2, 1, 1);
        _text1.setPromptText("Enter server Port");


        _btn1 = new Button("Submit");
        _btn1.setPadding(new Insets(10));
        gp.setHgap(10);
        gp.setVgap(10);
        layout.setLeft(gp);
        layout.setBottom(_btn1);
        layout.setAlignment(_btn1, Pos.BOTTOM_RIGHT);


        _btn1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    int port = Integer.parseInt(_text2.getText());
                    String address = _text1.getText();
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

        });

        primaryStage.setScene(new Scene(layout, 300, 275));
        primaryStage.show();
    }
}