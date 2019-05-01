package crossword;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Set;


/**
 * Text-protocol game server.
 */
public class TextServer {
    // Abstraction function:
    //    AF(serverSocket, game, nextID) --> TextServer Object having the ability to connect one player to a Crossword Puzzle game. 
    //                                        One serverSocket (a rep) is able to run multiple connections, game is the game class for
    //                                        the Crossword game, and a nextID for the next players for concurrency
    // Representation invariant:
    //  true
    // Safety from rep exposure:
    //  serverSocket is private and final
    //  game is private and final
    //  nextID is never given to the client
    
    private final ServerSocket serverSocket;
    private final Game game;
    private int nextID = 1;
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
                        handleConnection(socket, Integer.toString(nextID));
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
            nextID++;
        }
    }
    
    /**
     * Handle a single client connection.
     * Returns when the client disconnects.
     * 
     * @param socket socket connected to client
     * @throws IOException if the connection encounters an error or closes unexpectedly
     */
    private void handleConnection(Socket socket, String playerID) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), UTF_8));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), UTF_8), true);
        
        try {
            for (String input = in.readLine(); input != null; input = in.readLine()) {
                String output = handleRequest(input, playerID);
                if (output.equals("quit")) {
                    break;
                }
                out.println(output);

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
    private String handleRequest(String input, String playerID) throws IOException {
        String[] tokens = input.split(" ");
        Set<String> puzzleNames = game.getPuzzleNames();
        if (tokens[0].equals("quit")) {
            return "quit";
        }
        if (tokens[0].equals("GET")) {
            StringBuilder allPuzzles = new StringBuilder();
            for (String puzzle: puzzleNames) {
                allPuzzles.append(puzzle + ",");
            }
            allPuzzles.deleteCharAt(allPuzzles.length()-1);
            return allPuzzles.toString();
        }
        if (puzzleNames.contains(tokens[0])) {
            return game.getPuzzleForResponse(tokens[0]);
        }
        if (!puzzleNames.contains(tokens[0])) {
            return tokens[0];
        }
        // if we reach here, the client message did not follow the protocol
        throw new UnsupportedOperationException(input);
    }
}
