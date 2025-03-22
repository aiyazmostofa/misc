import java.util.ArrayList;

public class Scanner {
	public static ArrayList<Token> scan(String code) {
		ArrayList<Token> tokens = new ArrayList<>();
		for (int i = 0; i < code.length(); i++) {
			if (code.charAt(i) == '(') {
				tokens.add(new Token(TokenType.LP, "("));
			} else if (code.charAt(i) == ')') {
				tokens.add(new Token(TokenType.RP, ")"));
			} else if (Character.isDigit(code.charAt(i))) {
				int j = i;
				while (j < code.length() && Character.isDigit(code.charAt(j))) {
					j++;
				}
				tokens.add(new Token(TokenType.NUM, code.substring(i, j)));
				i = j - 1;
			} else if (!Character.isWhitespace(code.charAt(i))) {
				int j = i;
				while (j < code.length() && !Character.isWhitespace(code.charAt(j)) &&
						code.charAt(j) != '(' && code.charAt(j) != ')') {
					j++;
				}
				tokens.add(new Token(TokenType.SYM, code.substring(i, j)));
				i = j - 1;
			}
		}
		return tokens;
	}

	public static boolean checkBalance(ArrayList<Token> tokens) {
		int open = 0;
		for (Token token : tokens) {
			if (token.type == TokenType.LP)
				open++;
			if (token.type == TokenType.RP)
				open--;
			if (open < 0)
				return false;
		}
		return open == 0;
	}
}

enum TokenType {
	LP, RP, NUM, SYM
}

class Token {
	TokenType type;
	String data;

	Token(TokenType token, String data) {
		this.type = token;
		this.data = data;
	}
}
