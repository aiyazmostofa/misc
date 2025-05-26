import java.util.HashMap;
// Pretty simple machine, really.
public class Machine {
    private HashMap<String, Double> map;
    public Machine() { map = new HashMap<>(); }
    public double run(Parser.AST ast) throws Exception {
        double result = 0;
        switch (ast.getType()) {
        case ADD:
            return run(ast.getChild(0)) + run(ast.getChild(1));
        case SUB:
            return run(ast.getChild(0)) - run(ast.getChild(1));
        case MUL:
            return run(ast.getChild(0)) * run(ast.getChild(1));
        case DIV:
            return run(ast.getChild(0)) / run(ast.getChild(1));
        case ASSIGN:
            result = run(ast.getChild(1));
            map.put(ast.getChild(0).getContent(), result);
            return result;
        case PRINT:
            result = run(ast.getChild(0));
            System.out.println(result);
            return result;
        case NEG:
            return -run(ast.getChild(0));
        case VAR:
            if (!map.containsKey(ast.getContent())) {
                throw new Exception(String.format(
                    "variable '%s' had not been assigned",
                    ast.getContent()));
            }
            return map.get(ast.getContent());
        case CONST:
            try {
                result = Double.parseDouble(ast.getContent());
            } catch (Exception e) {
                throw new Exception(String.format(
                    "'%s' is not a valid numeric constant",
                    ast.getContent()));
            }
            return result;
        }
        return result;
    }
}
