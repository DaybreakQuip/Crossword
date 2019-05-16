package crossword;


public class CrosswordCanvasTest {
    // Partitions:
    //     Length of playerID = 1, > 1
    //     Whether state includes: START, CHOOSE, WAIT, PLAY, or SHOW_SCORE
    //     Whether there is a previousResponse
    //     Length of puzzleList = 0, 1, > 1
    //     Length of matchList = 0, 1, > 1
    //     Length of currentPuzzle = 0, 1, > 1
    //     Length of overallScore = 0, 1, > 1
    //     Length of winner = 0, 1, > 1

    // Manual tests
    // Covers: length of playerID = 1,
    //         state includes START and CHOOSE
    //         length of puzzleList = 0, > 1
    //         length of matchList = 0, > 1
    //         length of currentPuzzle = 0
    //         length of overallScore = 0
    //         length of winner = 0
    // 1. Create a new TextServer with one of these commands: 
    //      Windows: java -cp "bin;lib/parserlib.jar" crossword.ServerMain puzzles/ 4444
    //      Mac: java -cp bin:lib/parserlib.jar crossword.ServerMain puzzles/ 4444
    // 2. Open another terminal window and connect to the client with the command: 
    //      java -cp bin crossword.Client localhost 4444
    // 3. Make sure the crossword client (GUI) appears with a text box and enter button across the top
    //      and instructions on the right for how to login.
    // 4. Type "A" (without quotation marks) into the text box and press the enter button.
    // 5. Make sure the GUI appears with lists of commands, available matches, and available puzzles. 
    
    // Manual tests
    // Covers: length of playerID > 1,
    //         state includes CHOOSE and WAIT
    //         length of puzzleList > 1
    //         length of matchList > 1
    //         length of currentPuzzle = 0
    //         length of overallScore = 0
    //         length of winner = 0
    // 1. Create a new TextServer with one of these commands: 
    //      Windows: java -cp "bin;lib/parserlib.jar" crossword.ServerMain puzzles/ 4444
    //      Mac: java -cp bin:lib/parserlib.jar crossword.ServerMain puzzles/ 4444
    // 2. Open another terminal window and connect to the client with the command: 
    //      java -cp bin crossword.Client localhost 4444
    // 3. Make sure the crossword client (GUI) appears with a text box and enter button across the top
    //      and instructions on the right for how to login.
    // 4. Type "Player" (without quotation marks) into the text box and press the enter button.
    // 5. Make sure the GUI appears with lists of commands, available matches, and available puzzles. 
    // 6. Type "NEW 0 Easy \"easy puzzle\"" (without quotation marks) into the text box and press enter.
    // 7. Make sure the GUI appears with instructions for waiting for another player to join the match.
    
    // Covers: length of playerID > 1,
    //         state includes CHOOSE, WAIT, PLAY
    //         length of puzzleList > 1
    //         length of matchList > 1
    //         length of currentPuzzle > 1
    //         length of overallScore = 0
    //         length of winner = 0
    // 1. Create a new TextServer with one of these commands: 
    //      Windows: java -cp "bin;lib/parserlib.jar" crossword.ServerMain puzzles/ 4444
    //      Mac: java -cp bin:lib/parserlib.jar crossword.ServerMain puzzles/ 4444
    // 2. Open another terminal window and connect to the client #1 with the command: 
    //      java -cp bin crossword.Client localhost 4444
    // 3. Make sure the crossword client (GUI) appears with a text box and enter button across the top
    //      and instructions on the right for how to login.
    // 4. Type "Player" (without quotation marks) into the text box and press the enter button.
    // 5. Make sure the GUI appears with lists of commands, available matches, and available puzzles. 
    // 6. Type "NEW 0 Easy \"easy puzzle\"" (without quotation marks) into the text box and press enter.
    // 7. Make sure the GUI appears with instructions for waiting for another player to join the match.
    // 8. Open another terminal window and connect to the client #2 with the command: 
    //      java -cp bin crossword.Client localhost 4444
    // 9. Make sure the crossword client (GUI) appears with a text box and enter button across the top
    //      and instructions on the right for how to login.
    // 10. Type "Opponent" (without quotation marks) into the text box and press the enter button.
    // 11. Make sure the GUI appears with lists of commands, available matches, and available puzzles. 
    // 12. Type "PLAY 0" (without quotation marks) into the text box and press the enter button.
    // 13. Make sure a crossword puzzle with blank yellow squares as cells appear with the list of 
    //     commands and hints on both client #1 and client #2.
    
