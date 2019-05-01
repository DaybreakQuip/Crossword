@skip whitespace {
    file ::= ">>" name description entry+;
    name ::= stringIndent;
    description ::= string;
    entry ::= "("  wordname ","  clue "," direction "," row "," col ")";
}

comment ::= ("//" [^\r\n]*)?;
wordname ::= [a-z\-]+;
clue ::= string;
direction ::= "DOWN" | "ACROSS";
row ::= int;
col ::= int;
string ::= '"' ([^"\r\n\\] | '\\' [\\nrt] )* '"';
stringIndent ::= '"' [^"\r\n\t\\]* '"';
int ::= [0-9]+;
whitespace ::= [ \t\r\n]+;
newline ::= "\n";