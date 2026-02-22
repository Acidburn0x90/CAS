import java.util.Map;

public class Multiply extends BinaryOperation {
    public Multiply(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public double evaluate(Map<String, Double> variables) {
        return left.evaluate(variables) * right.evaluate(variables);
    }

    @Override
    public Expression differentiate(String varName) {
        return new Add(new Multiply(left, right.differentiate(varName)), 
                       new Multiply(left.differentiate(varName), right));
    }
    
    @Override
    public String toString() {
        if (left instanceof Constant && 
           (right instanceof Variable || right instanceof Power || right instanceof UnaryOperation)) {
            return left.toString() + right.toString();
        }
        return "(" + left + "*" + right + ")";
    }
    @Override
    public Expression simplify() {
        Expression sL = left.simplify();
        Expression sR = right.simplify();

        // 1. Constant Folding
        if (sL instanceof Constant && sR instanceof Constant) {
            return new Constant(((Constant) sL).getValue() * ((Constant) sR).getValue());
        }
        // Identity rules (0 and 1)
        if (sL instanceof Constant && ((Constant) sL).getValue() == 0) return new Constant(0);
        if (sR instanceof Constant && ((Constant) sR).getValue() == 0) return new Constant(0);
        if (sL instanceof Constant && ((Constant) sL).getValue() == 1) return sR;
        if (sR instanceof Constant && ((Constant) sR).getValue() == 1) return sL;

        // 2. Associative Reordering: Move constants to the left (x * 3 -> 3 * x)
        if (sR instanceof Constant && !(sL instanceof Constant)) {
            return new Multiply(sR, sL).simplify();
        }

        // 3. Constant Merging: 2 * (3 * x) -> 6 * x
        if (sL instanceof Constant && sR instanceof Multiply && ((Multiply) sR).left instanceof Constant) {
            double combined = ((Constant) sL).getValue() * ((Constant) ((Multiply) sR).left).getValue();
            return new Multiply(new Constant(combined), ((Multiply) sR).right).simplify();
        }

        // 4. Power Merging (x^a * x^b -> x^(a+b))
        if (sL instanceof Power && sR instanceof Power && ((Power) sL).getBase().equals(((Power) sR).getBase())) {
            return new Power(((Power) sL).getBase(), new Add(((Power) sL).getExponent(), ((Power) sR).getExponent())).simplify();
        }

        return new Multiply(sL, sR);
    }
}