import java.util.*;
import java.io.*;
import java.math.*;

// http://www.usaco.org/index.php?page=viewproblem2&cpid=1350
public class Main {
    void run() {
        int N = in.nextInt();
        long M = in.nextLong();
        long K = in.nextLong();
        long[][] array = new long[N][2];
        TreeMap<Long, Long> map = new TreeMap<>();
        for (int i = 0; i < N; i++) {
            array[i][0] = in.nextLong();
            array[i][1] = in.nextLong();
            map.put(array[i][0], array[i][1]);
        }

        Arrays.sort(array, Comparator.comparingLong(v -> v[0]));
        HashMap<Long, Integer> index = new HashMap<>();
        for (int i = 0; i < N; i++)
            index.put(array[i][0], i);

        long t = 0;
        int p = Integer.MAX_VALUE;
        for (int i = N - 1; i >= 0; i--) {
            long w = array[i][0];
            long a = array[i][1];
            long v = Math.min(M - t, map.get(w));
            map.merge(w, -v, Long::sum);
            t += v;
            Long temp = map.floorKey(w - K);
            if (temp == null)
                continue;
            p = Math.min(p, index.get(temp));
            long s = a - map.get(w);
            while (p >= 0 && s > 0) {
                v = Math.min(s, map.get(array[p][0]));
                s -= Math.min(s, map.get(array[p][0]));
                map.merge(array[p][0], -v, Long::sum);
                if (map.get(array[p][0]) == 0)
                    p--;
            }
        }

        long c = 0;
        for (long[] cow : array)
            c += cow[1] - map.get(cow[0]);

        out.println(c);
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
