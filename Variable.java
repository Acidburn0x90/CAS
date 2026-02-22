import java.util.Map;

public class Variable implements Expression {
    private final String name;
    
    public Variable(String name) {
        this.name = name;
    }

    @Override
    public double evaluate(Map<String, Double> variables) {
        if (!variables.containsKey(name)) {
            throw new IllegalArgumentException("Variable '" + name + "' not found.");
        }
        return variables.get(name);
}

    @Override
    public String toString() {
        return name;
    }

    @Override
    public Expression differentiate(String varName) {
        if (this.name.equals(varName)) {
            return new Constant(1);
        } else {
            return new Constant(0);
        }
    }

    @Override
    public Expression simplify() {
        return this; // A variable is already simplified
    }

    @Override
    public boolean equals(Object obj) {
        // 1. If they point to the exact same memory address, they are equal.
        if (this == obj) return true;
        
        // 2. If the other object isn't even a Variable, they cannot be equal.
        if (obj == null || getClass() != obj.getClass()) return false;
        
        // 3. If they are both Variables, cast the other object...
        Variable other = (Variable) obj;
        
        // 4. ...and compare their actual string names.
        return this.name.equals(other.name);
    }

    
    @Override
    public int hashCode() {
        return name.hashCode();
    }

}