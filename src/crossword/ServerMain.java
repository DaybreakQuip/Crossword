/* Copyright (c) 2019 MIT 6.031 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package crossword;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

import edu.mit.eecs.parserlib.UnableToParseException;
/**
 * Game server runner.
 */
public class ServerMain {
    /**
     * Start a Crossword Extravaganza server.
     * 
     * Command to connect server: java -cp "bin;lib/parserlib.jar" crossword.ServerMain puzzles/ 4444
     * Command to Client: java -cp bin crossword.Client localhost 4444
     * @param args The command line arguments should include only the folder where
     *             the puzzles are located.
     * @throws IOException Server won't connect
     * @throws UnableToParseException Unable to Parse Exception
     */
    public static void main(String[] args) throws IOException, UnableToParseException {
        final Queue<String> arguments = new LinkedList<>(Arrays.asList(args));
        final int port;
        final String directory;
        final Game game;

        try {
            directory = arguments.remove();
            game = Game.parseGameFromFiles(directory);
        } catch (NoSuchElementException | NumberFormatException e) {
            throw new IllegalArgumentException("missing or invalid PORT", e);
        }
        try {
            port = Integer.parseInt(arguments.remove());
        } catch (NoSuchElementException | NumberFormatException e) {
            throw new IllegalArgumentException("missing or invalid PORT", e);
        }
        new TextServer(game, port).serve();
  
    }
   
}
