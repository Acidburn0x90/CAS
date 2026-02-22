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
        // SIMPLE POWER RULE: d/dx [x^n] = n * x^(n-1)
        // This keeps Test 2 and Test 4 from exploding into logs
        if (right instanceof Constant && left instanceof Variable) {
            double n = ((Constant) right).getValue();
            return new Multiply(new Constant(n), new Power(left, new Constant(n - 1))).simplify();
        }

        // GENERAL POWER RULE: For complex cases like f(x)^g(x)
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

        // Identity Rules
        if (simplifiedRight instanceof Constant && ((Constant) simplifiedRight).getValue() == 0) return new Constant(1);
        if (simplifiedRight instanceof Constant && ((Constant) simplifiedRight).getValue() == 1) return simplifiedLeft;
        if (simplifiedLeft instanceof Constant && ((Constant) simplifiedLeft).getValue() == 1) return new Constant(1);
        if (simplifiedLeft instanceof Constant && ((Constant) simplifiedLeft).getValue() == 0) return new Constant(0);

        return new Power(simplifiedLeft, simplifiedRight);
    }
}