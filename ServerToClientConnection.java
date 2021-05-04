import java.io.*;
import java.net.*;
public class ServerToClientConnection {
    Socket socket;
        PrintWriter out;
        boolean done;
        String name; // name of client
    public ServerToClientConnection(Socket socket, String name) {
            this.socket = socket;
            done = false;
            this.name = name;
        }
        public void start(){
            try{
                out = new PrintWriter(socket.getOutputStream(), true);
            }catch (Exception e) {
            System.err.println("ABORTING: An error occurred while creating server socket. " + e.getMessage());
            System.exit(1);
        }
            
        }
}
