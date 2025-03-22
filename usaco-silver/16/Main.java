import java.util.*;
import java.io.*;
import java.math.*;

// http://www.usaco.org/index.php?page=viewproblem2&cpid=1016
public class Main {
    ArrayList<Integer>[] graph;
    int[] times;
    int[] temp;

    void run() {
        int N = in.nextInt();
        graph = new ArrayList[N];
        times = new int[N];
        for (int i = 0; i < N; i++) {
            times[i] = in.nextInt();
            graph[i] = new ArrayList<>();
        }
        for (int i = 0; i < N - 1; i++) {
            int a = in.nextInt() - 1;
            int b = in.nextInt() - 1;
            graph[a].add(b);
            graph[b].add(a);
        }

        if (N == 1) {
            out.println(0);
            return;
        }

        int count = 0;
        for (int i = 0; i < N; i++) {
            temp = Arrays.copyOf(times, N);
            if (dfs(i, -1))
                count++;
        }
        out.println(count);
    }

    boolean dfs(int v, int p) {
        for (int i = 0; i < graph[v].size(); i++) {
            int u = graph[v].get(i);
            if (u == p)
                continue;
            temp[u] = add(temp[u]);
            dfs(u, v);
            if (!(temp[v] == 12 && p == -1 && i == graph[v].size() - 1)) {
                temp[v] = add(temp[v]);
            }
        }

        if (p != -1) {
            while (temp[v] != 12) {
                temp[p] = add(temp[p]);
                temp[v] = add(temp[v]);
            }
        }

        return temp[v] == 12;
    }

    int add(int val) {
        val++;
        if (val == 13)
            return 1;
        return val;
    }

    static String IN = "clocktree.in";
    static String OUT = "clocktree.out";
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
