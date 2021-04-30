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
            
        }
}
