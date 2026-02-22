
public class Main {
    public static void main(String[] args) {
        System.out.println("=== SYMBOLIC MATH ENGINE TEST SUITE ===\n");

        // An array of equations designed to test specific engine features
        String[] tests = {
            "sin(x^2)",   // Test 1: Chain rule, implicit formatting, and nested ASTs
            "x^2 / x",    // Test 2: Canonicalization (division -> negative power) and merging
            "2x * 3",     // Test 3: Implicit multiplication, parsing, and tree rotations
            "x * x^2"     // Test 4: Term collection and exponent addition
        };

        for (int i = 0; i < tests.length; i++) {
            System.out.println("--- Test " + (i + 1) + " ---");
            runTest(tests[i], 3.0);
            System.out.println();
        }

        // Test 5: The strict EOF Syntax Check
        System.out.println("--- Test 5: Syntax Error Handling ---");
        try {
            String badMath = "2 + 3 x";
            System.out.println("Input: " + badMath);
            Lexer lexer = new Lexer(badMath);
            Parser parser = new Parser(lexer.tokenize());
            parser.parse();
            System.out.println("FAILURE: The parser accepted invalid math.");
        } catch (IllegalArgumentException e) {
            System.out.println("SUCCESS: Parser correctly threw error: " + e.getMessage());
        }
    }
    private static void runTest(String input, double xValue) {
    try {
        Lexer lexer = new Lexer(input);
        Parser parser = new Parser(lexer.tokenize());
        Expression equation = parser.parse();
        
        // --- THE FIX: Simplify BEFORE Differentiating ---
        Expression simplifiedInput = equation.simplify(); 
        System.out.println("Simplified f(x): " + simplifiedInput.toString());

        Expression derivative = simplifiedInput.differentiate("x");
        
        // Final pass to clean up the derivative result
        Expression finalResult = derivative.simplify();
        System.out.println("Derivative f'(x): " + finalResult.toString());

    } catch (Exception e) {
        e.printStackTrace();
    }
}
}