/* Copyright (c) 2019 MIT 6.031 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package crossword;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JComponent;

/**
 * This component allows you to draw a crossword puzzle. Right now it just has
 * some helper methods to draw cells and add text in them, and some demo code
 * to show you how they are used. You can use this code as a starting point when
 * you develop your own UI.
 * @author asolar
 */
class CrosswordCanvas extends JComponent {

    /**
     * Horizontal offset from corner for first cell.
     */
    private final int originX = 100;
    /**
     * Vertical offset from corner for first cell.
     */
    private final int originY = 90;
    /**
     * Size of each cell in crossword. Use this to rescale your crossword to have
     * larger or smaller cells.
     */
    private final int delta = 30;

    /**
     * Font for letters in the crossword.
     */
    private final Font mainFont = new Font("Arial", Font.PLAIN, delta * 4 / 5);

    /**
     * Font for small indices used to indicate an ID in the crossword.
     */
    private final Font indexFont = new Font("Arial", Font.PLAIN, delta / 3);

    /**
     * Font for small indices used to indicate an ID in the crossword.
     */
    private final Font textFont = new Font("Arial", Font.PLAIN, 16);
    
    // Delimiters for string responses
    public static final String ENTRY_DELIM = "~";
    public static final String WORD_DELIM = "`";
    public static final String RESPONSE_DELIM = ";";
    
    // Abstraction function:
    //  AF(originX, originY, delta, mainFont, indexFont, textFont, 
    //     playerID, puzzle, state, puzzleList, matchList, currentPuzzle, wordToLine) = 
    //          canvas representing the crossword puzzle with with puzzle cells of size delta
    //          starting at originX, originY and text using mainFont, indexFont, and textFont.
    //          playerID represents the unique identifier for the client drawing on the canvas.
    //          puzzle represents the blank crossword puzzle, where puzzle entries are separated 
    //          by ENTRY_DELIM and puzzle parts of each entry are separated by WORD_DELIM.
    //          The canvas can be in one of five states representing the state of the puzzle:
    //          START, CHOOSE, WAIT, PLAY, and SHOW_SCORE. The state of the canvas influences
    //          what is drawn on the canvas. puzzleList represents the list of available consistent
    //          puzzles that the player can create a match from, whereas matchList is the list of
    //          matches that are waiting for another player to join. currentPuzzle represents
    //          all guesses, confirmed words, and scores of all players in a match, with different 
    //          background colors highlighting each player's guesses on the crossword puzzle.
    //          currentPuzzle contains entries for each player and their score, separated by
    //          WORD_DELIM. Each player-score entry is separated by ENTRY_DELIM. The scores and
    //          the list of guessed/confirmed words are separated by RESPONSE_DELIM, and the entry
    //          for each guessed/confirmed word is separated by ENTRY_DELIM. Each word entry has
    //          information about the word separated by WORD_DELIM. wordToLine maps each word id to
    //          the line that its hint is drawn on in the PLAY state.
    // Representation invariant:
    //  originX >= 0, 
    //  originY >= 0, 
    //  delta >= 0,
    //  Set.of(State.START, State.CHOOSE, State.WAIT, State.PLAY, State.SHOW_SCORE).contains(state)
    // Safety from rep exposure:
    //  All fields are private and immutable
    //  The client can only modify the rep through mutator methods.
    // Thread safety argument:
    //  This uses the monitor pattern
    
    private String playerID = "";
    private String puzzle;
    private State state;
    private String previousResponse = "No previous response";
    private String puzzleList = "";
    private String matchList = "";
    private String currentPuzzle = "";
    private String overallScore = "";
    private final Map<Integer, SimpleImmutableEntry<Integer, String>> wordToLine = new HashMap<>();
    
    /**
     * @param puzzle string representation of the crossword puzzle
     */
    public CrosswordCanvas(String puzzle) {
        this.puzzle = puzzle;
        this.state = State.START;
        
        checkRep();
    }
    
    /**
     * Asserts that the RI is true
     */
    private void checkRep() {
        assert originX >= 0;
        assert originY >= 0;
        assert delta >= 0;
        assert Set.of(State.START, State.CHOOSE, State.WAIT, State.PLAY, State.SHOW_SCORE).contains(state);
    }
    
