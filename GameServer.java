public class GameServer implements Runnable {
    GameEngine gameEngine;
    QuestionDB qdb;
    
    public GameServer(){
        qdb = new QuestionDB("triviaQuestions.txt");
        gameEngine = new gameEngine(qdp);
    }
    public synchronized int addPlayer(String name, TriviaNite client) {
        return gameEngine.addPlayer(name, client);
    }
    public void run() {
        
        long currentTime = System.currentTimeMillis();
        while (!gameEngine.isDone()) {
            debug.println(10, "(GameServer.run) Executing...");
            // Compute elapsed time since last iteration
            long newTime = System.currentTimeMillis();
            if(!gameEngine.getP)
            
            

            try {
                Thread.sleep(100);
            } catch (Exception e) { }
        } 
    }

}