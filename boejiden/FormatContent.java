import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
public class FormatContent {
  public static void main(String[] args) throws Exception {
    File directory = new File("./src/content/solutions");
    for (File file : directory.listFiles()) {
      String fileName = file.getName();
      if (!fileName.endsWith(".md")) continue;
      System.out.println(fileName);
      String fileContent = readFile(file);
      int curr = 0, prev = 0;
      StringBuilder sb = new StringBuilder();
      while ((curr = fileContent.indexOf("```c++", curr)) != -1) {
        sb.append(fileContent.substring(prev, curr));
        int next = fileContent.indexOf("```", curr + 1);
        writeFile(new File("main.cpp"), "#include <bits/stdc++.h>\n" +
                                          fileContent.substring(curr + 6, next)
                                            .replaceAll("#include <.*?>", ""));
        new ProcessBuilder("clang-format", "-i", "main.cpp").start().waitFor();
        sb.append("```c++\n")
          .append(readFile(new File("main.cpp")).trim())
          .append("\n");
        new File("main.cpp").delete();
        curr = prev = next;
      }
      sb.append(fileContent.substring(prev));
      writeFile(file, sb.toString());
    }
  }
  private static void writeFile(File f, String content) throws Exception {
    PrintWriter out = new PrintWriter(f);
    out.print(content);
    out.close();
  }
  private static String readFile(File f) throws Exception {
    Scanner in = new Scanner(f);
    StringBuilder sb = new StringBuilder();
    while (in.hasNextLine()) sb.append(in.nextLine()).append("\n");
    in.close();
    return sb.toString();
  }
}
