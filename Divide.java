import java.util.Map;

public class Divide extends BinaryOperation {

    public Divide(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public double evaluate(Map<String, Double> variables) {
        return left.evaluate(variables) / right.evaluate(variables);
    }

    @Override
    public Expression differentiate(String varName) {
        return new Divide(
            new Subtract(
                new Multiply(left.differentiate(varName), right),
                new Multiply(left, right.differentiate(varName))
            ),
            new Power(right, new Constant(2))
        );
    }

    @Override
    public String toString() {
        return "(" + left + "/" + right + ")";
    }

    @Override
    public Expression simplify() {
        Expression simplifiedLeft = left.simplify();
        Expression simplifiedRight = right.simplify();
        // If both sides are constants, we can evaluate them directly
        if (simplifiedLeft instanceof Constant && simplifiedRight instanceof Constant) {
            double value = ((Constant) simplifiedLeft).getValue() / ((Constant) simplifiedRight).getValue();
            return new Constant(value);
        }
        // If the denominator is 0, we can't simplify further
        if (simplifiedRight instanceof Constant && ((Constant) simplifiedRight).getValue() == 0) {
            throw new ArithmeticException("Division by zero");
        }
        // If the numerator is 0, we can simplify to 0
        if (simplifiedLeft instanceof Constant && ((Constant) simplifiedLeft).getValue() == 0) {
            return new Constant(0);
        }
        // If the denominator is 1, we can simplify to the numerator
        if (simplifiedRight instanceof Constant && ((Constant) simplifiedRight).getValue() == 1) {
            return simplifiedLeft;
        }

        // Canonicalize: a / b  -->  a * (b ^ -1)
        Expression inverseRight = new Power(simplifiedRight, new Constant(-1));
        Expression canonicalized = new Multiply(simplifiedLeft, inverseRight);
        
        // Return the simplified version of the new multiplication branch
        return canonicalized.simplify();
    }
}
