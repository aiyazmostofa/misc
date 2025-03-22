import java.util.*;
import java.io.*;
import java.math.*;

// http://www.usaco.org/index.php?page=viewproblem2&cpid=944
public class Main {
    HashSet<Integer> visited;
    ArrayList<Integer>[] graph;
    Point[] points;

    void run() {
        int N = in.nextInt();
        int M = in.nextInt();

        points = new Point[N];
        graph = new ArrayList[N];
        for (int i = 0; i < N; i++) {
            points[i] = new Point(in.nextInt(), in.nextInt());
            graph[i] = new ArrayList<>();
        }

        for (int i = 0; i < M; i++) {
            int a = in.nextInt() - 1;
            int b = in.nextInt() - 1;
            graph[a].add(b);
            graph[b].add(a);
        }

        long min = Integer.MAX_VALUE;
        visited = new HashSet<>();
        for (int i = 0; i < N; i++) {
            if (visited.contains(i))
                continue;
            ArrayList<Integer> list = new ArrayList<>();
            dfs(i, list);
            min = Math.min(perim(list), min);
        }
        out.println(min);
    }

    long perim(ArrayList<Integer> list) {
        long minX = Integer.MAX_VALUE;
        long minY = Integer.MAX_VALUE;
        long maxX = Integer.MIN_VALUE;
        long maxY = Integer.MIN_VALUE;

        for (int i : list) {
            minX = Math.min(minX, points[i].x);
            minY = Math.min(minY, points[i].y);
            maxX = Math.max(maxX, points[i].x);
            maxY = Math.max(maxY, points[i].y);
        }

        return 2 * (maxX - minX) + 2 * (maxY - minY);
    }

    void dfs(int v, ArrayList<Integer> list) {
        list.add(v);
        visited.add(v);
        for (int u : graph[v]) {
            if (visited.contains(u))
                continue;
            dfs(u, list);
        }
    }

    class Point {
        int x;
        int y;

        Point(int _x, int _y) {
            x = _x;
            y = _y;
        }
    }

    static String IN = "fenceplan.in";
    static String OUT = "fenceplan.out";
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
