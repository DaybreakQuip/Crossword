/* Copyright (c) 2017-2018 MIT 6.031 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package crossword;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        String filename = "inconsistent.puzzle";
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
        
        System.out.println("The parsed puzzle is: \n" + puzzle);
    }
    
    // the nonterminals of the grammar
    private static enum PuzzleGrammar {
        FILE, COMMENT, NAME, DESCRIPTION, ENTRY, WORDNAME, CLUE, DIRECTION, ROW, COL, STRING, STRINGINDENT, INT, WHITESPACE
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
     * Returns a new Puzzle by parsing a file
     * @param filename the name of the .puzzle file
     * @return a new Puzzle parsed from the file
     * @throws IOException if there is a problem with reading the file
     * @throws UnableToParseException if there is a problem with parsing
     */
    public static Puzzle parseFromFile(String filename) throws IOException, UnableToParseException{
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        StringBuilder inputBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            inputBuilder.append(line + "\n");
        }
        
        reader.close();
        String input = inputBuilder.toString();
        return PuzzleParser.parse(input);
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
        // System.out.println("Puzzle: " + puzzle);
        
        return puzzle;
    }
    
    /**
     * Convert a parse tree into an puzzle object.
     * 
     * @param parseTree constructed according to the grammar in Puzzle.g
     * @return puzzle corresponding to parseTree
     */
    private static Puzzle makePuzzle(final ParseTree<PuzzleGrammar> parseTree) {
        switch (parseTree.name()) {
        case FILE: // ">>" name description entry*
            // Create a puzzle from this file
            final List<ParseTree<PuzzleGrammar>> children = parseTree.children();
            // Initialize variables for the puzzle
            String name = "";
            String description = "";
            List<PuzzleEntry> entries = new ArrayList<>();
            // Populate the puzzle variables by parsing the children of the file
            for (ParseTree<PuzzleGrammar> child : children) {
                /* Print for debugging use only
                 System.out.println("NAME:" + child.name());
                 System.out.println("STRING:" + child.text());
                 System.out.println("----------");
                */
                switch (child.name()) {
                    case NAME: // '"' [^"\r\n\t\\]* '"'
                        String nameWithQuotes = child.text();
                        name = nameWithQuotes.substring(1, nameWithQuotes.length()-2);
                        break;
                    case DESCRIPTION: // string "\n"
                        String descWithNewline = child.text();
                        description = descWithNewline.substring(1, descWithNewline.length()-2);
                        break;
                    case ENTRY: // "("  wordname ","  clue "," direction "," row "," col ")"
                        entries.add(makeEntry(child));
                    case COMMENT:
                        break;
                    default:
                        System.out.println(parseTree.name());
                        throw new AssertionError("File has unexpected child");
                }
            }
            // Create and return the new puzzle we just parsed
            return new Puzzle(name, description, entries);
        default:
            System.out.println(parseTree.name());
            throw new AssertionError("makePuzzle: should never get here");
        }
    }
    
    /**
     * Convert a parse tree into a PuzzleEntry object.
     * 
     * @param parseTree constructed according to the grammar in Puzzle.g
     * @return puzzle entry corresponding to parseTree
     */
    private static PuzzleEntry makeEntry(final ParseTree<PuzzleGrammar> parseTree) {
        // entry ::= "("  wordname ","  clue "," direction "," row "," col ")"
        List<ParseTree<PuzzleGrammar>> children = parseTree.children();
        // indices for different parts of entries
        final int wordIndex = 0;
        final int clueIndex = 1;
        final int orientationIndex = 2;
        final int rowIndex = 3;
        final int colIndex = 4;
        // Extract word, clue, orientation, row, and col from the parseTree
        String word = children.get(wordIndex).text();
        String clueWithQuotes = children.get(clueIndex).text();
        String clue = clueWithQuotes.substring(1, clueWithQuotes.length()-1);
        Orientation orientation = Orientation.valueOf(children.get(orientationIndex).text());
        int row = Integer.valueOf(children.get(rowIndex).text());
        int col = Integer.valueOf(children.get(colIndex).text());
        Point point = new Point(row, col);
        
        return new PuzzleEntry(word, clue, orientation, point);
    }

}
