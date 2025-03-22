import java.util.*;
import java.io.*;
import java.math.*;

// http://www.usaco.org/index.php?page=viewproblem2&cpid=1112
public class Main {
    void run() {
        int N = in.nextInt();
        int[][] matrix = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                matrix[i][j] = in.nextInt();
            }
        }

        int[][] le = new int[N + 1][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (matrix[i][j] < 100)
                    le[i + 1][j]++;
                le[i + 1][j] += le[i][j];
            }
        }

        int[][] eq = new int[N + 1][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (matrix[i][j] == 100)
                    eq[i + 1][j]++;
                eq[i + 1][j] += eq[i][j];
            }
        }

        long sum = 0;
        for (int i = 1; i <= N; i++) {
            for (int j = i; j <= N; j++) {
                boolean[] l = new boolean[N];
                boolean[] e = new boolean[N];
                for (int k = 0; k < N; k++) {
                    l[k] = le[j][k] - le[i - 1][k] == 0;
                    e[k] = l[k] && eq[j][k] - eq[i - 1][k] > 0;
                }

                int b = 0;
                for (int k = 0; k < N; k++) {
                    if (e[k]) {
                        int a = k;
                        b = Math.max(k, b);
                        while (a > 0 && l[a - 1] && !e[a - 1])
                            a--;
                        while (b < N - 1 && l[b + 1])
                            b++;
                        sum += (b - k + 1) * (k - a + 1);
                    }
                }
            }
        }
        out.println(sum);
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
