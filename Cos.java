public class Cos extends UnaryOperation{
    public Cos(Expression operand) {
        super(operand);
    }

    @Override
    public double evaluate(java.util.Map<String, Double> variables) {
        return Math.cos(operand.evaluate(variables));
    }

    @Override
    public Expression differentiate(String varName) {
        // d/dx [cos(f(x))] = -sin(f(x)) * f'(x)
        return new Multiply(
            new Multiply(new Constant(-1), new Sin(operand)),
            operand.differentiate(varName)
        );
    }

    @Override
    public String toString() {
        return "cos(" + operand + ")";
    }

    @Override
    public Expression simplify() {
        Expression simplifiedOperand = operand.simplify();
        if (simplifiedOperand instanceof Constant) {
            double value = Math.cos(((Constant) simplifiedOperand).getValue());
            return new Constant(value);
        }
        return new Cos(simplifiedOperand);
    }
    
    
}
