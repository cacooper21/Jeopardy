/***
 * Client to Server:
 * A simple Client to Server class that handles various commands
 *
 * Author: 
 * Modified by: ...
 * CSC340 - Spring 2021
 *
 * This handles Client connection to server and Player score.
 ***/
import java.net.*;
import java.util.Scanner;
import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
public class ClientToServerConnection extends Thread {
    Player p;
    static GameServer server;
    public Socket socket;
    public String name;
    public boolean done;
    public Scanner in;
    public boolean connectionStarted;
    public boolean connectionFailed;
    public String hostname = "127.0.0.1";  // Default is local host
    public int port = 1518;                // Default port is 1518
    private TriviaNite app;
    PrintWriter out;
 
    public ClientToServerConnection(TriviaNite app, String name){
        this.app = app;
        this.name = name;
        this.done = false;
        this.connectionStarted = false;

        this.connectionFailed = false;
    }
     //Starts up the connection with the server
        public void run(){
            try{
                //socket that connects to the server
                socket = new Socket(hostname, port);
                in = new Scanner(socket.getInputStream());
                out = new PrintWriter(socket.getOutputStream(), true);
                connectionStarted = true;
                sendToServer("JOIN " + this.name);
                while(!done){
                     String line = in.nextLine();
                    processLine(line);
                }
            }
            catch (IOException e){
                connectionStarted = true;
                connectionFailed = true;
                System.out.println("ERROR: " + e.getMessage());
            }
        }
 //Send a command to the sever
        public void sendToServer(String command){
            if (out == null){
                System.out.println("DEBUG: not connected to server, cannot transmit");
                return;
            }
            System.out.println("DEBUG: Transmitting command" + command);
            synchronized (out){
                out.println(command);
                out.flush();//make sure message is transmitted immediately
            }
        }
        public void processLine(String line){
            System.out.println("Client to Server Connection, Processing Line: " + line );
            if(line.startsWith("JOINED")){
                String playerID = line.substring(7);
                app.playerID = Integer.parseInt(playerID);
                System.out.println("Registered as player: "+ app.playerID);
            }
            else if(line.startsWith("UPDATEQUESTION")){
               String[] arr1 = line.split(" ",2);
             
               String[] arr2 = arr1[1].split("#",2);

               String questionText = arr2[0];

               List<String> choices = Arrays.asList(arr2[1].split("#"));
          
               Question question = new Question(questionText, choices);
               app.updateQuestion(question);
            }
            else if(line.startsWith("ANSWER")){
                 String[] arr1 = line.split(" ",2); //gets rid of first ANSWER
                 String[] arr2 = arr1[1].split("#"); //answer#name1@score1#name2@score2
                int answer = Integer.parseInt(arr2[0]);

                ArrayList<TriviaNite.PlayerScores> playerScores = new ArrayList<TriviaNite.PlayerScores>();
                for(int i = 1;i<arr2.length;i++){
                    String[] arr3 = arr2[i].split("@", 2);

                    playerScores.add(new TriviaNite.PlayerScores(arr3[0], arr3[1]));
                }
                app.postAnswer(answer, playerScores);
            }
        }
}