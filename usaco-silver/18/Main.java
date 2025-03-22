import java.util.*;
import java.io.*;
import java.math.*;

// http://www.usaco.org/index.php?page=viewproblem2&cpid=1039
public class Main {
    void run() {
        int N = in.nextInt();
        int M = in.nextInt();
        int[][] cows = new int[N][2];
        for (int i = 0; i < N; i++) {
            cows[i][0] = in.nextInt() - 1;
            cows[i][1] = in.nextInt() - 1;
        }

        TreeSet<Integer>[] buk = new TreeSet[M];
        for (int i = 0; i < M; i++) {
            buk[i] = new TreeSet<>();
        }

        for (int i = 0; i < N; i++) {
            if (buk[cows[i][0]].size() == 0) {
                buk[cows[i][0]].add(i);
            } else {
                buk[cows[i][0]].add(i);
                buk[cows[i][1]].add(i);
            }
        }

        int count = 0;
        for (int i = 0; i < M; i++)
            if (buk[i].size() > 0)
                count++;

        for (int i = 0; i < N; i++) {
            out.println(count);
            buk[cows[i][0]].remove(i);
            if (buk[cows[i][0]].size() == 0)
                count--;
            int p = cows[i][0];
            while (buk[p].size() > 0 && cows[buk[p].first()][0] == p) {
                int v = buk[p].first();
                int q = cows[v][1];
                if (!buk[q].contains(v))
                    break;
                buk[q].remove(v);
                if (buk[q].size() == 0)
                    count--;
                p = q;
            }
        }
    }

    static String IN = "cereal.in";
    static String OUT = "cereal.out";
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
