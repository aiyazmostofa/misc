import java.util.*;
import java.io.*;
import java.math.*;

// http://www.usaco.org/index.php?page=viewproblem2&cpid=1064
public class Main {
    void run() {
        int N = in.nextInt();
        char[] d = new char[N];
        long[] x = new long[N];
        long[] y = new long[N];
        for (int i = 0; i < N; i++) {
            d[i] = in.next().charAt(0);
            x[i] = in.nextLong();
            y[i] = in.nextLong();
        }

        PriorityQueue<IS> pq = new PriorityQueue<>((a, b) -> {
            return Long.compare(a.t, b.t);
        });

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (i == j)
                    continue;
                if (d[i] == 'N') {
                    if (d[j] != 'E' || x[j] > x[i] || y[j] < y[i] || x[i] - x[j] >= y[j] - y[i])
                        continue;
                    IS is = new IS();
                    is.x = x[i];
                    is.y = y[j];
                    is.s = i;
                    is.e = j;
                    is.t = y[j] - y[i];
                    pq.add(is);
                } else {
                    if (d[j] != 'N' || x[j] < x[i] || y[j] > y[i] || y[i] - y[j] >= x[j] - x[i])
                        continue;
                    IS is = new IS();
                    is.x = x[j];
                    is.y = y[i];
                    is.s = i;
                    is.e = j;
                    is.t = x[j] - x[i];
                    pq.add(is);
                }
            }
        }

        ArrayList<Integer>[] graph = new ArrayList[N];
        for (int i = 0; i < N; i++)
            graph[i] = new ArrayList<>();
        boolean[] visited = new boolean[N];
        long[] stop = new long[N];
        while (!pq.isEmpty()) {
            IS is = pq.poll();
            if (visited[is.s])
                continue;
            if (visited[is.e]) {
                if (d[is.s] == 'N' && stop[is.e] < is.x)
                    continue;
                if (d[is.s] == 'E' && stop[is.e] < is.y)
                    continue;
            }
            visited[is.s] = true;
            if (d[is.s] == 'N')
                stop[is.s] = is.y;
            if (d[is.s] == 'E')
                stop[is.s] = is.x;
            graph[is.e].add(is.s);
        }

        for (int i = 0; i < N; i++) {
            int c = 0;
            Stack<Integer> dfs = new Stack<>();
            dfs.add(i);
            while (!dfs.isEmpty()) {
                int v = dfs.pop();
                for (int u : graph[v]) {
                    c++;
                    dfs.add(u);
                }
            }
            out.println(c);
        }
    }

    class IS {
        long x;
        long y;
        int s;
        int e;
        long t;
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
