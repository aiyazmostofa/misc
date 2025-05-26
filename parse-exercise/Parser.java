import java.util.ArrayList;
// The grammar of this parser, without any modifications to remove
// left recursion, is as follows.
/*
<target> ::= <print>
<print> ::= PRINT <print> | <assign>
<assign> ::= IDENT EQUAL <print> | <addt>
<addt> ::= <addt> PLUS <mult> | <addt> DASH <mult> | <mult>
<mult> ::= <mult> STAR <neg> | <mult> SLASH <neg> | <neg>
<neg> ::= DASH <neg> | <atom>
<atom> ::= NUMBER | IDENT | <expr>
<expr> ::= LPAREN <print> RPAREN
 */
// To remove left recursion, notice that a rule of the grammar that
// follows the following format "A -> A x B | B" will produce
// expansions such as "B, B x B, B x B x B, ...". This can be dealt
// with in the recursive descent of A by parsing one B automatically,
// then parsing new B's as long as there is a following symbol x,
// building up the tree to match the left recursive pattern. The
// following psuedocode should illustrate the process more clearly.
/*
recA():
  left = recB()
  while (peek() == 'x'):
    pop()
    left = new ast(left, recB())
  return left
 */
public class Parser {
    public static enum ASTType {
        ADD,
        SUB,
        MUL,
        DIV,
        NEG,
        VAR,
        CONST,
        ASSIGN,
        PRINT
    }
    public static class AST {
        private AST[] children;
        private String content;
        private ASTType type;
        // I imposed an arbitrary restriction that an AST can't both
        // have content and multiple children. All this means was that
        // I was too lazy to generate the extra constructor
        public AST(ASTType type, String content) {
            this.type = type;
            this.content = content;
        }
        public AST(ASTType type, AST... children) {
            this.type = type;
            this.children = children;
        }
        public AST getChild(int i) { return children[i]; }
        public String getContent() { return content; }
        public ASTType getType() { return type; }
    }
    public static AST parse(ArrayList<Tokenizer.Token> tokens)
        throws Exception {
        TokenReader tr = new TokenReader(tokens);
        AST result = parsePrint(tr);
        if (tr.hasPeek()) {
            throw new Exception("unexpected extra tokens in the end");
        }
        return result;
    }
    private static class TokenReader {
        private ArrayList<Tokenizer.Token> tokens;
        private int i;
        public TokenReader(ArrayList<Tokenizer.Token> tokens) {
            this.tokens = tokens;
            this.i = 0;
        }
        public Tokenizer.Token peek() throws Exception {
            try {
                return tokens.get(i);
            } catch (Exception e) {
                throw new Exception("reached end before expected");
            }
        }
        public Tokenizer.Token peek(int x) throws Exception {
            try {
                return tokens.get(i + x);
            } catch (Exception e) {
                throw new Exception("reached end before expected");
            }
        }
        public void inc() { i++; }
        public boolean hasPeek() { return i < tokens.size(); }
        public boolean hasPeek(int x) {
            return i + x < tokens.size();
        }
        public int current() { return i; }
    }
    private static AST parseExpr(TokenReader tr) throws Exception {
        if (tr.peek().getType() != Tokenizer.TokenType.LPAREN) {
            throw new Exception(
                String.format("failed at token %d", tr.current()));
        }
        tr.inc();
        AST result = parsePrint(tr);
        if (!tr.hasPeek() ||
            tr.peek().getType() != Tokenizer.TokenType.RPAREN) {
            throw new Exception(String.format(
                "expected matching right parentheses at token %d",
                tr.current()));
        }
        tr.inc();
        return result;
    }
    private static AST parseAtom(TokenReader tr) throws Exception {
        if (tr.peek().getType() == Tokenizer.TokenType.IDENT) {
            AST result = new AST(ASTType.VAR, tr.peek().getContent());
            tr.inc();
            return result;
        } else if (tr.peek().getType() ==
                   Tokenizer.TokenType.NUMBER) {
            AST result =
                new AST(ASTType.CONST, tr.peek().getContent());
            tr.inc();
            return result;
        }
        return parseExpr(tr);
    }
    private static AST parseNeg(TokenReader tr) throws Exception {
        if (tr.peek().getType() == Tokenizer.TokenType.DASH) {
            tr.inc();
            return new AST(ASTType.NEG, parseNeg(tr));
        }
        return parseAtom(tr);
    }
    private static AST parseMult(TokenReader tr) throws Exception {
        AST left = parseNeg(tr);
        while (tr.hasPeek() &&
               (tr.peek().getType() == Tokenizer.TokenType.STAR ||
                tr.peek().getType() == Tokenizer.TokenType.SLASH)) {
            Tokenizer.TokenType operation = tr.peek().getType();
            tr.inc();
            AST right = parseNeg(tr);
            if (operation == Tokenizer.TokenType.STAR) {
                left = new AST(ASTType.MUL, left, right);
            } else {
                left = new AST(ASTType.DIV, left, right);
            }
        }
        return left;
    }
    private static AST parseAddt(TokenReader tr) throws Exception {
        AST left = parseMult(tr);
        while (tr.hasPeek() &&
               (tr.peek().getType() == Tokenizer.TokenType.PLUS ||
                tr.peek().getType() == Tokenizer.TokenType.DASH)) {
            Tokenizer.TokenType operation = tr.peek().getType();
            tr.inc();
            AST right = parseMult(tr);
            if (operation == Tokenizer.TokenType.PLUS) {
                left = new AST(ASTType.ADD, left, right);
            } else {
                left = new AST(ASTType.SUB, left, right);
            }
        }
        return left;
    }
    private static AST parseAssign(TokenReader tr) throws Exception {
        if (tr.peek().getType().equals(Tokenizer.TokenType.IDENT) &&
            tr.hasPeek(1) &&
            tr.peek(1).getType().equals(Tokenizer.TokenType.EQUAL)) {
            AST left = parseAtom(tr);
            tr.inc();
            return new AST(ASTType.ASSIGN, left, parsePrint(tr));
        }
        return parseAddt(tr);
    }
    private static AST parsePrint(TokenReader tr) throws Exception {
        if (tr.peek().getType().equals(Tokenizer.TokenType.PRINT)) {
            tr.inc();
            return new AST(ASTType.PRINT, parsePrint(tr));
        }
        return parseAssign(tr);
    }
}
