public enum TokenType {
    NUMBER,     // e.g., "3", "3.14"
    VARIABLE,   // e.g., "x", "y"
    FUNCTION,   // e.g., "ln" (and later "sin", "cos")
    PLUS,       // "+"
    MINUS,      // "-"
    MULTIPLY,   // "*"
    DIVIDE,     // "/"
    POWER,      // "^"
    LPAREN,     // "("
    RPAREN,     // ")"
    EOF         // "End Of File" - A critical marker for the parser later!
}