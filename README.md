# Jeopardy
Team Members:
Gio, Ells, Christian, Aden

Game installation:
  1. Download the code.
  2. Extract Zip file in a folder of your choice.
  3. open a terminal at folder head or navigate to head of folder with code.
  4. Compile each .java file using "javac NameOfFileHere.java"
  5. Start the GameServer by entering "Java GameServer [optional port]"
      if the port is ommitted, the server will use port 1518 by default.
      Make note of the port number you chose and IP address of your machine.
      Clients outside of LocalHost will need to know the IP to connect.
      Clients within LocalHost will only need to know the port number.
  6. Start the user client TriviaNite by entering "Java TriviaNite"
      Use the Game button in the upper left hand corner of the Application to choose name.
      From the same menu choose to change your port if you took the optional step.
      From the same menu if you are trying to connect to a non LocalHost Server choose Change Server IP.
      Now from the same menu, choose Start Game. 
      it will connect to server and questions will appear briefly. 

How to Play:
  1. When you see a question popup, click the correct answer, you have 5 seconds.
  2. Be quick with answers, the faster you answer correctly the more points you get per answer.
  3. a scoreboard of users is displayed after the 5 second timer ends after each question.

Team Contributions:
  Gio: wrote code for GameServer, ServerToClient and ClientToServer during group coding sessions, Added menu items for "change server ip",
  "change port", and "Start Game", wrote protocol, Managed Github.

  Ells: Formed idea for project, provided code to piecemeal together the client & server from a previous project, 
  small fixes primarily to fully enable multiplayer, added trivia questions to bolster up the list of questions offered, 
  code cleanup and formatting, added Headers to code. 
  
  Christian:  Assisted with the development of the client and server and set up repository

  Aden : Assisted with the development of the client and server

