import java.util.*;
import java.util.stream.IntStream;
import java.io.*;
import java.math.*;

// http://www.usaco.org/index.php?page=viewproblem2&cpid=1110
public class Main {
    void run() {
        int N = in.nextInt();
        int[][] array = new int[N][2];
        for (int i = 0; i < N; i++) {
            array[i][0] = in.nextInt() + 1000;
            array[i][1] = in.nextInt() + 1000;
        }

        boolean[][] matrix = new boolean[3000][3000];
        int t = 0;
        Queue<int[]> bfs = new ArrayDeque<>();
        for (int i = 0; i < N; i++) {
            bfs.add(array[i]);
            while (!bfs.isEmpty()) {
                int[] v = bfs.poll();
                if (matrix[v[0]][v[1]])
                    continue;
                t++;
                matrix[v[0]][v[1]] = true;
                for (int j = 0; j < 4; j++) {
                    int sx = v[0] + dx[j];
                    int sy = v[1] + dy[j];
                    int[] rs = check(matrix, sx, sy);
                    if (rs != null)
                        bfs.add(rs);
                }
                int[] rs = check(matrix, v[0], v[1]);
                if (rs != null)
                    bfs.add(rs);
            }
            out.println(t - i - 1);
        }
    }

    int[] dx = { 0, 1, 0, -1 };
    int[] dy = { 1, 0, -1, 0 };

    int[] check(boolean[][] matrix, int x, int y) {
        if (!matrix[x][y]) return null;
        int d = 0;
        int vx = x;
        int vy = y;
        for (int j = 0; j < 4; j++) {
            int sx = dx[j] + x;
            int sy = dy[j] + y;
            if (matrix[sx][sy]) {
                d++;
                vx -= dx[j];
                vy -= dy[j];
            }
        }

        if (d == 3)
            return new int[] { vx, vy };
        return null;
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
