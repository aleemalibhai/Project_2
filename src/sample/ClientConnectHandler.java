package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class ClientConnectHandler implements Runnable{

    private Socket socket;
    private BufferedReader in;
    private String path;
    private ObjectOutputStream objectOut;
    private String selection = "";
    private File file;
    private ArrayList<String> fileNames;


    public ClientConnectHandler(Socket socket, String path) throws IOException{
        this.socket = socket;
        this.path = path;
        this.fileNames = new ArrayList<>();
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
    // Accesses the path provide and gets all file names within that folder
    private void sendFileNames() throws IOException{
        socket.shutdownInput();
        objectOut = new ObjectOutputStream(this.socket.getOutputStream());
        File folder = new File(path);
        for (File file : folder.listFiles()) {
            this.fileNames.add(file.getName());
        }
        objectOut.writeObject(this.fileNames);
        objectOut.close();
    }

    /* Reads in character by character data sent over the input stream
       and prints the data in a File.
    */
    private void receiveFiles() throws IOException{
        PrintWriter outFile = new PrintWriter(path + "/" + in.readLine());
        int c;
        char ch;
        while ((c = in.read()) != -1) {
            ch = (char) c;
            outFile.print(ch);
        }
        outFile.flush();
        outFile.close();
        socket.shutdownInput();
    }

    /* Reads data from a file and send its to the output stream
       character by character.
     */
    private void sendFiles() throws IOException{
        String fileName = path + "/" + in.readLine();
        socket.shutdownInput();
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        BufferedReader input = new BufferedReader(new FileReader(new File(fileName)));
        int c;
        char ch;
        while ((c = input.read()) != -1) {
            ch = (char) c;
            out.print(ch);
        }
        out.flush();
        socket.shutdownOutput();
        input.close();
    }
}
