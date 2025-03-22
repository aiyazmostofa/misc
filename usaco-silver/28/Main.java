import java.util.*;
import java.io.*;
import java.math.*;

// http://usaco.org/index.php?page=viewproblem2&cpid=1134
public class Main {
    HashSet<Integer> set;
    HashSet<Integer> con;
    int[] p;
    HashSet<Integer>[][] visited;
    int[][][] matrix;

    int BLANK = 0;
    int WALL = 3;
    int O = 1;
    int M = 2;

    void run() {
        set = new HashSet<>();
        p = new int[10];
        p[0] = 1;
        for (int i = 1; i <= 9; i++)
            p[i] = p[i - 1] * 3;
        fill(0, 0);

        int r = 0;
        int c = 0;
        int N = in.nextInt();
        matrix = new int[N][N][3];
        for (int i = 0; i < N; i++) {
            String s = in.next();
            for (int j = 0; j < N; j++) {
                String t = s.substring(j * 3, j * 3 + 3);
                switch (t.charAt(0)) {
                    case '#':
                        matrix[i][j][0] = WALL;
                        break;
                    case 'M':
                        matrix[i][j][0] = M;
                        matrix[i][j][1] = t.charAt(1) - '1';
                        matrix[i][j][2] = t.charAt(2) - '1';
                        break;
                    case 'O':
                        matrix[i][j][0] = O;
                        matrix[i][j][1] = t.charAt(1) - '1';
                        matrix[i][j][2] = t.charAt(2) - '1';
                        break;
                    case 'B':
                        r = i;
                        c = j;
                    default:
                        matrix[i][j][0] = BLANK;
                        break;
                }
            }
        }

        visited = new HashSet[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                visited[i][j] = new HashSet<>();
            }
        }

        con = new HashSet<>();
        visited[r][c].add(0);
        dfs(r, c, 0);
        out.println(con.size());
    }

    int[] dx = { 0, 1, 0, -1 };
    int[] dy = { 1, 0, -1, 0 };

    void dfs(int r, int c, int v) {
        if ((matrix[r][c][0] == M || matrix[r][c][0] == O) && get(v, matrix[r][c][1] * 3 + matrix[r][c][2]) == 0)
            v = set(v, matrix[r][c][1] * 3 + matrix[r][c][2], matrix[r][c][0]);

        if (set.contains(v)) {
            con.add(v);
            return;
        }

        for (int i = 0; i < 4; i++) {
            int sx = r + dx[i];
            int sy = c + dy[i];
            if (matrix[sx][sy][0] == WALL || visited[sx][sy].contains(v))
                continue;
            visited[sx][sy].add(v);
            dfs(sx, sy, v);
        }
    }

    int get(int v, int i) {
        // 123 % 1000 => 123 / 100 => 1
        // 123 % 100 => 23 / 10 => 2
        // 123 % 10 => 3 / 1 => 3
        return v % p[i + 1] / p[i];
    }

    int set(int v, int i, int j) {
        // 120 + 3 => 123
        // 103 + 2 * 10 => 123
        // 23 + 1 * 100 => 123
        return v + p[i] * j;
    }

    void fill(int i, int v) {
        if (i == 9) {
            if (checkWinner(v))
                set.add(v);
            return;
        }

        fill(i + 1, v);
        fill(i + 1, set(v, i, 1));
        fill(i + 1, set(v, i, 2));
    }

    int[][] st = { { 0, 1, 2 }, { 3, 4, 5 }, { 6, 7, 8 }, { 0, 3, 6 }, { 1, 4, 7 }, { 2, 5, 8 }, { 0, 4, 8 },
            { 2, 4, 6 } };

    boolean checkWinner(int n) {
        for (int[] s : st)
            if (get(n, s[0]) == M && get(n, s[1]) == O && get(n, s[2]) == O
                    || get(n, s[0]) == O && get(n, s[1]) == O && get(n, s[2]) == M)
                return true;
        return false;
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
