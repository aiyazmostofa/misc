import java.util.*;
import java.io.*;
import java.math.*;

// http://www.usaco.org/index.php?page=viewproblem2&cpid=894
public class Main {
    int[] colors;
    ArrayList<Integer>[] graph;
    int N;

    void run() {
        N = in.nextInt();
        graph = new ArrayList[N];
        colors = new int[N];
        Arrays.fill(colors, -1);
        for (int i = 0; i < N; i++)
            graph[i] = new ArrayList<>();
        for (int i = 0; i < N - 1; i++) {
            int a = in.nextInt() - 1;
            int b = in.nextInt() - 1;
            graph[a].add(b);
            graph[b].add(a);
        }

        colors[0] = 1;
        dfs(0, 0);
        int max = 0;
        for (int i : colors) max = Math.max(i, max);
        out.println(max);
    }

    int dfs(int v, int prev) {
        int lowest = 1;
        for (int u : graph[v]) {
            if (colors[u] != -1)
                continue;
            while (lowest == colors[v] || lowest == colors[prev])
                lowest++;
            colors[u] = lowest;
            lowest++;
        }

        for (int u : graph[v]) {
            if (u == prev)
                continue;
            dfs(u, v);
        }
        return lowest;
    }

    static String IN = "planting.in";
    static String OUT = "planting.out";
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