    /**
     * @return the state of the crossword puzzle
     */
    public synchronized State getState() {
        return state;
    }
    
    /**
     * Resets the player's ID
     */
    public synchronized void clearPlayerInfo() {
        playerID = "";
    }
    
    /**
     * Clears the current puzzle info from the canvas
     */
    public synchronized void clearPuzzleInfo() {
        puzzle = "";
        previousResponse = "No previous response";
        puzzleList = "";
        matchList = "";
        currentPuzzle = "";
    }
    
    /**
     * @param state the new state of the crossword puzzle
     */
    public synchronized void setState(State state) {
        this.state = state;
    }
    
    /**
     * @param playerID id of the player
     */
    public synchronized void setPlayerID(String playerID) {
        this.playerID = playerID;
    }
    
    /**
     * @param puzzle the new string representation of the crossword puzzle
     */
    public synchronized void setPuzzle(String puzzle) {
        this.puzzle = puzzle;
    }
    
    /**
     * @param puzzleList the list of valid puzzle names
     */
    public synchronized void setPuzzleList(String puzzleList) {
        this.puzzleList = puzzleList;
    }
    
    /**
     * @param matchList the list of valid match ids
     */
    public synchronized void setMatchList(String matchList) {
        this.matchList = matchList;
    }
    
    /**
     * @param previousResponse the previous response from the server
     */
    public synchronized void setPreviousResponse(String previousResponse) {
        this.previousResponse = previousResponse;
    }
    
    /**
     * @return current puzzles with guesses
     */
    public synchronized String getCurrentPuzzle() {
        return currentPuzzle;
    }
    
    /**
     * @param currentPuzzle the current puzzle from the server
     */
    public synchronized void setCurrentPuzzle(String currentPuzzle) {
        this.currentPuzzle = currentPuzzle;
    }
    
    /**
     * @param overallScore the overall scores of both players from the server
     */
    public synchronized void setOverallScore(String overallScore) {
        this.overallScore = overallScore;
    }
    
