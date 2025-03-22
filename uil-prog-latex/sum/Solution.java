import java.io.File;
import java.util.Scanner;
public class Solution {
  public static void main(String[] args) throws Exception {
    Scanner in = new Scanner(new File("sum.dat"));
    int N = in.nextInt(), sum = 0;
    for (int i = 0; i < N; i++) { sum += in.nextInt(); }
    System.out.print(sum);
    in.close();
  }
}
