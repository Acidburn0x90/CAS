import java.util.Map;

public class Add extends BinaryOperation {
    public Add(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public double evaluate(Map<String, Double> variables) {
        return left.evaluate(variables) + right.evaluate(variables);
    }

    @Override
    public Expression differentiate(String varName) {
        return new Add(left.differentiate(varName), right.differentiate(varName));
    }

    @Override
    public String toString() {
        return "(" + left + " + " + right + ")";
    }

    @Override
    public Expression simplify() {
        Expression sL = left.simplify();
        Expression sR = right.simplify();

        if (sL instanceof Constant && sR instanceof Constant) {
            return new Constant(((Constant) sL).getValue() + ((Constant) sR).getValue());
        }
        if (sL instanceof Constant && ((Constant) sL).getValue() == 0) return sR;
        if (sR instanceof Constant && ((Constant) sR).getValue() == 0) return sL;

        // Pattern: cx + x -> (c+1)x
        if (sL instanceof Multiply && ((Multiply) sL).left instanceof Constant && ((Multiply) sL).right.equals(sR)) {
            double c = ((Constant) ((Multiply) sL).left).getValue();
            return new Multiply(new Constant(c + 1), sR).simplify();
        }
        
        // Pattern: x + x -> 2x
        if (sL.equals(sR)) return new Multiply(new Constant(2), sL).simplify();

        return new Add(sL, sR);
    }
}