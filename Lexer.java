import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private final String input;
    private int pos = 0;

    public Lexer(String input) {
        this.input = input;
    }

    public List<Token> tokenize() {
        // 1. Initialize the list to store our tokens
        List<Token> tokens = new ArrayList<>();

        while (pos < input.length()) {
            char c = input.charAt(pos);

            // WHITESPACE
            if (Character.isWhitespace(c)) {
                pos++;
                continue; // 3. Jump back to the top of the loop
            }

            // OPERATORS
            if (c == '+' || c == '-' || c == '*' || c == '/' || c == '^' || c == '(' || c == ')') {
                switch (c) {
                    case '+': tokens.add(new Token(TokenType.PLUS, "+")); break;
                    case '-': tokens.add(new Token(TokenType.MINUS, "-")); break;
                    case '*': tokens.add(new Token(TokenType.MULTIPLY, "*")); break;
                    case '/': tokens.add(new Token(TokenType.DIVIDE, "/")); break;
                    case '^': tokens.add(new Token(TokenType.POWER, "^")); break;
                    case '(': tokens.add(new Token(TokenType.LPAREN, "(")); break;
                    case ')': tokens.add(new Token(TokenType.RPAREN, ")")); break;
                }
                pos++; // 2. Advance the pointer!
                continue;
            }

            // NUMBERS (Greedy Match)
            if (Character.isDigit(c) || c == '.') {
                StringBuilder sb = new StringBuilder();
                while (pos < input.length() && (Character.isDigit(input.charAt(pos)) || input.charAt(pos) == '.')) {
                    sb.append(input.charAt(pos));
                    pos++;
                }
                tokens.add(new Token(TokenType.NUMBER, sb.toString()));
                continue;
            }

            // VARIABLES & FUNCTIONS (Greedy Match)
            if (Character.isLetter(c)) {
                StringBuilder sb = new StringBuilder();
                while (pos < input.length() && (Character.isLetterOrDigit(input.charAt(pos)) || input.charAt(pos) == '_')) {
                    sb.append(input.charAt(pos));
                    pos++;
                }
                String name = sb.toString();
                // Grouping the functions makes this cleaner
                if (name.equals("ln") || name.equals("sin") || name.equals("cos")) {
                    tokens.add(new Token(TokenType.FUNCTION, name));
                } else {
                    tokens.add(new Token(TokenType.VARIABLE, name));
                }
                continue;
            }

            // 4. THE FAIL-SAFE
            throw new IllegalArgumentException("Lexer Error: Unknown character '" + c + "' at position " + pos);
        }

        // Add the End Of File token so the Parser knows when to stop
        tokens.add(new Token(TokenType.EOF, ""));
        return tokens;
    }
}