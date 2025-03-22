import java.util.*;
import java.io.*;
import java.math.*;

// http://www.usaco.org/index.php?page=viewproblem2&cpid=1088
public class Main {
    void run() {
        int N = in.nextInt();
        int[][] matrix = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                matrix[i][j] = in.nextInt();

        int[] one = new int[N];
        int[] zero = new int[N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (i % 2 == 1)
                    one[j] += matrix[i][j];
                else
                    zero[j] += matrix[i][j];
            }
        }

        int a = 0;
        for (int i = 0; i < N; i++)
            a += Math.max(one[i], zero[i]);

        one = new int[N];
        zero = new int[N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (j % 2 == 1)
                    one[i] += matrix[i][j];
                else
                    zero[i] += matrix[i][j];
            }
        }

        int b = 0;
        for (int i = 0; i < N; i++)
            b += Math.max(one[i], zero[i]);

        out.println(Math.max(a, b));
    }

    static String IN = "";
    static String OUT = "";
    Katti in;
    PrintWriter out;

    Main(InputStream is, OutputStream os) {
        in = new Katti(is);
        out = new PrintWriter(os);
    }

    public static void main(String[] args) throws IOException {
        Main main = new Main(
                IN.length() == 0 || System.getenv("CP_ENJOYER") != null ? System.in
                        : new FileInputStream(IN),
                OUT.length() == 0 || System.getenv("CP_ENJOYER") != null ? System.out
                        : new FileOutputStream(OUT));
        main.run();
        main.in.br.close();
        main.out.close();
    }

    class Katti {
        BufferedReader br;
        StringTokenizer st;

        Katti(InputStream is) {
            br = new BufferedReader(new InputStreamReader(is));
        }

        String next() {
            try {
                while (st == null || !st.hasMoreTokens())
                    st = new StringTokenizer(br.readLine());
                return st.nextToken();
            } catch (Exception ignored) {
            }
            return null;
        }

        int nextInt() {
            return Integer.parseInt(next());
        }

        long nextLong() {
            return Long.parseLong(next());
        }

        double nextDouble() {
            return Double.parseDouble(next());
        }
    }
}
