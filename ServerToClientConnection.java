import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class ServerToClientConnection extends Thread{
    Socket socket;
        PrintWriter out;
        boolean done;
        String name; // name of client
        GameEngine engine;//reference to game engine
        public Scanner in;
        int playerID;
        Question currentQuestion;
    public ServerToClientConnection(Socket socket, String name, GameEngine engine) {
            this.socket = socket;
            playerID = -1;
            this.engine = engine;
            done = false;
            this.name = name;
        }
        @Override
        public void run(){
            try{
                 in = new Scanner(socket.getInputStream());
                out = new PrintWriter(socket.getOutputStream(), true);
                while(!done){
                    String line = in.nextLine();
                    processLine(line);
                }
            }catch (Exception e) {
            System.err.println("ABORTING: An error occurred while creating server socket. " + e.getMessage());
            System.exit(1);
        }
            
        }
        public void processLine(String line){
            System.out.print("Server to Client Connection, Processing Line: " + line );
            if(line.startsWith("JOIN")){
                joinServer(line);
            }
            else if(line.startsWith("CHOICE")){
                String[] arr1 = line.split(" ",2);
                    int choice = Integer.parseInt(arr1[1]);
                    engine.makeChoice(playerID, choice);
            }
            
        }
        public void joinServer(String line){
            String name = line.substring(5); //grab name from line
            playerID = engine.addPlayer(this, name);
            sendToClient("JOINED " + playerID);
        }
        //Send a command to the Client
        public void sendToClient(String command) {
            if (out == null) {
                System.out.println("DEBUG: not connected to server, cannot transmit");
                return;
            }
            System.out.println("DEBUG: Transmitting command " + command);
            synchronized (out) {
                out.println(command);
                out.flush();//make sure message is transmitted immediately
            }
        }
        public void updateQuestion(Question question){
             currentQuestion = question;
             String actualQuestion = currentQuestion.question;
             List<String> choices = currentQuestion.choices;
             String finalCommand = actualQuestion;
           
             //UPDATEQUESTION question choices
            for(String choice : choices){
                finalCommand += "#" + choice;
            }
            sendToClient("UPDATEQUESTION " + finalCommand);
             

            
        }
        public void postAnswer(int answer, String scores){
            //ANSWER answer#name1@score1#...
            String finalCommand = "ANSWER " + answer + scores; 
            sendToClient(finalCommand);

        }
}
