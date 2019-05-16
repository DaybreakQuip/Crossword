package crossword;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import edu.mit.eecs.parserlib.UnableToParseException;

public class TextServerTest {
    // Partitions:
    //     Number of connecting clients = 1, > 1
    //     Number of puzzles = 0, 1, > 1
    //     Number of ongoing matches = 0, 1, > 1
    //     Number of matches waiting to start = 0, 1, > 1
    //     Number of clients (players) waiting for a match to start = 0, 1, > 1
    //     Number of clients (players) playing in a match = 0, 1, > 1
    //     Includes commands: WATCH, WAIT, WAIT_PLAY, LOGIN, NEW_MATCH, PLAY,
    //          NEW, LOGOUT, EXIT_WAIT, EXIT_PLAY, TRY, CHALLENGE

    private static final String LOCALHOST = "127.0.0.1";
    
    private static final int MAX_CONNECTION_ATTEMPTS = 5;
    
    /* Start server on its own thread. */
    private static Thread startServer(final TextServer server) {
        Thread thread = new Thread(() ->  {
            try {
                server.serve();
            } catch (IOException ioe) {
                throw new RuntimeException("serve() threw IOException", ioe);
            }
        });
        thread.start();
        return thread;
    }
    
    /* Connect to server with retries on failure. */
    private static Socket connectToServer(final Thread serverThread, final TextServer server) throws IOException {
        final int port = server.port();
        assertTrue(port > 0, "server.port() returned " + port);
        for (int attempt = 0; attempt < MAX_CONNECTION_ATTEMPTS; attempt++) {
            final int attemptMultiplier = 10;
            try { Thread.sleep(attempt * attemptMultiplier); } catch (InterruptedException ie) { }
            if ( ! serverThread.isAlive()) {
                throw new IOException("server thread no longer running");
            }
            try {
                final Socket socket = new Socket(LOCALHOST, port);
                final int timeoutMultiplier = 3;
                final int timeout = 1000;
                socket.setSoTimeout(timeout * timeoutMultiplier);
                return socket;
            } catch (ConnectException ce) {
                // may try again
            }
        }
        throw new IOException("unable to connect after " + MAX_CONNECTION_ATTEMPTS + " attempts");
    }
    
    /**
     * Tests that assertions are enabled
     */
    @Test
    public void testAssertionsEnabled() {
        assertThrows(AssertionError.class, () -> { assert false; },
                "make sure assertions are enabled with VM argument '-ea'");
    }
    
    // asserts the server responds as expected to commands
    private static void assertValidServerResponse(String directory, 
            List<String> commands, List<String> responses) throws IOException, URISyntaxException, UnableToParseException{
        Game game = Game.parseGameFromFiles(directory);
        final TextServer server = new TextServer(game, 0);
        final Thread thread = startServer(server);
        final Socket socket = connectToServer(thread, server);
        
        final BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), UTF_8));
        final PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), UTF_8), true);
        
        for (int i = 0; i < commands.size(); i++) {
            out.println(commands.get(i));
            assertEquals(responses.get(i), in.readLine());
        }
        
        socket.close();
        in.close();
        out.close();
    }
    
    // This test covers
    //     Number of connecting clients = 1
    //     Number of puzzles > 1
    //     Number of ongoing matches = 0
    //     Number of matches waiting to start = 0
    //     Number of clients (players) choosing a match = 1
    //     Number of clients (players) waiting for a match to start = 0
    //     Number of clients (players) playing in a match = 0
    //     Includes commands: LOGIN
    @Test
    public void testLoginValid() throws IOException, URISyntaxException, UnableToParseException {
        String directory = "puzzles/";
        List<String> commands = new ArrayList<String>();
        List<String> responses = new ArrayList<String>();
        commands.add("player420 LOGIN");
        responses.add("V6031 as3fb Easy as3fb SimpleComments as3fb Cross as3fb Easy1 as3fb Reactions as3fb Metamorphic cs2fd ");
        
        assertValidServerResponse(directory, commands, responses);
    }
}
