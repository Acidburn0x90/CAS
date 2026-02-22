import java.util.Map;

public class Subtract extends BinaryOperation {
    public Subtract(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public double evaluate(Map<String, Double> variables) {
        return left.evaluate(variables) - right.evaluate(variables);
    }

    @Override
    public Expression differentiate(String varName) {
        return new Subtract(left.differentiate(varName), right.differentiate(varName));
    }
    
    @Override
    public String toString() {
        return "(" + left + "-" + right + ")";
    }

    @Override
    public Expression simplify() {
        Expression simplifiedLeft = left.simplify();
        Expression simplifiedRight = right.simplify();
        // If both sides are constants, we can evaluate them directly
        if (simplifiedLeft instanceof Constant && simplifiedRight instanceof Constant) {
            double value = ((Constant) simplifiedLeft).getValue() - ((Constant) simplifiedRight).getValue();
            return new Constant(value);
        }
        // If the right side is a constant 0, we can simplify to the left side
        if (simplifiedRight instanceof Constant && ((Constant) simplifiedRight).getValue() == 0) {
            return simplifiedLeft;
        }
        // If the left side is a constant 0, we can simplify to the negative of the right side
        if (simplifiedLeft instanceof Constant && ((Constant) simplifiedLeft).getValue() == 0) {
            return new Multiply(new Constant(-1), simplifiedRight);
        }
        // Canonicalize: a - b --> a + (-1 * b)
        Expression negativeRight = new Multiply(new Constant(-1), simplifiedRight);
        Expression canonicalized = new Add(simplifiedLeft, negativeRight);
        
        return canonicalized.simplify();
    }
    
}
