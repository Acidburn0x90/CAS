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

        // 1. Constant Folding
        if (simplifiedLeft instanceof Constant && simplifiedRight instanceof Constant) {
            return new Constant(((Constant) simplifiedLeft).getValue() * ((Constant) simplifiedRight).getValue());
        }
        if (simplifiedLeft instanceof Constant && ((Constant) simplifiedLeft).getValue() == 0) return new Constant(0);
        if (simplifiedRight instanceof Constant && ((Constant) simplifiedRight).getValue() == 0) return new Constant(0);
        if (simplifiedLeft instanceof Constant && ((Constant) simplifiedLeft).getValue() == 1) return simplifiedRight;
        if (simplifiedRight instanceof Constant && ((Constant) simplifiedRight).getValue() == 1) return simplifiedLeft;

        // 2. Advanced Power Merging (Fixes Test 2 and Test 4)
        // Case: x^a * x^b -> x^(a+b)
        if (simplifiedLeft instanceof Power && simplifiedRight instanceof Power) {
            Power pLeft = (Power) simplifiedLeft;
            Power pRight = (Power) simplifiedRight;
            if (pLeft.getBase().equals(pRight.getBase())) {
                return new Power(pLeft.getBase(), new Add(pLeft.getExponent(), pRight.getExponent())).simplify();
            }
        }
        // Case: x * x^a -> x^(a+1)
        if (simplifiedRight instanceof Power && ((Power) simplifiedRight).getBase().equals(simplifiedLeft)) {
            return new Power(simplifiedLeft, new Add(((Power) simplifiedRight).getExponent(), new Constant(1))).simplify();
        }
        // Case: x^a * x -> x^(a+1)
        if (simplifiedLeft instanceof Power && ((Power) simplifiedLeft).getBase().equals(simplifiedRight)) {
            return new Power(simplifiedRight, new Add(((Power) simplifiedLeft).getExponent(), new Constant(1))).simplify();
        }

        if (simplifiedLeft.equals(simplifiedRight)) return new Power(simplifiedLeft, new Constant(2)).simplify();
        
        // 3. Associative Reordering: (x * 2) * 3 -> x * 6
        if (simplifiedRight instanceof Constant && simplifiedLeft instanceof Multiply && ((Multiply) simplifiedLeft).right instanceof Constant) {
            double newVal = ((Constant) simplifiedRight).getValue() * ((Constant) ((Multiply) simplifiedLeft).right).getValue();
            return new Multiply(((Multiply) simplifiedLeft).left, new Constant(newVal)).simplify();
        }

        return new Multiply(simplifiedLeft, simplifiedRight);
    }
}