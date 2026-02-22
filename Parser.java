import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int pos = 0;

    public Expression parse() {
        // 1. Parse the full mathematical tree
        Expression result = expression();
        
        // 2. The Strict Grammar Check
        if (peek().type != TokenType.EOF) {
            throw new IllegalArgumentException(
                "Syntax Error: Unexpected trailing token '" + peek().value + "'"
            );
        }
        
        // 3. If the coast is clear, return the AST
        return result;
    }

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public Token peek() {
        return tokens.get(pos);
    }

    public Token advance() {
        return tokens.get(pos++);
    }

    public boolean match(TokenType type) {
        if (peek().type == type) {
            advance();
            return true;
        }
        return false;
    }

    // Grammar methods in order of operations: 
    // Expression (+, -)
    // Term (*, /)
    // Power (^)
    // Primary (Numbers, Variables, Functions, Parenthasis)

    private Expression expression() {
        Expression left = term(); 
        while (peek().type == TokenType.PLUS || peek().type == TokenType.MINUS) {
            Token operator = advance(); 
            Expression right = term();
            
            if (operator.type == TokenType.PLUS) {
                left = new Add(left, right);
            } else {
                left = new Subtract(left, right);
            }
        }
        return left;
    }
    // Identical, just up one level
    private Expression term() {
        Expression left = power();
        
        // We use an infinite loop that we manually break out of 
        // when we no longer see term-level operations.
        while (true) {
            
            // 1. Explicit Multiplication
            if (match(TokenType.MULTIPLY)) {
                Expression right = power();
                left = new Multiply(left, right);
            } 
            // 2. Explicit Division
            else if (match(TokenType.DIVIDE)) {
                Expression right = power();
                left = new Divide(left, right);
            } 
            // 3. IMPLICIT MULTIPLICATION (e.g., 2x, 3sin(x), 4(x+1))
            else if (peek().type == TokenType.VARIABLE || 
                     peek().type == TokenType.FUNCTION || 
                     peek().type == TokenType.LPAREN) {
                
                // Notice we do NOT consume an operator token here.
                // We just immediately parse the right side and wrap it in a Multiply node.
                Expression right = power();
                left = new Multiply(left, right);
            } 
            // 4. No term-level operations left
            else {
                break;
            }
        }
        
        return left;
    }

    private Expression power() {
    Expression left = primary(); // Grab the base
    
        if (peek().type == TokenType.POWER) {
            advance(); // Consume the '^'
            Expression right = power(); 
            left = new Power(left, right);
        }
        return left;
    }
    private Expression primary() {
    Token current = advance();

    switch (current.type) {
        case NUMBER:
            return new Constant(Double.parseDouble(current.value));
        case VARIABLE:
            return new Variable(current.value);
        case FUNCTION:
            String functionName = current.value;
            // Strictly enforce the opening parenthesis
            if (!match(TokenType.LPAREN)) {
                throw new IllegalArgumentException("Expected '(' after function " + functionName);
            }
            // Parse the inner math
            Expression argument = expression();
            // Strictly enforce the closing parenthesis
            if (!match(TokenType.RPAREN)) {
                throw new IllegalArgumentException("Expected ')' after function argument");
            }
            // Route to the correct AST node
            switch (functionName) {
                case "ln": return new Log(argument);
                case "sin": return new Sin(argument); 
                case "cos": return new Cos(argument);
                default: throw new IllegalArgumentException("Unknown function: " + functionName);
            }
        case LPAREN:
            // Parentheses override: jump back up to the top of the grammar
            Expression expr = expression();
            
            if (!match(TokenType.RPAREN)) {
                throw new IllegalArgumentException("Expected ')' to close grouped expression");
            }
            return expr;
            
        default:
            // The Fallback: If we get here, the user typed garbage syntax.
            throw new IllegalArgumentException("Unexpected token in math expression: " + current.type);
    }
}
}