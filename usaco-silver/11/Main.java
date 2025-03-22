import java.util.*;
import java.io.*;
import java.math.*;

// http://www.usaco.org/index.php?page=viewproblem2&cpid=968
public class Main {
    void run() {
        int N = in.nextInt();
        int M = in.nextInt();
        String s = in.next();
        int[] uf = new int[N];
        for (int i = 0; i < N; i++)
            uf[i] = i;
        int[] sz = new int[N];
        Arrays.fill(sz, 1);
        for (int i = 0; i < N - 1; i++) {
            int a = in.nextInt() - 1;
            int b = in.nextInt() - 1;
            if (s.charAt(a) == s.charAt(b))
                union(uf, sz, a, b);
        }

        for (int i = 0; i < M; i++) {
            int a = in.nextInt() - 1;
            int b = in.nextInt() - 1;
            char type = in.next().charAt(0);
            out.print(type == s.charAt(b) || type == s.charAt(a) || find(uf, a) != find(uf, b) ? 1 : 0);
        }
        out.println();
    }

    void union(int[] uf, int[] sz, int i, int j) {
        i = find(uf, i);
        j = find(uf, j);
        if (i == j)
            return;
        if (sz[i] > sz[j]) {
            uf[j] = i;
            sz[i] += sz[j];
        } else {
            uf[i] = j;
            sz[j] += sz[i];
        }
    }

    int find(int[] uf, int i) {
        while (uf[i] != i) {
            uf[i] = uf[uf[i]];
            i = uf[i];
        }
        return i;
    }

    static String IN = "milkvisits.in";
    static String OUT = "milkvisits.out";
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