    // Covers: length of playerID > 1,
    //         state includes CHOOSE and START
    //         length of puzzleList > 1
    //         length of matchList > 1
    //         length of currentPuzzle > 1
    //         length of overallScore = 0
    //         length of winner = 0
    // 1. Create a new TextServer with one of these commands: 
    //      Windows: java -cp "bin;lib/parserlib.jar" crossword.ServerMain puzzles/ 4444
    //      Mac: java -cp bin:lib/parserlib.jar crossword.ServerMain puzzles/ 4444
    // 2. Open another terminal window and connect to the client #1 with the command: 
    //      java -cp bin crossword.Client localhost 4444
    // 3. Make sure the crossword client (GUI) appears with a text box and enter button across the top
    //      and instructions on the right for how to login.
    // 4. Type "Player" (without quotation marks) into the text box and press the enter button.
    // 5. Make sure the GUI appears with lists of commands, available matches, and available puzzles. 
    // 6. Type "EXIT" (without quotation marks) into the text box and press enter.
    // 7. Make sure the GUI appears with instructions for how to login.
    
    // Covers: length of playerID > 1,
    //         state includes CHOOSE, WAIT, PLAY
    //         length of puzzleList > 1
    //         length of matchList > 1
    //         length of currentPuzzle > 1
    //         length of overallScore = 0
    //         length of winner = 0
    // 1. Create a new TextServer with one of these commands: 
    //      Windows: java -cp "bin;lib/parserlib.jar" crossword.ServerMain puzzles/ 4444
    //      Mac: java -cp bin:lib/parserlib.jar crossword.ServerMain puzzles/ 4444
    // 2. Open another terminal window and connect to the client with the command: 
    //      java -cp bin crossword.Client localhost 4444
    // 3. Make sure the crossword client (GUI) appears with a text box and enter button across the top
    //      and instructions on the right for how to login.
    // 4. Type "Player" (without quotation marks) into the text box and press the enter button.
    // 5. Make sure the GUI appears with lists of commands, available matches, and available puzzles. 
    // 6. Type "NEW 0 Easy \"easy puzzle\"" (without quotation marks) into the text box and press enter.
    // 7. Make sure the GUI appears with instructions for waiting for another player to join the match.
    // 8. Type "EXIT" (without quotation marks) into the text box and press enter.
    // 9. Make sure the GUI appears with lists of commands, available matches, and available puzzles. 
    
    // Covers: length of playerID > 1,
    //         state includes PLAY
    //         length of puzzleList = 0
    //         length of matchList = 0
    //         length of currentPuzzle > 1
    //         length of overallScore = 0
    //         length of winner = 0
    // 1. Create a new TextServer with one of these commands: 
    //      Windows: java -cp "bin;lib/parserlib.jar" crossword.ServerMain puzzles/ 4444
    //      Mac: java -cp bin:lib/parserlib.jar crossword.ServerMain puzzles/ 4444
    // 2. Open another terminal window and connect to the client #1 with the command: 
    //      java -cp bin crossword.Client localhost 4444
    // 3. Make sure the crossword client (GUI) appears with a text box and enter button across the top
    //      and instructions on the right for how to login.
    // 4. Type "Player" (without quotation marks) into the text box and press the enter button.
    // 5. Make sure the GUI appears with lists of commands, available matches, and available puzzles. 
    // 6. Type "NEW 0 Easy \"easy puzzle\"" (without quotation marks) into the text box and press enter.
    // 7. Make sure the GUI appears with instructions for waiting for another player to join the match.
    // 8. Open another terminal window and connect to the client #2 with the command: 
    //      java -cp bin crossword.Client localhost 4444
    // 9. Make sure the crossword client (GUI) appears with a text box and enter button across the top
    //      and instructions on the right for how to login.
    // 10. Type "Opponent" (without quotation marks) into the text box and press the enter button.
    // 11. Make sure the GUI appears with lists of commands, available matches, and available puzzles. 
    // 12. Type "PLAY 0" (without quotation marks) into the text box and press the enter button.
    // 13. Make sure a crossword puzzle with blank yellow squares as cells appear with the list of 
    //     commands and hints on both client #1 and client #2.
    // 14. In client #1, type in the command "TRY 0 star" (without quotation marks) into the text box and press enter.
    // 15. Make sure the cells for word 0 in the puzzle in client #1's window have a red background and are
    //     filled with the letters 'S' 'T' 'A' 'R' and the hint for word 0 is highlighted with a red background.
    // 16. Make sure the cells for word 0 in the puzzle in client #2's window have a green background and are
    //     filled with the letters 'S' 'T' 'A' 'R' and the hint for word 0 is highlighted with a green background.
    
