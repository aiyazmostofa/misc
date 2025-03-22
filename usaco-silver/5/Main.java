import java.util.*;
import java.io.*;
import java.math.*;

// http://www.usaco.org/index.php?page=viewproblem2&cpid=895
public class Main {
    boolean[][] matrix;
    boolean[][] visited;
    int N;

    void run() {
        N = in.nextInt();
        matrix = new boolean[N][N];
        for (int i = 0; i < N; i++) {
            String next = in.next();
            for (int j = 0; j < N; j++) {
                matrix[i][j] = next.charAt(j) == '#';
            }
        }

        visited = new boolean[N][N];
        int[] max = { 0, 0 };
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (!matrix[i][j] || visited[i][j])
                    continue;
                int[] result = bfs(i, j);
                if (result[0] > max[0]) {
                    max = result;
                } else if (result[0] == max[0])
                    max[1] = Math.min(result[1], max[1]);
            }
        }
        out.println(max[0] + " " + max[1]);
    }

    int[] dx = { 1, 0, -1, 0 };
    int[] dy = { 0, -1, 0, 1 };

    int[] bfs(int i, int j) {
        Queue<Integer> bfs = new ArrayDeque<>();
        bfs.add(i);
        bfs.add(j);
        int area = 0;
        int perimeter = 0;
        visited[i][j] = true;
        while (!bfs.isEmpty()) {
            int r = bfs.poll();
            int c = bfs.poll();
            area++;
            for (int k = 0; k < 4; k++) {
                int sx = r + dx[k];
                int sy = c + dy[k];
                if (sx < 0 || sx >= N || sy < 0 || sy >= N || !matrix[sx][sy]) {
                    perimeter++;
                    continue;
                }
                if (visited[sx][sy])
                    continue;
                visited[sx][sy] = true;
                bfs.add(sx);
                bfs.add(sy);
            }
        }

        return new int[] { area, perimeter };
    }

    static String IN = "perimeter.in";
    static String OUT = "perimeter.out";
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
