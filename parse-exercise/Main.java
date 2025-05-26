import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        if (args.length > 1) {
            System.err.println(
                "You can't provide more than ONE argument.");
        } else if (args.length == 1) {
            fileRun(args[0]);
        } else {
            repl();
        }
    }
    private static void fileRun(String path) {
        String source;
        try {
            source = Files.readString(Path.of(path));
        } catch (Exception e) {
            System.err.printf("Could not open '%s'\n.", path);
            return;
        }
        String[] lines = source.split("\n");
        @SuppressWarnings("unchecked")
        ArrayList<Tokenizer.Token>[] tokensArray =
            new ArrayList[lines.length];
        boolean error = false;
        for (int i = 0; i < lines.length; i++) {
            try {
                tokensArray[i] = Tokenizer.tokenize(lines[i]);
            } catch (Exception e) {
                System.err.printf("Error at line %d, %s.\n", i + 1,
                                  e.getMessage());
                error = true;
            }
        }
        if (error) {
            return;
        }
        Parser.AST[] astsArray = new Parser.AST[tokensArray.length];
        for (int i = 0; i < tokensArray.length; i++) {
            if (tokensArray[i].size() == 0) {
                continue;
            }
            try {
                astsArray[i] = Parser.parse(tokensArray[i]);
            } catch (Exception e) {
                System.err.printf("Error at line %d, %s.\n", i + 1,
                                  e.getMessage());
                error = true;
            }
        }
        if (error) {
            return;
        }
        Machine machine = new Machine();
        for (int i = 0; i < astsArray.length; i++) {
            if (astsArray[i] == null) {
                continue;
            }
            try {
                machine.run(astsArray[i]);
            } catch (Exception e) {
                System.err.printf("Error at line %d, %s.\n", i + 1,
                                  e.getMessage());
                return;
            }
        }
    }
    private static void repl() {
        System.out.println("""
Welcome to <peepee poopoo> REPL! Press enter on an empty input to quit.""");
        Scanner in = new Scanner(System.in);
        Machine machine = new Machine();
        while (true) {
            System.out.print("> ");
            String line = in.nextLine();
            if (line.trim().length() == 0) {
                break;
            }
            try {
                machine.run(Parser.parse(Tokenizer.tokenize(line)));
            } catch (Exception e) {
                System.err.printf("Error: %s.\n", e.getMessage());
                continue;
            }
        }
        in.close();
    }
}
