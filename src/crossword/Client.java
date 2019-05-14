/* Copyright (c) 2019 MIT 6.031 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package crossword;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.awt.BorderLayout;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import crossword.Game.WatchListener;
/**
 * Text-protocol game client.
 */
public class Client {
    
    private static final int CANVAS_WIDTH = 1200;
    private static final int CANVAS_HEIGHT = 900;
    private static final String RESPONSE_DELIM = ";";
    
    private final String host;
    private final int port;
    private String playerID;
    private CrosswordCanvas canvas;
    
    // Abstraction function:
    //  AF(): TODO fill this in
    // Representation invariant:
    //  true
    // Safety from rep exposure:
    //  All fields are private
    // Thread safety argument:
    //  This uses the monitor pattern
    
    // TODO: Potentially delete this field later, it's for debugging purposes and will remember the previous response sent by the server
    //       It can be used to directly print errors returned by the server (i.e. invalid playerID format or something)
    private String previousResponse = "No previous response";
    
    /**
     * creates a new client
     */
    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }
    

    /**
     * Start a Crossword Extravaganza client.
     * 
     * Command to connect client: java -cp bin crossword.Client localhost 4444
     * 
     * @param args The command line arguments should include only the server address.
     * @throws IOException not connected
     */
    public static void main(String[] args) throws IOException {
        final Queue<String> arguments = new LinkedList<>(List.of(args));
        
        final String host;
        final int port;
        
        try {
            host = arguments.remove();
        } catch (NoSuchElementException nse) {
            throw new IllegalArgumentException("missing HOST", nse);
        }
        try {
            port = Integer.parseInt(arguments.remove());
        } catch (NoSuchElementException | NumberFormatException e) {
            throw new IllegalArgumentException("missing or invalid PORT", e);
        }
        
        try (                
                Socket socket = new Socket(host, port);
                BufferedReader socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream(), UTF_8));
                PrintWriter socketOut = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), UTF_8), true);
                BufferedReader systemIn = new BufferedReader(new InputStreamReader(System.in));
        ) {
            Client client = new Client(host, port);
            client.launchGameWindow(socketIn, socketOut, host, port);
            boolean showRaw = false;
            while (!socket.isClosed()) {
                System.out.print("? ");
                final String command = systemIn.readLine();
                switch (command) {
                case "":
                    System.out.println("exiting");
                    return;
                case "!debug":
                    showRaw = ! showRaw;
                    System.out.println("debugging " + (showRaw ? "on" : "off"));
                    continue;
                default:
                    socketOut.println(command);
                }
            }
            System.out.println("connection closed");
        }
    }
    
    /**
     * Returns the response from server given a request
     * @param request the request for the server 
     * @param socketIn the socket for accepting responses
     * @param socketOut the socket for sending requests
     * @return response from the server given the request
     */
    private synchronized String getResponse(String request, BufferedReader socketIn, PrintWriter socketOut) {
        try {
            // TODO: Delete this line later, it's for debugging purposes
            System.out.println("Request: " + request);
            
            socketOut.println(request);
            String response = socketIn.readLine();
            this.previousResponse = response;
            canvas.setPreviousResponse(response);
            
            // TODO: Delete this later after we're done with the project
            System.out.println("Response: " + response);
            return response;
        } catch (IOException ioe) {
            throw new RuntimeException("Error occured when processing request: " + request);
        }
    }
    
    /**
     * Transition crossword canvas from start state to choose state
     * @param canvas crossword canvas to modify
     * @param id id of the player/client
     * @param socketIn buffered reader to read input from server
     * @param socketOut print writer to write to the server
     */
    private synchronized void transitionStartState(String id, BufferedReader socketIn, PrintWriter socketOut) {
        String request = id + " " + "LOGIN";
        String response = getResponse(request, socketIn, socketOut);
        
        if (response.charAt(0) == 'I') {
            return;
        }
        
        canvas.setPlayerID(id);
        // list of puzzles RESPONSE_DELIM list of matches
        String[] matchesAndPuzzles = response.substring(1).split(RESPONSE_DELIM);
        canvas.setPuzzleList(matchesAndPuzzles[0]);
        if (matchesAndPuzzles.length == 1) {
            canvas.setMatchList("");
        } else {
            canvas.setMatchList(matchesAndPuzzles[1]);
        }
        canvas.setState(State.CHOOSE);
        canvas.repaint();
        playerID = id;
        
        // Create a new thread to watch for changes for available matches
        new Thread(new Runnable() {
            public void run() {
                try (
                    Socket watchSocket = new Socket(host, port);
                    BufferedReader watchSocketIn = new BufferedReader(new InputStreamReader(watchSocket.getInputStream(), UTF_8));
                    PrintWriter watchSocketOut = new PrintWriter(new OutputStreamWriter(watchSocket.getOutputStream(), UTF_8), true);
                    BufferedReader watchSystemIn = new BufferedReader(new InputStreamReader(System.in));
                ) {
                    while (canvas.getState() == State.CHOOSE) {
                        watchSocketOut.println(playerID + " " + "WATCH");
                        System.out.println("Waiting for watch response");
                        String watchResponse = watchSocketIn.readLine();
                        System.out.println("The watch response was: " + watchResponse);
                        canvas.setMatchList(watchResponse);
                        canvas.repaint();
                    }
                } catch (IOException ioe) {
                    System.out.println("Something went wrong in watching for changes in matches");
                } 
            }
         }).start();
         
    }
    
    /**
     * Transition crossword canvas from choose state to play state
     * 
     * Commands:
     *  PLAY Match_ID
     *  NEW Match_ID Puzzle_ID "Description"
     *  EXIT
     * 
     * @param canvas crossword canvas to modify
     * @param command command that the player can call in the choose state
     * @param socketIn buffered reader to read input from server
     * @param socketOut print writer to write to the server
     */
    private synchronized void transitionChooseState(String command, BufferedReader socketIn, PrintWriter socketOut) {
        String request = playerID + " " + command;
        String response = getResponse(request, socketIn, socketOut);
        
        String[] commandParts = command.split(" ");
        if (response.charAt(0) == 'I') {
            return;
        }
        
        if (commandParts[0].equals("PLAY")) {
            canvas.setState(State.PLAY);
        } else {
            canvas.setState(State.WAIT);
        }
        canvas.setPuzzle(response.substring(1));
        canvas.repaint();
        
        /**
        new Thread(new Runnable() {
            public void run() {
                try (
                    Socket waitSocket = new Socket(host, port);
                    BufferedReader waitSocketIn = new BufferedReader(new InputStreamReader(waitSocket.getInputStream(), UTF_8));
                    PrintWriter waitSocketOut = new PrintWriter(new OutputStreamWriter(waitSocket.getOutputStream(), UTF_8), true);
                    BufferedReader waitSystemIn = new BufferedReader(new InputStreamReader(System.in));
                ) {
                    while (true) {
                        waitSocketOut.println(playerID + " " + "WAIT");
                        // ???
                    }
                } catch (IOException ioe) {
                    System.out.println("Something went wrong in waiting for another player");
                } 
            }
         }).start();
        
        transitionWaitState(socketIn, socketOut);
        */
    }
    
    /**
     * Transition crossword canvas from wait state to play state
     * @param canvas crossword canvas to modify
     * @param socketIn buffered reader to read input from server
     * @param socketOut print writer to write to the server
     */
    private synchronized void transitionWaitState(BufferedReader socketIn, PrintWriter socketOut) {
        String request = playerID + " " + "WAIT";
        String response = getResponse(request, socketIn, socketOut);
        
        if (response.charAt(0) == 'I') {
            return;
        }
        canvas.setState(State.PLAY);
        canvas.setPuzzle(response.substring(1));
        canvas.repaint();
    }
    
    /**
     * Starter code to display a window with a CrosswordCanvas,
     * a text box to enter commands and an Enter button.
     */
    private synchronized void launchGameWindow(BufferedReader socketIn, PrintWriter socketOut, String host, int port) {
        canvas = new CrosswordCanvas("");
        canvas.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);

        JTextField textbox = new JTextField(30);
        textbox.setFont(new Font("Arial", Font.BOLD, 20));
                
        JButton enterButton = new JButton("Enter");
        enterButton.addActionListener((event) -> {
            // This code executes every time the user presses the Enter
            // button. Recall from reading 24 that this code runs on the
            // Event Dispatch Thread, which is different from the main
            // thread.
            String text = textbox.getText();
            textbox.setText("");
            canvas.repaint();
            
            if (text.length() == 0) {
                return;
            }
            
            switch (canvas.getState()) {
            case START:
                {
                    transitionStartState(text, socketIn, socketOut);
                    break;
                }
            case CHOOSE:
                {
                    transitionChooseState(text, socketIn, socketOut);
                    break;
                }
            case WAIT:
                {
                    
                    break;
                }
            case PLAY:
                {
                    break;
                }
            case SHOW_SCORE:
                {
                    break;
                }
            default:
                {
                    throw new AssertionError("should never get here");
                }
            }
        });
        enterButton.setSize(10, 10);

        JFrame window = new JFrame("Crossword Client");
        window.setLayout(new BorderLayout());
        window.add(canvas, BorderLayout.CENTER);

        JPanel contentPane = new JPanel();
        contentPane.add(textbox);
        contentPane.add(enterButton);

        window.add(contentPane, BorderLayout.SOUTH);

        window.setSize(CANVAS_WIDTH + 50, CANVAS_HEIGHT + 50);

        window.getContentPane().add(contentPane);

        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setVisible(true);
    }
    
    
}
