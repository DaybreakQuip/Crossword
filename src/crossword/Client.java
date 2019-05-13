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
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
/**
 * Text-protocol game client.
 */
public class Client {
    
    private static final int CANVAS_WIDTH = 1200;
    private static final int CANVAS_HEIGHT = 900;
    private static final String RESPONSE_DELIM = ";";

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
            launchGameWindow(socketIn, socketOut);
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
     * Transition crossword canvas from start state to choose state
     * @param canvas crossword canvas to modify
     * @param playerId id of the player/client
     * @param socketIn buffered reader to read input from server
     * @param socketOut print writer to write to the server
     */
    private static void transitionStartState(CrosswordCanvas canvas, String playerId, BufferedReader socketIn, PrintWriter socketOut) {
        canvas.setState(State.CHOOSE);
        socketOut.println("LOGIN " + playerId);
        try {
            String response = socketIn.readLine();
            // list of matches RESPONSE_DELIM list of puzzles
            String[] matchesAndPuzzles = response.split(RESPONSE_DELIM);
            canvas.setPuzzleList(matchesAndPuzzles[0]);
            canvas.setMatchList(matchesAndPuzzles[1]);
        } catch (IOException ioe) {}
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
    private static void transitionChooseState(CrosswordCanvas canvas, String command, BufferedReader socketIn, PrintWriter socketOut) {
        canvas.setState(State.PLAY);
        socketOut.println(command);
        try {
            canvas.setPuzzle(socketIn.readLine());
        } catch (IOException ioe) {}
    }
    
    /**
     * Starter code to display a window with a CrosswordCanvas,
     * a text box to enter commands and an Enter button.
     */
    private static void launchGameWindow(BufferedReader socketIn, PrintWriter socketOut) {
        CrosswordCanvas canvas = new CrosswordCanvas("");
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
            switch (canvas.getState()) {
            case START:
                {
                    transitionStartState(canvas, text, socketIn, socketOut);
                    break;
                }
            case CHOOSE:
                {
                    transitionChooseState(canvas, text, socketIn, socketOut);
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
