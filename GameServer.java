import java.io.*;
import java.net.*;
import java.util.HashSet;
public class GameServer implements Runnable {
    private Thread gameEngineThread; // The Thread that is running this game Engine
    GameEngine gameEngine;
    public HashSet<ServerToClientConnection> connection; // The set of client connections
    public int port = 1518;  
    public boolean done = false;
    public GameServer() throws FileNotFoundException{
        gameEngine = new GameEngine("triviaQuestions.txt");
        gameEngineThread = new Thread(gameEngine);
        this.connection = new HashSet<ServerToClientConnection>();
    }
   
    public void run() {
         gameEngineThread.start();
        try {
            // Create a server socket bound to the given port
            ServerSocket serverSocket = new ServerSocket(port);

            while (!done) {
                // Wait for a client request, establish new thread, and repeat
                Socket clientSocket = serverSocket.accept();
                addConnection(clientSocket);
            }
        } catch (Exception e) {
            System.err.println("ABORTING: An error occurred while creating server socket. " + e.getMessage());
            System.exit(1);
        }
  
    }
  public void addConnection(Socket clientSocket) {
        String name = clientSocket.getInetAddress().toString();
        System.out.println("Jeprody: Connecting to client: " + name);
        ServerToClientConnection c = new  ServerToClientConnection(clientSocket, name);
        connection.add(c);
        c.start(); // Start the thread.
   
}
 public static void main(String[] args)throws FileNotFoundException{
        GameServer s = new GameServer();
                s.run();
            }
}