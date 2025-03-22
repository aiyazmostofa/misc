import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class Main {
	public static void main(String[] args) throws IOException {
		ArrayList<Token> tokens = Scanner.scan(Files.readString(Path.of(args[0])));
		if (!Scanner.checkBalance(tokens)) {
			throw new RuntimeException("Parentheses not balanced.");
		}
		ArrayList<AST> asts = new ArrayList<>();
		GlobalInt p = new GlobalInt();
		while (p.x < tokens.size()) {
			try {
				asts.add(Parser.parse(tokens, p));
			} catch (Exception e) {
				System.out.println("Error @ " + p.x);
				throw e;
			}
			p.x++;
		}
		Generator.generateCode(args[1], asts);
	}
}
