import java.util.Map;

public class Constant implements Expression {
    private final double value;

    public Constant(double value) {
        this.value = value;
    }

    @Override
    public double evaluate(Map<String, Double> variables) {
        return value;
    }

    @Override
    public Expression differentiate(String varName) {
        return new Constant(0);
    }

    @Override
    public String toString() {
        // If the number has no fractional part, print it as an integer
        if (this.value % 1 == 0) {
            return String.valueOf((long) this.value);
        }
        // Otherwise, print the full decimal
        return String.valueOf(this.value);
    }

    @Override
    public Expression simplify() {
        return this; // A constant is already simplified
    }

    // Inside the Constant class:
    public double getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object obj) {
        // 1. If they point to the exact same memory address, they are equal.
        if (this == obj) return true;
        
        // 2. If the other object isn't even a Constant, they cannot be equal.
        if (obj == null || getClass() != obj.getClass()) return false;

        // 3. If they are both Constants, cast the other object...
        Constant other = (Constant) obj;
        
        // 4. ...and compare their actual values.
        return this.value == other.value;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }

}