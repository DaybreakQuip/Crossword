@skip WHITESPACE {
    FILE ::= ">>" NAME DESCRIPTION "\n" ENTRY*
    NAME ::= StringIdent
    DESCRIPTION ::= String
    ENTRY ::= "("  WORDNAME ","  CLUE "," DIRECTION "," ROW "," COL ")"
}

COMMENT = ("//" [^\r\n]*)?
WORDNAME ::= [a-z\-]+
CLUE ::= String
DIRECTION ::= "DOWN" | "ACROSS"
ROW ::= Int
COL ::= Int
String::= '"' ([^"\r\n\\] | '\\' [\\nrt] )* '"'
StringIdent ::= '"' [^"\r\n\t\\]* '"'
Int ::= [0-9]+
WHITESPACE ::= [ \t\r\n]+;