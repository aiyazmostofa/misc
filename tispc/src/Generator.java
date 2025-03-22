import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Generator {
	private static HashMap<String, String> postInstructions = new HashMap<>() {
		{
			put("+", "\tadd r0, r0, r1\n");
			put("-", "\tsub r0, r0, r1\n");
			put("*", "\tmul r0, r0, r1\n");
			put("/", "\tdiv r0, r0, r1\n");
			put("+f", "\taddf r0, r0, r1\n");
			put("-f", "\tsubf r0, r0, r1\n");
			put("*f", "\tmulf r0, r0, r1\n");
			put("/f", "\tdivf r0, r0, r1\n");
			put("|", "\tor r0, r0, r1\n");
			put("^", "\txor r0, r0, r1\n");
			put("&", "\tand r0, r0, r1\n");
			put("<<", "\tlshft r0, r0, r1\n");
			put(">>", "\trshft r0, r0, r1\n");
			put("set", "\tmov (r0)(0), r1\n");
			put("out", "\tout r0, r1\n");
		}
	};

	private static void handleBinaryOp(PrintWriter out, AST ast, HashMap<String, Integer> map,
			Stack<String> variables,
			Stack<Integer> depths, int depth, GlobalInt sp) {
		generateCode(out, ast.children.get(1), map, variables, depths, depth + 1, sp);
		out.printf("\tpush r0\n");
		sp.x += 8;
		generateCode(out, ast.children.get(2), map, variables, depths, depth + 1, sp);
		out.printf("\tmov r1, r0\n");
		out.printf("\tpop r0\n");
		sp.x -= 8;
		UnitAST uast = (UnitAST) (ast.children.get(0));
		String key = uast.token.data;
		if (postInstructions.containsKey(key)) {
			out.printf("%s", postInstructions.get(key));
		} else {
			Label label = new Label();
			out.printf("\tclr r2\n");
			out.printf("\tld r3, %s\n", label);
			if (key.equals("<")) {
				out.printf("\tbrgt r3, r1, r0\n");
			} else {
				out.printf("\tbrgt r3, r0, r1\n");
			}
			out.printf("\tbrr 8\n");
			out.printf("%s\n", label);
			out.printf("\tnot r2, r2\n");
			out.printf("\tmov r0, r2\n");
		}
	}

	private static void generateCode(PrintWriter out, AST ast, HashMap<String, Integer> map,
			Stack<String> variables,
			Stack<Integer> depths, int depth, GlobalInt sp) {
		if (ast instanceof UnitAST) {
			UnitAST uast = (UnitAST) ast;
			if (ast.type == AstType.NUM) {
				out.printf("\tld r0, %s\n", uast.token.data);
			} else {
				if (map.containsKey(uast.token.data)) {
					out.printf("\tld r1, %d\n", sp.x - map.get(uast.token.data));
					out.printf("\tadd r1, r31, r1\n");
					out.printf("\tmov r0, (r1)(0)\n");
				} else {
					throw new RuntimeException("Value not found.");
				}
			}
			return;
		}
		UnitAST uast = (UnitAST) ast.children.get(0);
		switch (uast.token.data) {
			case "let":
				generateCode(out, ast.children.get(2), map, variables, depths, depth, sp);
				String key = ((UnitAST) ast.children.get(1)).token.data;
				if (map.containsKey(key)) {
					out.printf("\tld r1, %d\n", sp.x - map.get(key));
					out.printf("\tadd r1, r31, r1\n");
					out.printf("\tmov (r1)(0), r0\n");
				} else {
					out.printf("\tpush r0\n");
					variables.add(key);
					depths.add(depth);
					sp.x += 8;
					map.put(key, sp.x);
				}
				break;
			case "while":
				Label bodyL = new Label(), condL = new Label();
				out.printf("\tld r0, %s\n", condL);
				out.printf("\tbr r0\n");
				out.printf("%s\n", bodyL);
				for (int i = 2; i < ast.children.size(); i++) {
					generateCode(out, ast.children.get(i), map, variables, depths, depth + 1, sp);
				}
				int oldsp = sp.x;
				while (!depths.isEmpty() && depths.peek() == depth + 1) {
					depths.pop();
					map.remove(variables.pop());
				}
				if (variables.isEmpty()) {
					sp.x = 0;
				} else {
					sp.x = map.get(variables.peek());
				}
				out.printf("\taddi r31, %d\n", oldsp - sp.x);
				out.printf("%s\n", condL);
				generateCode(out, ast.children.get(1), map, variables, depths, depth + 1, sp);
				out.printf("\tld r1, %s\n", bodyL);
				out.printf("\tbrnz r1, r0\n");
				break;
			case "if":
				Label startL = new Label(), endL = new Label();
				generateCode(out, ast.children.get(1), map, variables, depths, depth + 1, sp);
				out.printf("\tld r1, %s\n", startL);
				out.printf("\tbrnz r1, r0\n");
				out.printf("\tld r1, %s\n", endL);
				out.printf("\tbr r1\n");
				out.printf("%s\n", startL);
				for (int i = 2; i < ast.children.size(); i++) {
					generateCode(out, ast.children.get(i), map, variables, depths, depth + 1, sp);
				}
				oldsp = sp.x;
				while (!depths.isEmpty() && depths.peek() == depth + 1) {
					depths.pop();
					map.remove(variables.pop());
				}
				if (variables.isEmpty()) {
					sp.x = 0;
				} else {
					sp.x = map.get(variables.peek());
				}
				out.printf("\taddi r31, %d\n", oldsp - sp.x);
				out.printf("%s\n", endL);
				break;
			case "array":
				out.printf("\tld r0, %s\n", ((UnitAST) ast.children.get(1)).token.data);
				out.printf("\tsub r31, r31, r0\n");
				out.printf("\tmov r0, r31\n");
				sp.x += Long.parseLong(((UnitAST) ast.children.get(1)).token.data);
				Reference ref = new Reference();
				variables.add(ref.toString());
				map.put(ref.toString(), sp.x);
				depths.add(depth);
				break;
			case "get":
				generateCode(out, ast.children.get(1), map, variables, depths, depth + 1, sp);
				out.printf("\tmov r0, (r0)(0)\n");
				break;
			case "in":
				generateCode(out, ast.children.get(1), map, variables, depths, depth + 1, sp);
				out.printf("\tin r0, r0\n");
				break;
			case "~":
				generateCode(out, ast.children.get(1), map, variables, depths, depth + 1, sp);
				out.printf("\tnot r0, r0\n");
				break;
			default:
				handleBinaryOp(out, ast, map, variables, depths, depth, sp);
				break;
		}
	}

	public static void generateCode(String outputName, ArrayList<AST> asts) throws FileNotFoundException {
		PrintWriter out = new PrintWriter(outputName);
		HashMap<String, Integer> map = new HashMap<>();
		Stack<String> variables = new Stack<>();
		Stack<Integer> depths = new Stack<>();
		GlobalInt sp = new GlobalInt();
		out.printf(".code\n");
		for (AST ast : asts) {
			generateCode(out, ast, map, variables, depths, 0, sp);
		}
		out.printf("\thalt\n");
		out.close();
	}
}

class Label {
	static int total;
	int id;

	Label() {
		id = total++;
	}

	public String toString() {
		return ":L" + id;
	}
}

class Reference {
	static int total;
	int id;

	Reference() {
		id = total++;
	}

	public String toString() {
		return "(" + id + ")";
	}
}
