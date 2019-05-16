package crossword;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

import crossword.Game.PlayListener;
import crossword.Game.WaitListener;
import crossword.Game.WatchListener;


/**
 * Text-protocol game server.
 */
public class TextServer {
    private static final String QUIT = "quit";
    private static final String LISTENER = "listener"; // Returned by listener commands 
    
    // Abstraction function:
    //    AF(serverSocket, game): TextServer Object having the ability to connect one player to 
    //                            a Crossword Puzzle game. One serverSocket (a rep) is able to 
    //                            run multiple connections, game is the game class for
    //                            the Crossword game
    // Representation invariant:
    //  true
    // Safety from rep exposure:
    //  serverSocket is private and final
    //  game is private and final
    // Thread Safety Argument:
    //   TextServer creates a new thread for each new connecting client
    //   game is an object with threadsafe type
    //   threads for each clients operates on their own and do not interact with each other
    
    private final ServerSocket serverSocket;
    private final Game game;
    /**
     * Make a new text game server using game that listens for connections on port.
     * 
     * @param game shared crossword puzzles
     * @param port server port number
     * @throws IOException if an error occurs opening the server socket
     */
    public TextServer(Game game, int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.game = game;
    }
        
    /**
     * @return the port on which this server is listening for connections
     */
    public int port() {
        return serverSocket.getLocalPort();
    }
    
    /**
     * Run the server, listening for and handling client connections.
     * Never returns normally.
     * 
     * @throws IOException if an error occurs waiting for a connection
     */
    public void serve() throws IOException {
        System.err.println("Server listening on " + serverSocket.getLocalSocketAddress());
        while (true) {  
            Socket socket = serverSocket.accept();
            try {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        handleConnection(socket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    finally {
                        try {
                            socket.close();
                        }
                        catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Prints game stats for debugging including info such as:
     *  - Number of watch listeners (for CHOOSE state)
     *  - Number of wait listeners (for WAIT state)
     *  - Number of play listeners (for PLAY state)
     */
    private void printGameStats() {
        game.printListenerStats();
    }
    
    /**
     * Handle a single client connection.
     * Returns when the client disconnects.
     * 
     * @param socket socket connected to client
     * @throws IOException if the connection encounters an error or closes unexpectedly
     */
    private void handleConnection(Socket socket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), UTF_8));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), UTF_8), true);
        
