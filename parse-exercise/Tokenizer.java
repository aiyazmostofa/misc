import java.util.ArrayList;
// You might notice that numbers have a very lazy tokenization
// process. That is, as long as you have dots and numbers, you will
// match fine. The actual verification is done by 'Double.parseDouble'
// when the interpreter (machine) runs the code.
public class Tokenizer {
    public static enum TokenType {
        PLUS,
        DASH,
        STAR,
        SLASH,
        EQUAL,
        IDENT,
        LPAREN,
        RPAREN,
        PRINT,
        NUMBER
    }
    public static class Token {
        private final TokenType type;
        private final String content;
        public Token(TokenType type, String content) {
            this.type = type;
            this.content = content;
        }
        public TokenType getType() { return type; }
        public String getContent() { return content; }
    }
    public static ArrayList<Token> tokenize(String line)
        throws Exception {
        ArrayList<Token> tokens = new ArrayList<>();
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == '+') {
                tokens.add(new Token(TokenType.PLUS, "+"));
            } else if (line.charAt(i) == '-') {
                tokens.add(new Token(TokenType.DASH, "-"));
            } else if (line.charAt(i) == '*') {
                tokens.add(new Token(TokenType.STAR, "*"));
            } else if (line.charAt(i) == '/') {
                tokens.add(new Token(TokenType.SLASH, "/"));
            } else if (line.charAt(i) == '=') {
                tokens.add(new Token(TokenType.EQUAL, "="));
            } else if (line.charAt(i) == '(') {
                tokens.add(new Token(TokenType.LPAREN, "("));
            } else if (line.charAt(i) == ')') {
                tokens.add(new Token(TokenType.RPAREN, ")"));
            } else if (isLetter(line.charAt(i))) {
                StringBuilder sb =
                    new StringBuilder("" + line.charAt(i));
                while (i + 1 < line.length() &&
                       (isLetter(line.charAt(i + 1)) ||
                        isNumber(line.charAt(i + 1)))) {
                    i++;
                    sb.append(line.charAt(i));
                }
                String s = sb.toString();
                if (s.toLowerCase().equals("print")) {
                    tokens.add(
                        new Token(TokenType.PRINT, sb.toString()));
                } else {
                    tokens.add(
                        new Token(TokenType.IDENT, sb.toString()));
                }
            } else if (isNumber(line.charAt(i)) ||
                       line.charAt(i) == '.') {
                StringBuilder sb =
                    new StringBuilder("" + line.charAt(i));
                while (i + 1 < line.length() &&
                       (isNumber(line.charAt(i + 1)) ||
                        line.charAt(i + 1) == '.')) {
                    i++;
                    sb.append(line.charAt(i));
                }
                tokens.add(
                    new Token(TokenType.NUMBER, sb.toString()));
            } else if (!Character.isWhitespace(line.charAt(i))) {
                throw new Exception(String.format(
                    "column %d: '%c'", i + 1, line.charAt(i)));
            }
        }
        return tokens;
    }
    private static boolean isLetter(char c) {
        return ('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z');
    }
    private static boolean isNumber(char c) {
        return '0' <= c && c <= '9';
    }
}
