import java.io.*;
import java.net.*;
import java.util.HashSet;
public class GameServer implements Runnable {
    private Thread gameEngineThread; // The Thread that is running this game Engine
    GameEngine gameEngine;
    public HashSet<ServerToClientConnection> connection; // The set of client connections
    public int port = 1518;  
    public boolean done = false;
    public String questionFileName = "triviaQuestions.txt"; /// The name of file with the question DB -- not stored here if doing Network version!

    public GameServer() throws FileNotFoundException{
        createGame();
        this.connection = new HashSet<ServerToClientConnection>();
    }
   
    public void run() {
     
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
        ServerToClientConnection c = new  ServerToClientConnection(clientSocket, name, gameEngine);
        connection.add(c);
        c.start(); // Start the thread.
   
}
// Helper function to start up a game engine
    //   In a network version this would NOT be used
    private void createGame() {
        if (gameEngine == null) {
            try {
                gameEngine = new GameEngine(questionFileName);
                gameEngineThread = new Thread(gameEngine);
                gameEngineThread.start();
            } catch (FileNotFoundException e) {
                System.err.println("Error: The file " + questionFileName + " could not be loaded.");
                System.err.println(e.getMessage());
            }
        }
    }
 public static void main(String[] args)throws FileNotFoundException{
        GameServer s = new GameServer();
                s.run();
            }
}