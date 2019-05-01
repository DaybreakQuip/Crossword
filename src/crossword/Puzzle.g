@skip whitespace {
    file ::= ">>" name description (comment | entry)*;
    entry ::= "("  wordname ","  clue "," direction "," row "," col ")";
    name ::= stringIndent;
}

description ::= string "\n";
comment ::= "//" [^\r\n]* "\n";
wordname ::= [a-z\-]+;
clue ::= string;
direction ::= "DOWN" | "ACROSS";
row ::= int;
col ::= int;
string ::= '"' ([^"\r\n\\] | '\\' [\\nrt] )* '"';
stringIndent ::= '"' [^"\r\n\t\\]* '"';
int ::= [0-9]+;
whitespace ::= [ \t\r\n]+;