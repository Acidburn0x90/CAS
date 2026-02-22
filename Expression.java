import java.util.Map;


// Expressions are Trees. They can be a number, a variable, or an operation (like addition or multiplication).
// The Expression interface defines the methods that all types of expressions must implement.
// The evaluate method takes a map of variable names to their values and computes the value of the expression.
// The differentiate method takes a variable name and returns a new Expression that represents the derivative of the original expression with respect to that variable.
// The toString method returns a string representation of the expression, which can be used for printing or debugging.
public interface Expression {
    // Map for variables so we can say "x is 5, y is 10"
    double evaluate(Map<String, Double> variables);
    
    // Returns a NEW tree representing the derivative
    Expression differentiate(String varName);
    
    // Prints the math: "(3 * x) + 5"
    String toString(); 

    Expression simplify(); // Optional: simplifies the expression (e.g., (3 * 0) + x -> x)
}