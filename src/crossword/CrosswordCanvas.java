/* Copyright (c) 2019 MIT 6.031 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package crossword;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.List;
import java.util.ArrayList;

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
    private final int originY = 60;
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

    private final String puzzle;
    private static final String ENTRY_DELIM = "~";
    private static final String WORD_DELIM = "`";
    
    /**
     * 
     * @param puzzle TODO
     */
    public CrosswordCanvas(String puzzle) {
        this.puzzle = puzzle;
    }
    
    /**
     * Draw a cell at position (row, col) in a crossword.
     * @param row Row where the cell is to be placed.
     * @param col Column where the cell is to be placed.
     * @param g Graphics environment used to draw the cell.
     */
    private void drawCell(int row, int col, Graphics g) {
        g.drawRect(originX + col * delta,
                   originY + row * delta, delta, delta);
    }

    /**
     * Place a letter inside the cell at position (row, col) in a crossword.
     * @param letter Letter to add to the cell.
     * @param row Row position of the cell.
     * @param col Column position of the cell.
     * @param g Graphics environment to use.
     */
    private void letterInCell(String letter, int row, int col, Graphics g) {
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
    private void verticalId(String id, int row, int col, Graphics g) {
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
    private void horizontalId(String id, int row, int col, Graphics g) {
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
    private void resetLine() {
        line = 0;
    }

    // This code illustrates how to write a single line of text with a particular
    // color.
    private void println(String s, Graphics g) {
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
    private void printlnFancy(String s, Graphics g) {

        g.setFont(textFont);
        FontMetrics fm = g.getFontMetrics();
        int lineHeight = fm.getAscent() * 6 / 5;
        int xpos = originX + 500;
        int ypos = originY + line * lineHeight;

        // Before changing the color it is a good idea to record what the old color
        // was.
        Color oldColor = g.getColor();

        g.setColor(new Color(0, 0, 0));
        g.fillRect(xpos, ypos - fm.getAscent(), fm.stringWidth(s), lineHeight);
        g.setColor(new Color(200, 200, 0));
        g.drawString(s, xpos, ypos);
        // After writing the text you can return to the previous color.
        g.setColor(oldColor);
        ++line;
    }
    
    //private List<String> acrossAns = List.of("star", "kettle", "bee", "treasure", "troll");
    //private List<String> downAns = List.of("market", "extra", "loss");
    
    private void printPuzzle(Graphics g) {
        int across = 0;
        int down = 0;
        List<String> acrossHints = new ArrayList<>();
        List<String> downHints = new ArrayList<>();
        
        System.out.println(puzzle);
        
        String[] entries = puzzle.split(ENTRY_DELIM);
        
        System.out.println("after split entries");
        for (String entry: entries) {
            System.out.println(entry);
            // info has the following format: 
            //     length WORD_DELIM hint WORD_DELIM orientation WORD_DELIM 
            //     row WORD_DELIM col ENTRY_DELIM
            String[] info = entry.split(WORD_DELIM);
            int length = Integer.parseInt(info[0]);
            int row = Integer.parseInt(info[3]);
            int col = Integer.parseInt(info[4]);
            
            // draw cells for across and record hint
            if (info[2].equals("ACROSS")) {
                horizontalId(Integer.toString(across), row, col, g);
                acrossHints.add(across + ". " + info[1] + "\n");
                
                for (int i = 0; i < length; i++) {
                    drawCell(row, col + i, g);
                    //letterInCell(acrossAns.get(across).substring(i, i+1).toUpperCase(), row, col + i, g);
                }
                across++;
            } else { // draw cells for down and record hint
                verticalId(Integer.toString(down), row, col, g);
                downHints.add(down + ". " + info[1] + "\n");
                
                for (int i = 0; i < length; i++) {
                    drawCell(row + i, col, g);
                    //letterInCell(downAns.get(down).substring(i, i+1).toUpperCase(), row + i, col, g);
                }
                
                down++;
            }
        }
        
        // print hints for across and down words
        resetLine();
        if (across > 0) {
            println("Across\n", g);
            for (String hint: acrossHints) {
                println(hint, g);
            }
        }
        
        println("\n", g);
        if (down > 0) {
            println("Down\n", g);
            for (String hint: downHints) {
                println(hint, g);
            }
        }
    }

    private int x = 1;

    /**
     * Simple demo code just to illustrate how to paint cells in a crossword puzzle.
     * The paint method is called every time the JComponent is refreshed, or every
     * time the repaint method of this class is called.
     * We added some state just to allow you to see when the class gets repainted,
     * although in general you wouldn't want to be mutating state inside the paint
     * method.
     */
    @Override
    public void paint(Graphics g) {
        printPuzzle(g);
        /*
        for (int i = 0; i < x; ++i) {
            drawCell(i, i, g);
            letterInCell(Character.toString(i + 65), i, i, g);
            verticalId(Integer.toString(i), i, i, g);
            horizontalId(Integer.toString(i), i, i, g);
            resetLine();
            println("This is an example of adding text to the canvas.", g);
            println("You can use formatting to convey information about the state of the game.", g);
            println("Remember, this code is mostly here to show you how things work.", g);
            println("Make it your own.", g);
            printlnFancy("It's ok to get fancy with format.", g);
            printlnFancy("Have some fun with your UI!", g);
        }
        x = x + 1;
        */
    }
}
