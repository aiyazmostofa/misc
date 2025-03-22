import java.util.*;
import java.io.*;
import java.math.*;

// http://www.usaco.org/index.php?page=viewproblem2&cpid=1160
public class Main {
    void run() {
        int N = in.nextInt();
        int M = in.nextInt();
        int[][] array = new int[N][2];
        for (int i = 0; i < N; i++) {
            array[i][0] = in.nextInt();
            array[i][1] = in.nextInt();
        }

        long[] start = new long[2 * M + 1];
        long[] freq = new long[M + 1];
        for (int i = 0; i < N; i++)
            freq[array[i][0]]++;
        for (int i = 0; i <= M; i++)
            for (int j = 0; j <= M; j++)
                start[i + j] += freq[i] * freq[j];

        long[] end = new long[2 * M + 1];
        freq = new long[M + 1];
        for (int i = 0; i < N; i++)
            freq[array[i][1]]++;
        for (int i = 0; i <= M; i++)
            for (int j = 0; j <= M; j++)
                end[i + j] += freq[i] * freq[j];

        long c = 0;
        for (int i = 0; i <= 2 * M; i++) {
            c += start[i];
            out.println(c);
            c -= end[i];
        }
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
