import java.util.*;
import java.io.*;
import java.math.*;

// http://www.usaco.org/index.php?page=viewproblem2&cpid=920
public class Main {
    int N;
    int M;
    ArrayList<E>[] graph;
    boolean[] visited;
    boolean[] color;

    void run() {
        N = in.nextInt();
        M = in.nextInt();
        visited = new boolean[N];
        color = new boolean[N];
        graph = new ArrayList[N];
        for (int i = 0; i < N; i++)
            graph[i] = new ArrayList<>();
        for (int i = 0; i < M; i++) {
            boolean s = in.next().charAt(0) == 'S';
            int a = in.nextInt() - 1;
            int b = in.nextInt() - 1;
            graph[a].add(new E(b, s));
            graph[b].add(new E(a, s));
        }

        boolean valid = true;
        int count = 0;
        for (int i = 0; i < N; i++) {
            if (!visited[i]) {
                valid &= dfs(i, true);
                count++;
            }
        }

        if (valid) {
            out.print(1);
            for (int i = 0; i < count; i++)
                out.print(0);
            out.println();
        } else
            out.println(0);
    }

    boolean dfs(int v, boolean c) {
        visited[v] = true;
        color[v] = c;
        for (E e : graph[v]) {
            if (visited[e.v]) {
                if (e.s) {
                    if (c != color[e.v])
                        return false;
                } else {
                    if (c == color[e.v])
                        return false;
                }
                continue;
            }
            if (!dfs(e.v, e.s ? c : !c))
                return false;
        }
        return true;
    }

    class E {
        int v;
        boolean s;

        public E(int _v, boolean _s) {
            v = _v;
            s = _s;
        }
    }

    static String IN = "revegetate.in";
    static String OUT = "revegetate.out";
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
