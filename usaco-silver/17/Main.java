import java.util.*;
import java.io.*;
import java.math.*;

// http://www.usaco.org/index.php?page=viewproblem2&cpid=1038
public class Main {
    long[][] array;
    int N;
    int M;

    void run() {
        N = in.nextInt();
        M = in.nextInt();
        array = new long[M][2];
        for (int i = 0; i < M; i++) {
            array[i][0] = in.nextLong();
            array[i][1] = in.nextLong();
        }

        Arrays.sort(array, (a, b) -> {
            return Long.compare(a[0], b[0]);
        });

        long l = 1;
        long r = 1_000_000_000L * 1_000_000_000L;
        long ans = -1;
        while (l <= r) {
            long m = (r - l) / 2 + l;
            if (check(m)) {
                ans = m;
                l = m + 1;
            } else r = m - 1;
        }
        out.println(ans);
    }

    boolean check(long m) {
        int count = 0;
        long min = 0;
        for (int i = 0; i < M && count < N; i++) {
            long p = Math.max(min, array[i][0]);
            while (count < N && p <= array[i][1]) {
                count++;
                p += m;
            }
            min = p;
        }
        return count == N;
    }

    static String IN = "socdist.in";
    static String OUT = "socdist.out";
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
