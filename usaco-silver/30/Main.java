import java.util.*;
import java.io.*;
import java.math.*;

// http://www.usaco.org/index.php?page=viewproblem2&cpid=1136
public class Main {
    void run() {
        int N = in.nextInt();
        int K = in.nextInt();
        int L = in.nextInt();
        int[] array = new int[N];
        for (int i = 0; i < N; i++)
            array[i] = in.nextInt();
        Arrays.sort(array);

        TreeMap<Integer, Integer> map = new TreeMap<>();
        for (int i = 0; i < N; i++) {
            map.putIfAbsent(array[i], i);
        }

        int ans = 0;
        int l = 1;
        int r = 100_000;
        while (l <= r) {
            int m = (l + r) / 2;
            int d;
            Integer k = map.ceilingKey(m);
            if (k == null)
                d = N;
            else
                d = map.get(k);
            int s = N - d;
            long total = (long) K * L;
            for (int i = d - 1; i >= 0; i--) {
                long v = Math.min(Math.min(K, total), m - array[i]);
                total -= v;
                if (array[i] + v >= m)
                    s++;
            }

            if (s >= m) {
                ans = m;
                l = m + 1;
            } else
                r = m - 1;
        }
        out.println(ans);
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