    // Covers: length of playerID > 1,
    //         state includes PLAY and SHOW_SCORE
    //         length of puzzleList = 0
    //         length of matchList = 0
    //         length of currentPuzzle > 1
    //         length of overallScore > 0
    //         length of winner > 1
    // 1. Create a new TextServer with one of these commands: 
    //      Windows: java -cp "bin;lib/parserlib.jar" crossword.ServerMain puzzles/ 4444
    //      Mac: java -cp bin:lib/parserlib.jar crossword.ServerMain puzzles/ 4444
    // 2. Open another terminal window and connect to the client #1 with the command: 
    //      java -cp bin crossword.Client localhost 4444
    // 3. Make sure the crossword client (GUI) appears with a text box and enter button across the top
    //      and instructions on the right for how to login.
    // 4. Type "Player" (without quotation marks) into the text box and press the enter button.
    // 5. Make sure the GUI appears with lists of commands, available matches, and available puzzles. 
    // 6. Type "NEW 0 Easy \"easy puzzle\"" (without quotation marks) into the text box and press enter.
    // 7. Make sure the GUI appears with instructions for waiting for another player to join the match.
    // 8. Open another terminal window and connect to the client #2 with the command: 
    //      java -cp bin crossword.Client localhost 4444
    // 9. Make sure the crossword client (GUI) appears with a text box and enter button across the top
    //      and instructions on the right for how to login.
    // 10. Type "Opponent" (without quotation marks) into the text box and press the enter button.
    // 11. Make sure the GUI appears with lists of commands, available matches, and available puzzles. 
    // 12. Type "PLAY 0" (without quotation marks) into the text box and press the enter button.
    // 13. Make sure a crossword puzzle with blank yellow squares as cells appear with the list of 
    //     commands and hints on both client #1 and client #2.
    // 14. In client #1, type in the command "TRY 0 star" (without quotation marks) into the text box and press enter.
    // 15. Make sure the cells for word 0 in the puzzle in client #1's window have a red background and are
    //     filled with the letters 'S' 'T' 'A' 'R' and the hint for word 0 is highlighted with a red background.
    // 16. Make sure the cells for word 0 in the puzzle in client #2's window have a green background and are
    //     filled with the letters 'S' 'T' 'A' 'R' and the hint for word 0 is highlighted with a green background.
    // 17. In client #1, type in the command "EXIT" (without quotation marks) into the text box and press enter. 
    // 18. Make sure the GUIs in both client #1 and client #2 appear with a list of commands, the challenge points 
    //     and total score for each player, and which player won the game (or tied the game).

