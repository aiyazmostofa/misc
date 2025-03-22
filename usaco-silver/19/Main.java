import java.util.*;
import java.io.*;
import java.math.*;

// http://www.usaco.org/index.php?page=viewproblem2&cpid=1040
public class Main {
    void run() {
        int N = in.nextInt();
        int[][] array = new int[N][2];
        for (int i = 0; i < N; i++) {
            array[i][0] = in.nextInt();
            array[i][1] = in.nextInt();
        }

        Arrays.sort(array, (a, b) -> {
            int result = Integer.compare(a[0], b[0]);
            if (result == 0)
                result = Integer.compare(a[1], b[1]);
            return result;
        });

        int[] l = new int[N];
        l[0] = array[0][1];
        for (int i = 1; i < N; i++)
            l[i] = Math.min(l[i - 1], array[i][1]);

        int[] r = new int[N];
        r[N - 1] = array[N - 1][1];
        for (int i = N - 2; i >= 0; i--)
            r[i] = Math.max(r[i + 1], array[i][1]);

        int count = 1;
        for (int i = 0; i < N - 1; i++)
            if (l[i] > r[i + 1])
                count++;
        out.println(count);
    }

    static String IN = "moop.in";
    static String OUT = "moop.out";
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
