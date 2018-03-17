package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientConnectHandler implements Runnable{

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private ObjectInputStream objectIn;
    private String path;
    private ObjectOutputStream objectOut;
    private String selection = "";
    private File file;
    private Scanner input;


    public ClientConnectHandler(Socket socket, String path) throws IOException{
        this.socket = socket;
        this.path = path;
    }

    @Override
    public void run(){
        try{
            // Serializes an array of files name and sends it back to client
            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            selection = in.readLine();

            switch(selection) {
                case "Upload":
                    // TODO Setup Upload
                    receiveFiles();
                    break;
                case "Download":
                    sendFiles();
                    break;
                case "DIR":
                    sendFileNames();
                default:
                    break;
            }

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try{
                socket.close();
            } catch (IOException e1){
                e1.printStackTrace();
            }
        }
    }
    private void sendFileNames() throws IOException{
        socket.shutdownInput();
        objectOut = new ObjectOutputStream(this.socket.getOutputStream());
        objectOut.writeObject(new SerializableFile(this.path).getServerFiles());
        objectOut.close();
    }

    private void receiveFiles() throws IOException{
        // TODO Setup Upload
        PrintWriter outFile = new PrintWriter(path + "/" + in.readLine());
        String line;
        while ((line = in.readLine()) != null) {
            outFile.println(line);
        }
        outFile.flush();
        outFile.close();
        socket.shutdownInput();
    }

    private void sendFiles() throws IOException{
        // TODO Setup Download
        socket.shutdownInput();
        String fileName = path + "/" + in.readLine();
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        file = new File(fileName);
        BufferedReader input = new BufferedReader(new FileReader(file));

        int c;
        ArrayList<Character> ch = new ArrayList<>();
        while ((c = input.read()) != -1) {
            ch.add((char) c);
        }
        for(int i = 0; i<ch.size(); i++){
            out.print(ch.get(i));
        }

        out.flush();
        socket.shutdownOutput();
        input.close();
    }
}