    // Covers: length of playerID > 1,
    //         state includes SHOW_SCORE and CHOOSE
    //         length of puzzleList = > 1
    //         length of matchList = > 1
    //         length of currentPuzzle > 1
    //         length of overallScore > 0
    //         length of winner > 1
    // 1. Create a new TextServer with one of these commands: 
    //      Windows: java -cp "bin;lib/parserlib.jar" crossword.ServerMain puzzles/ 4444
    //      Mac: java -cp bin:lib/parserlib.jar crossword.ServerMain puzzles/ 4444
    // 2. Open another terminal window and connect to the client #1 with the command: 
    //      java -cp bin crossword.Client localhost 4444
    // 3. Make sure the crossword client (GUI) appears with a text box and enter button across the top
    //      and instructions on the right for how to login.
    // 4. Type "Player" (without quotation marks) into the text box and press the enter button.
    // 5. Make sure the GUI appears with lists of commands, available matches, and available puzzles. 
    // 6. Type "NEW 0 Easy \"easy puzzle\"" (without quotation marks) into the text box and press enter.
    // 7. Make sure the GUI appears with instructions for waiting for another player to join the match.
    // 8. Open another terminal window and connect to the client #2 with the command: 
    //      java -cp bin crossword.Client localhost 4444
    // 9. Make sure the crossword client (GUI) appears with a text box and enter button across the top
    //      and instructions on the right for how to login.
    // 10. Type "Opponent" (without quotation marks) into the text box and press the enter button.
    // 11. Make sure the GUI appears with lists of commands, available matches, and available puzzles. 
    // 12. Type "PLAY 0" (without quotation marks) into the text box and press the enter button.
    // 13. Make sure a crossword puzzle with blank yellow squares as cells appear with the list of 
    //     commands and hints on both client #1 and client #2.
    // 14. In client #1, type in the command "TRY 0 star" (without quotation marks) into the text box and press enter.
    // 15. Make sure the cells for word 0 in the puzzle in client #1's window have a red background and are
    //     filled with the letters 'S' 'T' 'A' 'R' and the hint for word 0 is highlighted with a red background.
    // 16. Make sure the cells for word 0 in the puzzle in client #2's window have a green background and are
    //     filled with the letters 'S' 'T' 'A' 'R' and the hint for word 0 is highlighted with a green background.
    // 17. In client #1, type in the command "EXIT" (without quotation marks) into the text box and press enter. 
    // 18. Make sure the GUIs in both client #1 and client #2 appear with a list of commands, the challenge points 
    //     and total score for each player, and which player won the game (or tied the game).
    // 19. Type "NEW MATCH" (without quotation marks) into the text box and press the enter button.
    // 20. Make sure the GUI appears with lists of commands, available matches, and available puzzles. 
    
    // Covers: length of playerID > 1,
    //         state includes SHOW_SCORE and CHOOSE
    //         length of puzzleList > 1
    //         length of matchList > 1
    //         length of currentPuzzle = 0
    //         length of overallScore > 1
    //         length of winner > 1
    // 1. Create a new TextServer with one of these commands: 
    //      Windows: java -cp "bin;lib/parserlib.jar" crossword.ServerMain puzzles/ 4444
    //      Mac: java -cp bin:lib/parserlib.jar crossword.ServerMain puzzles/ 4444
    // 2. Open another terminal window and connect to the client #1 with the command: 
    //      java -cp bin crossword.Client localhost 4444
    // 3. Make sure the crossword client (GUI) appears with a text box and enter button across the top
    //      and instructions on the right for how to login.
    // 4. Type "Player" (without quotation marks) into the text box and press the enter button.
    // 5. Make sure the GUI appears with lists of commands, available matches, and available puzzles. 
    // 6. Type "NEW 0 Easy \"easy puzzle\"" (without quotation marks) into the text box and press enter.
    // 7. Make sure the GUI appears with instructions for waiting for another player to join the match.
    // 8. Open another terminal window and connect to the client #2 with the command: 
    //      java -cp bin crossword.Client localhost 4444
    // 9. Make sure the crossword client (GUI) appears with a text box and enter button across the top
    //      and instructions on the right for how to login.
    // 10. Type "Opponent" (without quotation marks) into the text box and press the enter button.
    // 11. Make sure the GUI appears with lists of commands, available matches, and available puzzles. 
    // 12. Type "PLAY 0" (without quotation marks) into the text box and press the enter button.
    // 13. Make sure a crossword puzzle with blank yellow squares as cells appear with the list of 
    //     commands and hints on both client #1 and client #2.
    // 14. In client #1, type in the command "TRY 0 star" (without quotation marks) into the text box and press enter.
    // 15. Make sure the cells for word 0 in the puzzle in client #1's window have a red background and are
    //     filled with the letters 'S' 'T' 'A' 'R' and the hint for word 0 is highlighted with a red background.
    // 16. Make sure the cells for word 0 in the puzzle in client #2's window have a green background and are
    //     filled with the letters 'S' 'T' 'A' 'R' and the hint for word 0 is highlighted with a green background.
    // 17. In client #1, type in the command "EXIT" (without quotation marks) into the text box and press enter. 
    // 18. Make sure the GUIs in both client #1 and client #2 appear with a list of commands, the challenge points 
    //     and total score for each player, and which player won the game (or tied the game).
    // 19. Type "EXIT" (without quotation marks) into the text box and press the enter button.
    // 20. Make sure the GUI appears with with a text box and enter button across the top
    //     and instructions on the right for how to login.
}
