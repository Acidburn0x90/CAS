import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== SYMBOLIC MATH ENGINE TEST SUITE ===\n");

        String[] tests = {
            "sin(x^2)",   // Test 1: Chain rule
            "x^2 / x",    // Test 2: Pre-differentiation simplification
            "2x * 3",     // Test 3: Tree rotations
            "x * x^2"     // Test 4: Term collection
        };

        for (int i = 0; i < tests.length; i++) {
            System.out.println("--- Test " + (i + 1) + " ---");
            runTest(tests[i], 3.0);
            System.out.println();
        }

        System.out.println("--- Test 5: Syntax Error Handling ---");
        try {
            String badMath = "2 + * 3"; 
            Lexer lexer = new Lexer(badMath);
            Parser parser = new Parser(lexer.tokenize());
            parser.parse();
            System.out.println("FAILURE: The parser accepted invalid math.");
        } catch (IllegalArgumentException e) {
            System.out.println("SUCCESS: Parser correctly threw error: " + e.getMessage());
        }
    }

    private static void runTest(String input, double xValue) {
        System.out.println("Input Equation: f(x) = " + input);
        try {
            Lexer lexer = new Lexer(input);
            Parser parser = new Parser(lexer.tokenize());
            Expression equation = parser.parse();

            // STEP 1: Simplify BEFORE differentiating (Fixes Test 2)
            Expression simplifiedInput = equation.simplify(); 
            System.out.println("Simplified f(x): " + simplifiedInput.toString());

            // STEP 2: Differentiate the clean tree
            Expression derivative = simplifiedInput.differentiate("x");
            
            // STEP 3: Final pass to clean up the derivative result
            Expression finalResult = derivative.simplify();
            System.out.println("Derivative f'(x): " + finalResult.toString());

        } catch (Exception e) {
            System.err.println("Engine error: " + e.getMessage());
        }
    }
}