import java.net.*;
import java.util.Scanner;
import java.io.*;

public class ClientToServerConnection extends Thread {
    Player p;
    GameServer server;
    public Socket socket;
    public String name;
    public boolean done;
    public Scanner in;
    public boolean connectionStarted;
    public boolean connectionFailed;
    private String hostname = "127.0.0.1";  // Default is local host
    private int port = 1518;                // Default port is 1518

    public ClientToServerConnection(Player p, GameServer server,String name){
        this.p = p;
        this.server = server;
        this.name = name;
        this.done = false;
        this.connectionStarted = false;
        this.connectionFailed = false;
    }
     //Starts up the connection with the server
        public void run() {
            try {
                //socket that connects to the server
                socket = new Socket(hostname, port);
                in = new Scanner(socket.getInputStream());
                connectionStarted = true;
                while(!done){

                }
            }
            catch (IOException e) {
                connectionStarted = true;
                connectionFailed = true;
                System.out.println("ERROR: " + e.getMessage());
            

            }
}
}