    /**
     * Draw a cell at position (row, col) in a crossword.
     * @param row Row where the cell is to be placed.
     * @param col Column where the cell is to be placed.
     * @param g Graphics environment used to draw the cell.
     * @param id id of the owner of the cell.
     */
    private synchronized void drawCell(int row, int col, Graphics g, String id, boolean confirmed) {
        // Before changing the color it is a good idea to record what the old color
        // was.
        Color oldColor = g.getColor();
        
        if (id.length() == 0) {
            g.setColor(Color.YELLOW);
        } else if (confirmed) {
            if (id.equals(playerID)) {
                g.setColor(Color.ORANGE);
            } else {
                g.setColor(Color.CYAN);
            }
        } else if (id.equals(playerID)) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.GREEN);
        }
        g.fillRect(originX + col * delta,
                originY + row * delta, delta, delta);
        // After drawing the cell you can return to the previous color.
        g.setColor(oldColor);
        
        g.drawRect(originX + col * delta,
                originY + row * delta, delta, delta);
    }
    
    /**
     * Place player id at the top-left corner of the canvas.
     * @param g Graphics environment to use.
     */
    private synchronized void drawPlayerID(Graphics g) {
        g.drawString("Player ID: " + playerID, 0, 30);
    }
    
    /**
     * Place print colors at the top-left corner of the canvas, 
     *  underneath the player id
     * @param g
     */
    private synchronized void drawPlayerColors(Graphics g) {
        g.drawString("Red:Me|Green:Other|Orange:Confirmed", 0, 60);
    }
    
    /**
     * Place error message from entering a command in the middle, underneath 
     * the text box
     * @param g Graphics environment to use
     */
    private synchronized void drawErrorMessage(Graphics g) {
        if (previousResponse.length() > 0 && previousResponse.charAt(0) == 'I') {
            g.drawString("Error: " + previousResponse.substring(1), 310, 60);
        } 
    }

    /**
     * Place a letter inside the cell at position (row, col) in a crossword.
     * @param letter Letter to add to the cell.
     * @param row Row position of the cell.
     * @param col Column position of the cell.
     * @param g Graphics environment to use.
     */
    private synchronized void letterInCell(String letter, int row, int col, Graphics g) {
        g.setFont(mainFont);
        FontMetrics fm = g.getFontMetrics();
        g.drawString(letter, originX + col * delta + delta / 6,
                             originY + row * delta + fm.getAscent() + delta / 10);
    }

    /**
     * Add a vertical ID for the cell at position (row, col).
     * @param id ID to add to the position.
     * @param row Row position of the cell.
     * @param col Column position of the cell.
     * @param g Graphics environment to use.
     */
    private synchronized void verticalId(String id, int row, int col, Graphics g) {
        g.setFont(indexFont);
        g.drawString(id, originX + col * delta + delta / 8,
                         originY + row * delta - delta / 15);
    }

    /**
     * Add a horizontal ID for the cell at position (row, col).
     * @param id ID to add to the position.
     * @param row Row position of the cell.
     * @param col Column position of the cell.
     * @param g Graphics environment to use.
     */
    private synchronized void horizontalId(String id, int row, int col, Graphics g) {
        g.setFont(indexFont);
        FontMetrics fm = g.getFontMetrics();
        int maxwidth = fm.charWidth('0') * id.length();
        g.drawString(id, originX + col * delta - maxwidth - delta / 8,
                         originY + row * delta + fm.getAscent() + delta / 15);
    }

    // The three methods that follow are meant to show you one approach to writing
    // in your canvas. They are meant to give you a good idea of how text output and
    // formatting work, but you are encouraged to develop your own approach to using
    // style and placement to convey information about the state of the game.

    private int line = 0;
    
    // The Graphics interface allows you to place text anywhere in the component,
    // but it is useful to have a line-based abstraction to be able to just print
    // consecutive lines of text.
    // We use a line counter to compute the position where the next line of code is
    // written, but the line needs to be reset every time you paint, otherwise the
    // text will keep moving down.
    private synchronized void resetLine() {
        line = 0;
    }

    // This code illustrates how to write a single line of text with a particular
    // color.
    private synchronized void println(String s, Graphics g) {
        g.setFont(textFont);
        FontMetrics fm = g.getFontMetrics();
        // Before changing the color it is a good idea to record what the old color
        // was.
        Color oldColor = g.getColor();
        g.setColor(new Color(100, 0, 0));
        g.drawString(s, originX + 500, originY + line * fm.getAscent() * 6 / 5);
        // After writing the text you can return to the previous color.
        g.setColor(oldColor);
        ++line;
    }

    // This code shows one approach for fancier formatting by changing the
    // background color of the line of text.
    private synchronized void printlnFancy(String s, Graphics g, Color highlight, int lineNumber) {
        final int oldLine = line;
        line = lineNumber;
        
        final Font fancierFont = new Font("Arial", Font.BOLD, 16);
        g.setFont(fancierFont);
        FontMetrics fm = g.getFontMetrics();
        int lineHeight = fm.getAscent() * 6 / 5;
        int xpos = originX + 500;
        int ypos = originY + line * lineHeight;

        // Before changing the color it is a good idea to record what the old color
        // was.
        Color oldColor = g.getColor();

        g.setColor(highlight);
        g.fillRect(xpos, ypos - fm.getAscent(), fm.stringWidth(s), lineHeight);
        g.setColor(oldColor);
        g.drawString(s, xpos, ypos);
        line = oldLine;
    }
    
    // This code shows one approach for fancier formatting by changing the
    // background color and font type of the line of text.
    private synchronized void printlnFancier(String s, Graphics g, Color highlight) {
        final Font fancierFont = new Font("Arial", Font.BOLD, 16);
        g.setFont(fancierFont);
        FontMetrics fm = g.getFontMetrics();
        int lineHeight = fm.getAscent() * 6 / 5;
        int xpos = originX + 500;
        int ypos = originY + line * lineHeight;

        // Before changing the color it is a good idea to record what the old color
        // was.
        Color oldColor = g.getColor();

        g.setColor(highlight);
        g.fillRect(xpos, ypos - fm.getAscent(), fm.stringWidth(s), lineHeight);
        g.setColor(oldColor);
        g.drawString(s, xpos, ypos);
        ++line;
    }
    
    /**
     * Prints a crossword puzzle with no words filled in and
     * hints for each word divided into horizontal words and
     * vertical words. 
     * 
     * @param g the graphics object to modify
     */
    private synchronized void printPuzzle(Graphics g) {
        int across = 0;
        int down = 0;
        List<String> acrossHints = new ArrayList<>();
        List<String> downHints = new ArrayList<>();
        
        // sort entries by word id
        String[] unsortedEntries = puzzle.split(ENTRY_DELIM);
        List<String> entries = new ArrayList<>(unsortedEntries.length);
        for (String entry: unsortedEntries) {
            entries.add(Integer.parseInt(entry.substring(0, entry.indexOf(WORD_DELIM))), entry);
        }
                
        for (String entry: entries) {
            // info has the following format: 
            //     id WORD_DELIM length WORD_DELIM hint WORD_DELIM 
            //     orientation WORD_DELIM row WORD_DELIM col ENTRY_DELIM
            String[] info = entry.split(WORD_DELIM);
            int id = Integer.parseInt(info[0]);
            int length = Integer.parseInt(info[1]);
            int row = Integer.parseInt(info[4]);
            int col = Integer.parseInt(info[5]);
            
            // draw cells for across and record hint
            if (info[3].equals("ACROSS")) {
                horizontalId(Integer.toString(id), row, col, g);
                acrossHints.add(id + ". " + info[2] + "\n");
                
                for (int i = 0; i < length; i++) {
                    drawCell(row, col + i, g, "", false);
                }
                across++;
            } else { // draw cells for down and record hint
                verticalId(Integer.toString(id), row, col, g);
                downHints.add(id + ". " + info[2] + "\n");
                
                for (int i = 0; i < length; i++) {
                    drawCell(row + i, col, g, "", false);
                }
                down++;
            }
        }
        
        // print hints for across and down words
        println("", g);
        
        int wordID;
        if (across > 0) {
            println("Across\n", g);
            for (String hint: acrossHints) {
                wordID = Integer.parseInt(hint.substring(0, hint.indexOf('.')));
                wordToLine.put(wordID, new SimpleImmutableEntry<>(line, hint));
                println(hint, g);
            }
        }
        
        println("\n", g);
        if (down > 0) {
            println("Down\n", g);
            for (String hint: downHints) {
                wordID = Integer.parseInt(hint.substring(0, hint.indexOf('.')));
                wordToLine.put(wordID, new SimpleImmutableEntry<>(line, hint));
                println(hint, g);
            }
        }
    }

    /**
     * Simple demo code just to illustrate how to paint cells in a crossword puzzle.
     * The paint method is called every time the JComponent is refreshed, or every
     * time the repaint method of this class is called.
     * We added some state just to allow you to see when the class gets repainted,
     * although in general you wouldn't want to be mutating state inside the paint
     * method.
     */
    @Override
    public synchronized void paint(Graphics g) {
        resetLine();
        println("Previous response: " + previousResponse, g);
        drawPlayerID(g);
        drawPlayerColors(g);
        // Draws the error message (i.e. invalid ID) if there is one
        drawErrorMessage(g);
        switch (state) {
        case START:
            {
                println("Please type in a valid ID into the text box and hit the Enter button.", g);
                println("Valid ID requirements: alphanumeric, at least one character long", g);
                break;
            }
        case CHOOSE:
            {
                println("Here are the commands that you can enter in the box:", g);
                println("    To play in an available match: PLAY Match_ID", g);
                println("    To create a new match: NEW Match_ID Puzzle_ID \"Description\"", g);
                println("    To log out: EXIT", g);
                
                println("", g);
                println("Available matches (Match_ID: Description):", g);
                if (matchList.length() > 0) {
                    String[] matches = matchList.split(ENTRY_DELIM);
                    
                    for (String match : matches) {
                        String[] matchEntry = match.split(WORD_DELIM);
                        println("    " + matchEntry[0] + ": " + matchEntry[1], g);
                    }
                }
                println("", g);
                println("Available puzzles:", g);
                String[] entries = puzzleList.split(ENTRY_DELIM);
                for (String entry : entries) {
                    println("    " + entry, g);
                }
                break;
            }
        case WAIT:
            {
                println("You are currently waiting for another player to join your match.", g);
                println("    To exit to match selection, enter: EXIT", g);
                println("    No other command may be entered.", g);
                break;
            }
        case PLAY:
            {
                println("Here are the commands that you can enter in the box:", g);
                println("    To try a word: TRY id word", g);
                println("    To challenge a word: CHALLENGE id word", g);
                println("    To forfeit: EXIT", g);
                
                printPuzzle(g);
                println("", g);
                
                if (currentPuzzle.length() > 0) {
                    List<String> playerIDs = new ArrayList<>();
                    String[] currentPuzzleState = currentPuzzle.split(RESPONSE_DELIM);
                    for (String currentPlayerState : currentPuzzleState[0].split(ENTRY_DELIM)) {
                        String[] playerPoints = currentPlayerState.split(WORD_DELIM);
                        println(playerPoints[0] + "'s Challenge Points: " + playerPoints[1], g);
                        playerIDs.add(playerPoints[0]);
                    }
                    
                    List<String> myWords = new ArrayList<>();
                    List<String> otherWords = new ArrayList<>();
                    
                    if (currentPuzzleState.length > 1) {
                        for (String wordEntry : currentPuzzleState[1].split(ENTRY_DELIM)) {
                            String[] wordEntered = wordEntry.split(WORD_DELIM);
                            int wordID = Integer.parseInt(wordEntered[2]);
                            int row = Integer.parseInt(wordEntered[5]);
                            int col = Integer.parseInt(wordEntered[6]);
                            boolean confirmed = wordEntered[1].equals("T");
                            String player = wordEntered[0];  
                            Color highlight;
                                                        
                            if (confirmed && player.equals(playerID)) { 
                                // player's confirmed word
                                highlight = Color.ORANGE;
                            } else if (confirmed) { 
                                // other player's confirmed word
                                highlight = Color.CYAN;
                            } else if (player.equals(playerID)) { 
                                // player's unconfirmed word
                                highlight = Color.RED;
                            } else { 
                                // other player's unconfirmed word
                                highlight = Color.GREEN;
                            }
                            
                            printlnFancy(wordToLine.get(wordID).getValue(), g, highlight, wordToLine.get(wordID).getKey());

                            // draw words entered
                            if (wordEntered[4].equals("ACROSS")) {
                                for (int i = 0; i < wordEntered[3].length(); i++) {
                                    drawCell(row, col + i, g, player, confirmed);
                                    letterInCell(wordEntered[3].substring(i, i+1).toUpperCase(), row, col + i, g);
                                }
                            } else {
                                for (int i = 0; i < wordEntered[3].length(); i++) {
                                    drawCell(row + i, col, g, player, confirmed);
                                    letterInCell(wordEntered[3].substring(i, i+1).toUpperCase(), row + i, col, g);
                                }
                            }

                            // separating each player's confirmed words
                            if (wordEntered[1].equals("T") && wordEntered[0].equals(playerID)) {
                                myWords.add(wordEntered[3]);
                            } else if (wordEntered[1].equals("T")) {
                                otherWords.add(wordEntered[3]);
                            }
                        }
                        
                        String otherPlayerID = (playerID.equals(playerIDs.get(0))) ? playerIDs.get(1) : playerIDs.get(0);

                        printlnFancier(playerID + "'s confirmed words: " + myWords.toString(), g, Color.ORANGE);
                        printlnFancier(otherPlayerID + "'s confirmed words: " + otherWords.toString(), g, Color.CYAN);
                    }
                }                
                break;
            }
        case SHOW_SCORE:
            {
                println("Here are the commands that you can enter in the box:", g);
                println("    To return to match selection: NEW MATCH", g);
                println("    To log out: EXIT", g);
                
                println("", g);
                // print scores
                if (overallScore.length() == 0) {
                    break;
                }
                String[] playerScores = overallScore.split(ENTRY_DELIM);
                for (String playerScore : playerScores) {
                    String[] playerPoints = playerScore.split(WORD_DELIM);
                    println(playerPoints[0] + "'s Challenge Points: " + playerPoints[1], g);
                    println(playerPoints[0] + "'s Total Points: " + playerPoints[2], g);
                }
                break;
            }
        default:
            {
                throw new AssertionError("should never get here");
            }
        }
    }
}
