/* Copyright (c) 2017-2018 MIT 6.031 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package crossword;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import edu.mit.eecs.parserlib.ParseTree;
import edu.mit.eecs.parserlib.Parser;
import edu.mit.eecs.parserlib.UnableToParseException;

public class PuzzleParser {
    
    /**
     * Main method. Parses and then reprints an example puzzle.
     * 
     * @param args command line arguments, not used
     * @throws UnableToParseException if example expression can't be parsed
     * @throws IOException if .puzzle file cannot be opened
     */
    public static void main(final String[] args) throws UnableToParseException, IOException {
        // TODO: Open a file here for the example puzzle
        String filename = "simple.puzzle";
        BufferedReader reader = new BufferedReader(new FileReader("puzzles/" + filename));
        StringBuilder inputBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            inputBuilder.append(line + "\n");
        }
        
        reader.close();
        String input = inputBuilder.toString();
        System.out.println("The input is: \n" + input);
        final Puzzle puzzle = PuzzleParser.parse(input);
        
        System.out.println(puzzle);
    }
    
    // the nonterminals of the grammar
    private static enum PuzzleGrammar {
        FILE, NAME, DESCRIPTION, ENTRY, COMMENT, WORDNAME, CLUE, DIRECTION, ROW, COL, STRING, STRINGINDENT, INT, WHITESPACE, NEWLINE
    }
    
    private static Parser<PuzzleGrammar> parser = makeParser();
    
    /**
     * Compile the grammar into a parser.
     * 
     * @return parser for the grammar
     * @throws RuntimeException if grammar file can't be read or has syntax errors
     */
    private static Parser<PuzzleGrammar> makeParser() {
        try {
            // read the grammar as a file, relative to the project root.
            final File grammarFile = new File("src/crossword/Puzzle.g");
            return Parser.compile(grammarFile, PuzzleGrammar.FILE);
            
        // Parser.compile() throws two checked exceptions.
        // Translate these checked exceptions into unchecked RuntimeExceptions,
        // because these failures indicate internal bugs rather than client errors
        } catch (IOException e) {
            throw new RuntimeException("can't read the grammar file", e);
        } catch (UnableToParseException e) {
            throw new RuntimeException("the grammar has a syntax error", e);
        }
    }

    /**
     * Parse a string into an puzzle.
     * 
     * @param string string to parse
     * @return Puzzle parsed from the string
     * @throws UnableToParseException if the string doesn't match the Puzzle grammar
     */
    public static Puzzle parse(final String string) throws UnableToParseException {
        // parse the example into a parse tree
        final ParseTree<PuzzleGrammar> parseTree = parser.parse(string);

        // display the parse tree in various ways, for debugging only
        // System.out.println("parse tree " + parseTree);
        // Visualizer.showInBrowser(parseTree);

        // make a puzzle from the parse tree
        final Puzzle puzzle = makePuzzle(parseTree);
        // System.out.println("Puzzle: " + expression);
        
        return puzzle;
    }
    
    /**
     * Convert a parse tree into an puzzle object.
     * 
     * @param parseTree constructed according to the grammar in Puzzle.g
     * @return puzzle object corresponding to parseTree
     */
    private static Puzzle makePuzzle(final ParseTree<PuzzleGrammar> parseTree) {
        switch (parseTree.name()) {
        case FILE: // ">>" name description "\n" entry*
            // TODO: Change this
            return Puzzle.makeDummyPuzzle();
        default:
            System.out.println(parseTree.name());
            throw new AssertionError("should never get here");
        }

    }

}
