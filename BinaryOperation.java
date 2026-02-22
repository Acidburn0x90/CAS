// This is abstract class for binary operations (like addition, multiplication, etc.) that implements the Expression interface.
//  It contains two protected fields for the left and right expressions, and a constructor to initialize them.
// They are abstract because we will have specific classes for each type of binary operation (like Addition, Multiplication, etc.)
// that will extend this class and implement the evaluate, differentiate, and toString methods according to the specific operation they represent.

public abstract class BinaryOperation implements Expression {
    protected final Expression left;
    protected final Expression right;

    public BinaryOperation(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }
    public Expression getLeft() { return this.left; }
    public Expression getRight() { return this.right; }
}