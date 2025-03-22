import java.util.*;
import java.io.*;
import java.math.*;

// http://www.usaco.org/index.php?page=viewproblem2&cpid=860
public class Main {
    int N;
    int M;
    int K;
    char[][] matrix;

    void run() {
        N = in.nextInt();
        M = 10;
        K = in.nextInt();
        matrix = new char[N][M];
        for (int i = 0; i < N; i++) {
            matrix[i] = in.next().toCharArray();
        }
        gravity();
        ArrayList<Integer> matches = new ArrayList<>();
        while ((matches = matches()).size() > 0) {
            for (int i = 0; i < matches.size(); i += 2)
                dfs2(matches.get(i), matches.get(i + 1), matrix[matches.get(i)][matches.get(i + 1)]);
            gravity();
        }
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                out.print(matrix[i][j]);
            }
            out.println();
        }
    }

    ArrayList<Integer> matches() {
        ArrayList<Integer> matches = new ArrayList<>();
        boolean[][] visited = new boolean[N][M];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (!visited[i][j] && matrix[i][j] != '0') {
                    int count = dfs1(i, j, matrix[i][j], visited);
                    if (count >= K) {
                        matches.add(i);
                        matches.add(j);
                    }
                }
            }
        }
        return matches;
    }

    int[] dx = { 0, -1, 0, 1 };
    int[] dy = { -1, 0, 1, 0 };

    int dfs1(int i, int j, char c, boolean[][] visited) {
        int count = 1;
        visited[i][j] = true;
        for (int k = 0; k < 4; k++) {
            int sx = dx[k] + i;
            int sy = dy[k] + j;
            if (sx < 0 || sx >= N || sy < 0 || sy >= M)
                continue;
            if (visited[sx][sy])
                continue;
            if (matrix[sx][sy] != c)
                continue;
            count += dfs1(sx, sy, c, visited);
        }
        return count;
    }

    void dfs2(int i, int j, char c) {
        matrix[i][j] = '0';
        for (int k = 0; k < 4; k++) {
            int sx = dx[k] + i;
            int sy = dy[k] + j;
            if (sx < 0 || sx >= N || sy < 0 || sy >= M)
                continue;
            if (matrix[sx][sy] != c)
                continue;
            dfs2(sx, sy, c);
        }
    }

    void gravity() {
        for (int j = 0; j < M; j++) {
            ArrayList<Character> list = new ArrayList<>();
            for (int i = N - 1; i >= 0; i--) {
                if (matrix[i][j] != '0')
                    list.add(matrix[i][j]);
            }
            for (int i = 0; i < list.size(); i++) {
                matrix[N - 1 - i][j] = list.get(i);
            }
            for (int i = list.size(); i < N; i++) {
                matrix[N - i - 1][j] = '0';
            }
        }
    }

    static String IN = "mooyomooyo.in";
    static String OUT = "mooyomooyo.out";
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
