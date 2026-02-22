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
        Expression simplifiedLeft = left.simplify();
        Expression simplifiedRight = right.simplify();

        // Constant Folding & Identities
        if (simplifiedLeft instanceof Constant && simplifiedRight instanceof Constant) {
            return new Constant(((Constant) simplifiedLeft).getValue() * ((Constant) simplifiedRight).getValue());
        }
        if (simplifiedLeft instanceof Constant && ((Constant) simplifiedLeft).getValue() == 0) return new Constant(0);
        if (simplifiedRight instanceof Constant && ((Constant) simplifiedRight).getValue() == 0) return new Constant(0);
        if (simplifiedLeft instanceof Constant && ((Constant) simplifiedLeft).getValue() == 1) return simplifiedRight;
        if (simplifiedRight instanceof Constant && ((Constant) simplifiedRight).getValue() == 1) return simplifiedLeft;

        // Term Merging (x * x^a)
        if (simplifiedLeft.equals(simplifiedRight)) return new Power(simplifiedLeft, new Constant(2)).simplify();
        
        // Associative Rotations (A * (c * B) -> c * (A * B))
        if (simplifiedRight instanceof Multiply && ((Multiply) simplifiedRight).left instanceof Constant) {
            return new Multiply(((Multiply) simplifiedRight).left, new Multiply(simplifiedLeft, ((Multiply) simplifiedRight).right)).simplify();
        }

        // DISTRIBUTIVE LAW: Fixes Test 2
        if (simplifiedRight instanceof Add) {
            return new Add(new Multiply(simplifiedLeft, ((Add) simplifiedRight).getLeft()), 
                        new Multiply(simplifiedLeft, ((Add) simplifiedRight).getRight())).simplify();
        }
        if (simplifiedLeft instanceof Add) {
            return new Add(new Multiply(((Add) simplifiedLeft).getLeft(), simplifiedRight), 
                        new Multiply(((Add) simplifiedLeft).getRight(), simplifiedRight)).simplify();
        }

        return new Multiply(simplifiedLeft, simplifiedRight);
    }
}