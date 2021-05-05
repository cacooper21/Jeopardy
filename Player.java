/***
 * Player
 * A player in our game
 *
 * Author: Christian Duncan
 * Modified by: ...
 * CSC340 - Spring 2021
 *
 ***/
public class Player {
    private String name;  // The name of the player
    private long score;   // Accumulated points
    public ServerToClientConnection clientConnection;
    // This is the current "connection" to the App.  This will need to be a ClientConnection
    //  Or you will need some association between a player and the client it connects to.
    //  The ClientConnection is sufficient (if following the NetworkGame practice done for the AGAR.IO version)
    //  Ultimately, the Player would not store a reference to TriviaNite since there would be a Connection across the Network
    //  between them.
    transient TriviaNite client;   //If keeping this for the networking version, this PROBABLY should not be serialized.
    public Player(ServerToClientConnection clientConnection, String name) {
     //   this.client = client;
        this.clientConnection = clientConnection;
        this.name = name;
        this.score = 0;
    }

    public synchronized String getName() { return this.name; }
    public synchronized long getScore() { return this.score; }
    public synchronized void updateScore(long delta) {
        this.score += delta;
    }
}