        try {
            for (String input = in.readLine(); input != null; input = in.readLine()) {
                String output = handleRequest(input, out);
                if (output.equals(QUIT)) {
                    break;
                } else if (output.equals(LISTENER)) {
                    continue;
                }
                out.println(output);
                
                // For debugging only: 
                // printGameStats();
            }
        } finally {
            out.close();
            in.close();
        }
    }
    
    /**
     * Handle a single client request and return the server response.
     * 
     * @param input message from client
     * @return output message to client
     * @throws IOException 
     */
    private String handleRequest(String input, PrintWriter out) throws IOException {  
        String[] tokens = input.split(" ");
        if (tokens.length < 2) {
            return "I" + "Invalid command";
        }
        
        String playerID = tokens[0];
        String command = tokens[1];  
        
        // Check whether the playerID is valid
        if (!(playerID.matches("[A-Za-z0-9]*") && playerID.length() > 0)) {
            return "I" + "Player ID must be alphanumeric and have a length > 0";
        }
        
        if (command.equals("quit")) {
            return QUIT;
        }
        //TODO: implement each command as needed
        if (command.equals("WATCH")) { // blocks and returns only when there is a change in available matches
            game.addWatchListener(new WatchListener() {
                public void onChange() {
                    out.println("V" + game.getAvailableMatchesForResponse());
                }
            });
            
            return LISTENER;
        }
        else if (command.equals("WAIT")) { // blocks and returns only when a match has two players
            game.addWaitListener(playerID, new WaitListener() {
                public void onChange() {
                    out.println("V" + game.getMatchPuzzleForResponse(playerID));
                }
            });
            
            return LISTENER;
        }
        else if (command.equals("WAIT_PLAY")) { // blocks and returns only when a play has been made in a match
            game.addPlayListener(playerID, new PlayListener() {
                public void onChange() {
                    out.println("V" + game.getPlayListenerResponse(playerID));
                }
            });
            
            return LISTENER;
        }
        else if (command.equals("LOGIN")) { // Logs in a player and returns the names of all puzzle templates
            if (game.login(playerID)) {
                // Build the string in 3 parts: add puzzle names -> add response delim -> add available matches
                StringBuilder builder = new StringBuilder();
                builder.append("V");
                builder.append(game.getPuzzleNamesForResponse());
                builder.append(Game.RESPONSE_DELIM);
                builder.append(game.getAvailableMatchesForResponse());
                return builder.toString();
            } else {
                return "I" + "Player id is already taken";
            }
        }
        else if (command.equals("PLAY")) {
            if (tokens.length < 3) {
                return "I" + "PLAY command must include matchID";
            }
            boolean join = game.joinMatch(playerID, tokens[2]);
            if (join) {
                return "V" + game.getPuzzleFromMatchID(tokens[2]);
            }
            return "I" + "Failed to join the match";
        }
        else if (command.equals("NEW")) {
            if (tokens.length < 5) {
                return "I" + "NEW command must contain matchID, puzzleID, and description";
            }
            
            String matchID = tokens[2];
            String puzzleID = tokens[3];
            String description = "";
            // The rest of the string is the description
            for (String token : Arrays.copyOfRange(tokens, 4, tokens.length)) {
                description += " " + token;
            }
            // Remove the extra space at the beginning
            description = description.substring(1);
            
            if (description.matches("\"[^\\n\\t\\\\\\r]+\"")) {
                description = description.substring(1, description.length()-1);
            } else {
                return "I" + "Description should not contain newlines, tabs or \\ and have length > 0" + description;
            }
            // Check whether MATCH_ID is alphanumeric
            if (!matchID.matches("[0-9a-zA-Z]+")) {
                return "I" + "MatchID should be alphanumeric and have length > 0: " + matchID;
            }
            
            boolean create = game.createMatch(playerID, matchID, puzzleID, description);
            if (create) {
                return "V";
            }
            return "I" + "Failed to create a match";
        }
        else if (command.equals("LOGOUT")) {
            if (game.logout(playerID)) {
                return "V";
            }
            return "I" + "Failed to log out the player";
        }
        else if (command.equals("EXIT_WAIT")) {
            if (game.exitWait(playerID)) {
                return "V";
            }
            return "I" + "Failed to log out the player";
        }
        else if (command.equals("EXIT_PLAY")) {
            if (game.exitPlay(playerID)) {
                String score = game.showScore(playerID);
                game.removePlayerAndMatch(playerID);
                return "V" + score;
            }
            return "I" + "Failed to exit game";
        }
        else if (command.equals("TRY")) {
            int wordID = Integer.parseInt(tokens[2]);
            String word = tokens[3].toLowerCase();
            if (game.tryWord(playerID, wordID, word)) {
                String response = game.getGuessesForResponse(playerID);
                game.removePlayerAndMatch(playerID);
                return "V" + response;
            }
            return "I" + "Failed to guess word.";
        }
        else if (command.equals("CHALLENGE")) {
            int wordID = Integer.parseInt(tokens[2]);
            String word = tokens[3].toLowerCase();
            if (game.challengeWord(playerID, wordID, word)) {
                String response = game.getGuessesForResponse(playerID);
                game.removePlayerAndMatch(playerID);
                return "V" + response;
            }
            return "I" + "Failed to challenge word.";        
        }

        // if we reach here, the client message did not follow the protocol
        // Instead of throwing an except, return a response indicating  failure
        return "I" + "Sorry, that is not a valid command: " + input;
        // throw new UnsupportedOperationException(input);

    }
}
