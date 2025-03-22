import java.util.*;
import java.util.stream.IntStream;
import java.io.*;
import java.math.*;

// http://www.usaco.org/index.php?page=viewproblem2&cpid=1159
public class Main {
    void run() {
        int T = in.nextInt();
        while (T-- > 0) {
            int N = in.nextInt();
            int M = in.nextInt();
            int[] uf = IntStream.range(0, N).toArray();
            int[] sz = new int[N];
            Arrays.fill(sz, 1);
            for (int i = 0; i < M; i++) {
                int a = in.nextInt() - 1;
                int b = in.nextInt() - 1;
                union(uf, sz, a, b);
            }
            HashMap<Integer, TreeSet<Integer>> map = new HashMap<>();
            for (int i = 0; i < N; i++) {
                int j = find(uf, i);
                if (!map.containsKey(j))
                    map.put(j, new TreeSet<>());
                map.get(j).add(i);
            }

            int s = find(uf, 0);
            int e = find(uf, N - 1);
            long min = closest(map.get(s), map.get(e));
            for (int i : map.keySet()) {
                int j = find(uf, i);
                if (s == j || e == j)
                    continue;
                long a = closest(map.get(s), map.get(j));
                long b = closest(map.get(e), map.get(j));
                min = Math.min(min, a + b);
            }
            out.println(min);
        }
    }

    long closest(TreeSet<Integer> a, TreeSet<Integer> b) {
        long min = Integer.MAX_VALUE;
        if (b.size() < a.size()) {
            TreeSet<Integer> c = a;
            a = b;
            b = c;
        }

        for (int n : a) {
            Integer k = b.floor(n);
            if (k != null)
                min = Math.min(n - k, min);
            k = b.ceiling(n);
            if (k != null)
                min = Math.min(k - n, min);
        }
        return min * min;
    }

    void union(int[] uf, int[] sz, int i, int j) {
        i = find(uf, i);
        j = find(uf, j);
        if (i == j) return;
        if (sz[i] > sz[j]) {
            uf[j] = i;
            sz[i] += sz[j];
        } else {
            uf[i] = j;
            sz[j] += sz[i];
        }
    }

    int find(int[] uf, int i) {
        while (i != uf[i]) {
            uf[i] = uf[uf[i]];
            i = uf[i];
        }
        return i;
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
