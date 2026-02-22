public abstract class UnaryOperation implements Expression {
    protected final Expression operand;

    public UnaryOperation(Expression operand) {
        this.operand = operand;
    }
}
