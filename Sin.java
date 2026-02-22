public class Sin extends UnaryOperation{
    public Sin(Expression operand) {
        super(operand);
    }

    @Override
    public double evaluate(java.util.Map<String, Double> variables) {
        return Math.sin(operand.evaluate(variables));
    }

    @Override
    public Expression differentiate(String varName) {
        // d/dx [sin(f(x))] = cos(f(x)) * f'(x)
        return new Multiply(new Cos(operand), operand.differentiate(varName));
    }

    @Override
    public String toString() {
        return "sin(" + operand + ")";
    }

    @Override
    public Expression simplify() {
        Expression simplifiedOperand = operand.simplify();
        if (simplifiedOperand instanceof Constant) {
            double value = Math.sin(((Constant) simplifiedOperand).getValue());
            return new Constant(value);
        }
        return new Sin(simplifiedOperand);
    }
    
    
}
