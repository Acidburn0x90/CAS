import java.util.Map;

public class Power extends BinaryOperation {
    public Power(Expression left, Expression right) {
        super(left, right);
    }

    public Expression getBase() { return this.left; }
    public Expression getExponent() { return this.right; }

    @Override
    public double evaluate(Map<String, Double> variables) {
        return Math.pow(left.evaluate(variables), right.evaluate(variables));
    }

    @Override
    public Expression differentiate(String varName) {
        // d/dx [f(x)^n] = n * f(x)^(n-1) * f'(x)
        if (right instanceof Constant) {
            double n = ((Constant) right).getValue();
            return new Multiply(
                new Constant(n),
                new Multiply(
                    new Power(left, new Constant(n - 1)),
                    left.differentiate(varName)
                )
            ).simplify();
        }

        // General Power Rule: f(x)^g(x)
        return new Multiply(this, new Add(
            new Multiply(right.differentiate(varName), new Log(left)), 
            new Multiply(right, new Divide(left.differentiate(varName), left))
        ));
    }
    
    @Override
    public String toString() {
        return "(" + left + "^" + right + ")";
    }

    @Override
    public Expression simplify() {
        Expression simplifiedLeft = left.simplify();
        Expression simplifiedRight = right.simplify();

        if (simplifiedLeft instanceof Constant && simplifiedRight instanceof Constant) {
            return new Constant(Math.pow(((Constant) simplifiedLeft).getValue(), ((Constant) simplifiedRight).getValue()));
        }

        if (simplifiedRight instanceof Constant && ((Constant) simplifiedRight).getValue() == 0) return new Constant(1);
        if (simplifiedRight instanceof Constant && ((Constant) simplifiedRight).getValue() == 1) return simplifiedLeft;
        if (simplifiedLeft instanceof Constant && ((Constant) simplifiedLeft).getValue() == 1) return new Constant(1);
        if (simplifiedLeft instanceof Constant && ((Constant) simplifiedLeft).getValue() == 0) return new Constant(0);

        return new Power(simplifiedLeft, simplifiedRight);
    }
}