import java.util.*;
import java.io.*;
import java.math.*;

// http://www.usaco.org/index.php?page=viewproblem2&cpid=858
public class Main {
    int[] array;
    int N;
    int M;
    int C;

    void run() {
        N = in.nextInt();
        M = in.nextInt();
        C = in.nextInt();

        array = new int[N];
        for (int i = 0; i < N; i++) {
            array[i] = in.nextInt();
        }
        Arrays.sort(array);

        int l = 0;
        int r = 1_000_000_000;
        int ans = -1;
        while (l <= r) {
            int m = (r - l) / 2 + l;
            if (check(m)) {
                ans = m;
                r = m - 1;
            } else
                l = m + 1;
        }
        out.println(ans);
    }

    boolean check(int m) {
        int count = 1;
        int prev = 0;
        for (int i = 1; i < N; i++) {
            if (array[i] - array[prev] > m || i - prev + 1 > C) {
                count++;
                prev = i;
            }
        }
        return count <= M;
    }

    static String IN = "convention.in";
    static String OUT = "convention.out";
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
