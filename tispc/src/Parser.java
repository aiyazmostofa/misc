import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
	static Pattern uint64 = Pattern.compile(
			"0|[1-9][0-9]{0,18}|1[0-7][0-9]{0,18}|18[0-3][0-9]{0,17}|184[0-3][0-9]{0,16}|1844[0-5][0-9]{0,15}|18446[0-6][0-9]{0,14}|184467[0-3][0-9]{0,13}|1844674[0-3][0-9]{0,12}|184467440[0-6][0-9]{0,10}|1844674407[0-2][0-9]{0,9}|18446744073[0-6][0-9]{0,8}|1844674407370[0-8][0-9]{0,6}|18446744073709[0-4][0-9]{0,5}|184467440737095[0-4][0-9]{0,4}|1844674407370955[0-0][0-9]{0,3}|18446744073709551[0-5][0-9]{0,2}|184467440737095516[0-0][0-9]{0,1}|1844674407370955161[0-4][0-9]{0,0}|18446744073709551615");
	static HashMap<String, FunctionFormat> formatMap = new HashMap<>() {
		{
			put("+", new FunctionFormat(new AstType[] { AstType.SYM, AstType.NUM,
					AstType.NUM, AstType.CLOSE },
					AstType.NUM));
			put("-", new FunctionFormat(new AstType[] { AstType.SYM, AstType.NUM,
					AstType.NUM, AstType.CLOSE },
					AstType.NUM));
			put("*", new FunctionFormat(new AstType[] { AstType.SYM, AstType.NUM,
					AstType.NUM, AstType.CLOSE },
					AstType.NUM));
			put("/", new FunctionFormat(new AstType[] { AstType.SYM, AstType.NUM,
					AstType.NUM, AstType.CLOSE },
					AstType.NUM));
			put("+f", new FunctionFormat(new AstType[] { AstType.SYM, AstType.NUM,
					AstType.NUM, AstType.CLOSE },
					AstType.NUM));
			put("-f", new FunctionFormat(new AstType[] { AstType.SYM, AstType.NUM,
					AstType.NUM, AstType.CLOSE },
					AstType.NUM));
			put("*f", new FunctionFormat(new AstType[] { AstType.SYM, AstType.NUM,
					AstType.NUM, AstType.CLOSE },
					AstType.NUM));
			put("/f", new FunctionFormat(new AstType[] { AstType.SYM, AstType.NUM,
					AstType.NUM, AstType.CLOSE },
					AstType.NUM));
			put("|", new FunctionFormat(new AstType[] { AstType.SYM, AstType.NUM,
					AstType.NUM, AstType.CLOSE },
					AstType.NUM));
			put("^", new FunctionFormat(new AstType[] { AstType.SYM, AstType.NUM,
					AstType.NUM, AstType.CLOSE },
					AstType.NUM));
			put("&", new FunctionFormat(new AstType[] { AstType.SYM, AstType.NUM,
					AstType.NUM, AstType.CLOSE },
					AstType.NUM));
			put("<<", new FunctionFormat(new AstType[] { AstType.SYM, AstType.NUM,
					AstType.NUM, AstType.CLOSE },
					AstType.NUM));
			put(">>", new FunctionFormat(new AstType[] { AstType.SYM, AstType.NUM,
					AstType.NUM, AstType.CLOSE },
					AstType.NUM));
			put("~", new FunctionFormat(
					new AstType[] { AstType.SYM, AstType.NUM, AstType.CLOSE },
					AstType.NUM));
			put("<", new FunctionFormat(new AstType[] { AstType.SYM, AstType.NUM,
					AstType.NUM, AstType.CLOSE },
					AstType.NUM));
			put(">", new FunctionFormat(new AstType[] { AstType.SYM, AstType.NUM,
					AstType.NUM, AstType.CLOSE },
					AstType.NUM));
			put("array", new FunctionFormat(
					new AstType[] { AstType.SYM, AstType.NUM, AstType.CLOSE },
					AstType.NUM));
			put("get", new FunctionFormat(new AstType[] { AstType.SYM, AstType.NUM, AstType.CLOSE },
					AstType.NUM));
			put("let", new FunctionFormat(
					new AstType[] { AstType.SYM, AstType.SYM, AstType.NUM, AstType.CLOSE },
					AstType.VOID));
			put("set", new FunctionFormat(new AstType[] { AstType.SYM, AstType.NUM,
					AstType.NUM,
					AstType.CLOSE },
					AstType.VOID));
			put("in", new FunctionFormat(
					new AstType[] { AstType.SYM, AstType.NUM, AstType.CLOSE },
					AstType.NUM));
			put("out", new FunctionFormat(new AstType[] { AstType.SYM, AstType.NUM,
					AstType.NUM, AstType.CLOSE },
					AstType.VOID));
			put("while",
					new FunctionFormat(new AstType[] { AstType.SYM, AstType.NUM,
							AstType.VOID, AstType.KLEENE },
							AstType.VOID));
			put("if", new FunctionFormat(new AstType[] { AstType.SYM, AstType.NUM,
					AstType.VOID, AstType.KLEENE },
					AstType.VOID));
		}
	};

	public static AST parse(ArrayList<Token> tokens, GlobalInt p) {
		if (tokens.get(p.x).type != TokenType.LP) {
			UnitAST uast = new UnitAST();
			uast.token = tokens.get(p.x);
			if (uast.token.type == TokenType.SYM) {
				uast.type = AstType.SYM;
			} else {
				uast.type = AstType.NUM;
				Matcher m = uint64.matcher(uast.token.data);
				if (!m.matches())
					throw new RuntimeException("Invalid integer constant.");
			}
			return uast;
		}
		AST ast = new AST();
		p.x++;
		while (tokens.get(p.x).type != TokenType.RP) {
			ast.children.add(parse(tokens, p));
			p.x++;
		}
		if (ast.children.get(0).type != AstType.SYM)
			throw new RuntimeException("Not valid function.");
		UnitAST uast = (UnitAST) ast.children.get(0);
		FunctionFormat format = formatMap.get(uast.token.data);
		if (format == null)
			throw new RuntimeException("Not valid function.");
		int j = 0;
		for (int i = 0; i < ast.children.size(); i++) {
			if (format.children[j] == AstType.CLOSE) {
				throw new RuntimeException("Too many arguments.");
			}
			if (format.children[j] == AstType.KLEENE) {
				j--;
			}
			if (format.children[j] == AstType.NUM) {
				if (ast.children.get(i).type != AstType.NUM &&
						ast.children.get(i).type != AstType.SYM) {
					throw new RuntimeException("Invalid parameter.");
				}
				if (uast.token.data.equals("array") &&
						(!(ast.children.get(i) instanceof UnitAST) ||
								ast.children.get(i).type != AstType.NUM)) {
					throw new RuntimeException("Array parameter needs const.");
				}
			} else {
				if (ast.children.get(i).type != format.children[j]) {
					throw new RuntimeException("Invalid parameter.");
				}
			}
			j++;
		}
		if (format.children[j] != AstType.CLOSE &&
				format.children[j] != AstType.KLEENE) {
			throw new RuntimeException("Too few arguments");
		}
		ast.type = format.returnType;
		return ast;
	}

	public static String printAST(AST ast) {
		if (ast instanceof UnitAST) {
			UnitAST uast = (UnitAST) ast;
			return uast.token.data;
		} else {
			String result = "(";
			for (int i = 0; i < ast.children.size(); i++) {
				result += printAST(ast.children.get(i));
				if (i < ast.children.size() - 1) {
					result += " ";
				}
			}
			result += ")";
			return result;
		}
	}
}

class AST {
	AstType type;
	ArrayList<AST> children = new ArrayList<>();
}

class UnitAST extends AST {
	Token token;
}

enum AstType {
	SYM, VOID, NUM, KLEENE, CLOSE
}

class FunctionFormat {
	AstType[] children;
	AstType returnType;

	FunctionFormat(AstType[] children, AstType returnType) {
		this.children = children;
		this.returnType = returnType;
	}
}
