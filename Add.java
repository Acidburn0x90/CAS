import java.util.Map;

public class Add extends BinaryOperation {
    // We dont need fields for left and right because they are inherited from BinaryOperation(protected final Expression left; protected final Expression right;)
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
        return "(" + left.toString() + " + " + right.toString() + ")";
    }

    @Override
    public Expression simplify() {
        Expression simplifiedLeft = left.simplify();
        Expression simplifiedRight = right.simplify();
        // If both sides are constants, we can evaluate them directly
        if (simplifiedLeft instanceof Constant && simplifiedRight instanceof Constant) {
        double value = ((Constant) simplifiedLeft).getValue() + ((Constant) simplifiedRight).getValue();
        return new Constant(value);
    }
        // If one side is a constant 0, we can simplify to the other side
        if (simplifiedLeft instanceof Constant && ((Constant) simplifiedLeft).getValue() == 0) {
            return simplifiedRight;
        }
        if (simplifiedRight instanceof Constant && ((Constant) simplifiedRight).getValue() == 0) {
            return simplifiedLeft;
        }
        // Pattern 1: x + x = 2x
        if (simplifiedLeft.equals(simplifiedRight)) {
            return new Multiply(new Constant(2), simplifiedLeft).simplify();
        }

        // Pattern 2: (c*x) + x = (c+1)x
        if (simplifiedLeft instanceof Multiply) {
            Multiply leftMult = (Multiply) simplifiedLeft;
            if (leftMult.getLeft() instanceof Constant && leftMult.getRight().equals(simplifiedRight)) {
                double newVal = ((Constant) leftMult.getLeft()).getValue() + 1;
                return new Multiply(new Constant(newVal), simplifiedRight).simplify();
            }
        }

        // Pattern 3: x + (c*x) = (1+c)x
        if (simplifiedRight instanceof Multiply) {
            Multiply rightMult = (Multiply) simplifiedRight;
            if (rightMult.getLeft() instanceof Constant && rightMult.getRight().equals(simplifiedLeft)) {
                double newVal = 1 + ((Constant) rightMult.getLeft()).getValue();
                return new Multiply(new Constant(newVal), simplifiedLeft).simplify();
            }
        }
        // Pattern 4: (a*x) + (b*x) = (a+b)x
        if (simplifiedLeft instanceof Multiply && simplifiedRight instanceof Multiply) {
            Multiply leftMult = (Multiply) simplifiedLeft;
            Multiply rightMult = (Multiply) simplifiedRight;
            
            if (leftMult.getLeft() instanceof Constant && rightMult.getLeft() instanceof Constant &&
                leftMult.getRight().equals(rightMult.getRight())) {
                double newVal = ((Constant) leftMult.getLeft()).getValue() + ((Constant) rightMult.getLeft()).getValue();
                return new Multiply(new Constant(newVal), leftMult.getRight()).simplify();
            }
        }

        return new Add(simplifiedLeft, simplifiedRight);
    }
    
    
}
