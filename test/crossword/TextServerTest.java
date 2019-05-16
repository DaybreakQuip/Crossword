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
    //MANUAL TEST:
    // Covers Playing a Game With Multiple Matches
    // Covers: length of player id = 1, transition = START->CHOOSE
    // 1. Create a new TextServer with one of these commands: 
    //      Windows: java -cp "bin;lib/parserlib.jar" crossword.ServerMain puzzles/ 4444
    //      Mac: java -cp bin:lib/parserlib.jar crossword.ServerMain puzzles/ 4444
    // 2. Open another terminal window and connect to the client with the command: 
    //      java -cp bin crossword.Client localhost 4444
    // 3. Make sure the crossword client (GUI) appears with a text box and enter button across the top
    //      and instructions on the right for how to login.
    // 4. Open another three more terminals and connect to the client. From here on out, clients will be labeled by their player name:
    //  "me", "you", "nome", and "noyou"
    // 5. me: Type me
    // 6. you Type you
    // 7. me: Type NEW 0 Reactions "Having Fun"; expects valid response
    // 8. you: Type PLAY 0; expects valid response
    // 9. me: Type TRY 0 PRODUCTS; expects valid response
    // 10. me: Type TRY 1 ababafffbaba; expects invalid
    // 11. you: TRY 0 products; expects invalid
    // 12. you: TRY 2 enzyems; expects valid
    // 13. me: TRY 12 doommgeys; expects valid
    // 14. me: CHALLENGE 2 enzymes; expects valid
    // 15. you: CHALLENGE 12 decreases; expects invalid
    // 16. me: TRY 10 increasing; expects invalid
    // 17. you: TRY 1 catalysts; expects valid
    // 18. me: CHALLENGE 1 catablots; expects valid
    // 19. you: TRY 13 activation; expects valid
    // 20. me: TRY 14 lechatelet; expects invalid
    // 21. me: TRY 14 le chatelier; expects invalid
    // 22. me: TRY 14 lechatelier; expects valid
    // 23. you: TRY 4 exothermic; expects valid
    // 24. me: CHALLENGE 4 octivation; expects valid
    // 25. you: CHALLENGE 14 lechateliee; expects valid
    // 26. you: CHALLENGE 15 balanced; expects invalid
    // 27. you: TRY 8 areas; expects valid
    // 28. me: TRY 5 aaaaaaaaaa; expects invalid
    // 29. me: TRY 5 aaaaaaaaaa; expects invalid
    // 30. you; TRY 6 reversible; expects valid
    // 31. me: TRY 11 equal; expects valid
    // 32. you: TRY 9 energy; expects valid
    // 33. you: CHALLENGE 5 reactants; expects valid
    // 34. you: TRY 7 dynamic; expects valid
    // 35. me: TRY 15 goesfaster; expects valid
    // 36. me: TRY 10 increased; expects valid
    // 37. me: TRY 12 decreased; expects valid
    // 38. me: TRY 3 collide; expects valid
    // Should display score of me: 0 challenge points, 8 points, you 1 challenge point, 9 points, you wins
    // 39. nome: nome
    // 40. noyou: noyou
    // 41. nome: TRY 0 MAT; expects valid
    // 42. noyou: TRY 0 CAT; expects invalid
    // 42. noyou: CHALLENGE 0 CAT; expects valid
    // 43. noyou: CHALLENGE 1 MAT; expects invalid
    // 44. nome: TRY 1 MAT; expects valid
    // 45. nome: 0 challenge points, 1 point, noyou: 2 challenge points, 3 points
    
 
    // Covers Playing a Match with forfeit
    // Covers: length of player id = 1, transition = START->CHOOSE
    // 1. Create a new TextServer with one of these commands: 
    //      Windows: java -cp "bin;lib/parserlib.jar" crossword.ServerMain puzzles/ 4444
    //      Mac: java -cp bin:lib/parserlib.jar crossword.ServerMain puzzles/ 4444
    // 2. Open another terminal window and connect to the client with the command: 
    //      java -cp bin crossword.Client localhost 4444
    // 3. Make sure the crossword client (GUI) appears with a text box and enter button across the top
    //      and instructions on the right for how to login.
    // 4. Open another three more terminals and connect to the client. From here on out, clients will be labeled by their player name:
    //  "me", "you", "nome", and "noyou"
    // 5. me: Type me
    // 6. you Type you
    // 7. me: Type NEW 0 Reactions "Having Fun"; expects valid response
    // 8. you: Type PLAY 0; expects valid response
    // 9. me: Type TRY 0 PRODUCTS; expects valid response
    // 10. me: Type TRY 1 ababafffbaba; expects invalid
    // 11. you: TRY 0 products; expects invalid
    // 12. you: TRY 2 enzyems; expects valid
    // 13. me: TRY 12 doommgeys; expects valid
    // 14. me: CHALLENGE 2 enzymes; expects valid
    // 15. you: CHALLENGE 12 decreases; expects invalid
    // 16. me: TRY 10 increasing; expects invalid
    // 17. you: TRY 1 catalysts; expects valid
    // 18. me: CHALLENGE 1 catablots; expects valid
    // 19. you: TRY 13 activation; expects valid
    // 20. me: TRY 14 lechatelet; expects invalid
    // 21. me: TRY 14 le chatelier; expects invalid
    // 22. me: TRY 14 lechatelier; expects valid
    // 23. you: TRY 4 exothermic; expects valid
    // 24. me: CHALLENGE 4 octivation; expects valid
    // 25. you: CHALLENGE 14 lechateliee; expects valid
    // 26. you: CHALLENGE 15 balanced; expects invalid
    // 27. you: TRY 8 areas; expects valid
    // 28. me: TRY 5 aaaaaaaaaa; expects invalid
    // 29. me: TRY 5 aaaaaaaaaa; expects invalid
    // 30. you; TRY 6 reversible; expects valid
    // 31. me: TRY 11 equal; expects valid
    // 32. me: EXIT_PLAY
    // 33. me: 0 challenge points, 6 points, you -1 challenge points and 4 points
    
    // This test covers
    //     Number of connecting clients > 1
    //     Number of puzzles > 1
    //     Number of ongoing matches = 0
    //     Number of matches waiting to start = 1
    //     Number of clients (players) choosing a match = 1
    //     Number of clients (players) waiting for a match to start = 1
    //     Number of clients (players) playing in a match = 0
    //     Includes commands: LOGIN, NEW

    @Test
    public void testMultipleLogin() throws IOException, URISyntaxException, UnableToParseException {
        String directory = "puzzles/";
        List<String> commands = new ArrayList<String>();
        List<String> responses = new ArrayList<String>();
        commands.add("player420 LOGIN");
        responses.add("V6031 as3fb Easy as3fb SimpleComments as3fb Cross as3fb Easy1 as3fb Reactions as3fb Metamorphic cs2fd ");
        commands.add("player360 LOGIN");
        responses.add("V6031 as3fb Easy as3fb SimpleComments as3fb Cross as3fb Easy1 as3fb Reactions as3fb Metamorphic cs2fd ");
        commands.add("player420 NEW 0 Easy \"easy puzzle\"");
        responses.add("V");
        assertValidServerResponse(directory, commands, responses);
    }
    
    // This test covers
    //     Number of connecting clients > 1
    //     Number of puzzles > 1
    //     Number of ongoing matches = 0
    //     Number of matches waiting to start > 1
    //     Number of clients (players) choosing a match = 0
    //     Number of clients (players) waiting for a match to start > 1
    //     Number of clients (players) playing in a match = 0
    //     Includes commands: LOGIN, NEW, LOGOUT

    @Test
    public void testMultipleLogout() throws IOException, URISyntaxException, UnableToParseException {
        String directory = "puzzles/";
        List<String> commands = new ArrayList<String>();
        List<String> responses = new ArrayList<String>();
        commands.add("player420 LOGIN");
        responses.add("V6031 as3fb Easy as3fb SimpleComments as3fb Cross as3fb Easy1 as3fb Reactions as3fb Metamorphic cs2fd ");
        commands.add("player360 LOGIN");
        responses.add("V6031 as3fb Easy as3fb SimpleComments as3fb Cross as3fb Easy1 as3fb Reactions as3fb Metamorphic cs2fd ");
        commands.add("player200 LOGIN");
        responses.add("V6031 as3fb Easy as3fb SimpleComments as3fb Cross as3fb Easy1 as3fb Reactions as3fb Metamorphic cs2fd ");
        commands.add("player100 LOGIN");
        responses.add("V6031 as3fb Easy as3fb SimpleComments as3fb Cross as3fb Easy1 as3fb Reactions as3fb Metamorphic cs2fd ");
        commands.add("player420 NEW 0 Easy \"easy puzzle\"");
        responses.add("V");
        commands.add("player360 NEW 1 Cross \"cross puzzle\"");
        responses.add("V");
        commands.add("player200 LOGOUT");
        responses.add("V");
        commands.add("player100 LOGOUT");
        responses.add("V");
        assertValidServerResponse(directory, commands, responses);
    }
}
