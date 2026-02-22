import java.util.Map;

public class Log extends UnaryOperation {
    public Log(Expression operand) {
        super(operand);
    }

    @Override
    public double evaluate(Map<String, Double> variables) {
        return Math.log(operand.evaluate(variables));
    }

    @Override
    public Expression differentiate(String varName) {
        // Using the rule: d/dx [ln(f(x))] = f'(x) / f(x)
        return new Divide(operand.differentiate(varName), operand);
    }
    
    @Override
    public String toString() {
        return "ln(" + operand + ")";
    }

    @Override
    public Expression simplify() {
        Expression simplifiedOperand = operand.simplify();
        // If the operand is a constant, we can evaluate it directly
        if (simplifiedOperand instanceof Constant) {
            double value = Math.log(((Constant) simplifiedOperand).getValue());
            return new Constant(value);
        }
        return new Log(simplifiedOperand);
    }

}
