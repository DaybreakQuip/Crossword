/* Copyright (c) 2019 MIT 6.031 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package crossword;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.BreakIterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
/**
 * TODO
 */
public class Client {

    private static final int CANVAS_WIDTH = 1200;
    private static final int CANVAS_HEIGHT = 900;

    /**
     * Start a Crossword Extravaganza client.
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
            boolean showRaw = false;
            launchGameWindow();
            while ( ! socket.isClosed()) {
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
                    playGame(socketIn, System.out, showRaw);
                }
            }
            System.out.println("connection closed");
        }
    }
    private static void playGame(BufferedReader in, PrintStream out, boolean showRaw) throws IOException {
    }
    /**
     * Starter code to display a window with a CrosswordCanvas,
     * a text box to enter commands and an Enter button.
     */
    private static void launchGameWindow() {

        CrosswordCanvas canvas = new CrosswordCanvas();
        canvas.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);

        JButton enterButton = new JButton("Enter");
        enterButton.addActionListener((event) -> {
            // This code executes every time the user presses the Enter
            // button. Recall from reading 24 that this code runs on the
            // Event Dispatch Thread, which is different from the main
            // thread.
            canvas.repaint();
        });
        enterButton.setSize(10, 10);

        JTextField textbox = new JTextField(30);
        textbox.setFont(new Font("Arial", Font.BOLD, 20));

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
