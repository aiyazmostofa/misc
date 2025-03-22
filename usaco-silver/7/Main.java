import java.util.*;
import java.io.*;
import java.math.*;

// http://www.usaco.org/index.php?page=viewproblem2&cpid=942
public class Main {
    void run() {
        int N = in.nextInt();
        boolean[][] matrix = new boolean[N][N];
        for (int i = 0; i < N; i++) {
            String s = in.next();
            for (int j = 0; j < N; j++)
                matrix[i][j] = s.charAt(j) == 'R';
        }

        int[] contacts = new int[N];
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                boolean fw = true;
                boolean rv = true;
                for (int l = 0; l < N; l++) {
                    if (matrix[j][l] != matrix[i][l])
                        fw = false;
                    if (matrix[j][l] == matrix[i][l])
                        rv = false;
                }

                if (!fw && !rv) {
                    contacts[i]++;
                    contacts[j]++;
                    break;
                }
            }
        }

        int max = 0;
        for (int i = 0; i < N; i++) {
            if (contacts[i] > contacts[max])
                max = i;
        }

        for (int j = 0; j < N; j++) {
            boolean yes = true;
            matrix[max][j] = !matrix[max][j];
            for (int k = 0; k < N; k++) {
                boolean fw = true;
                boolean rv = true;
                for (int l = 0; l < N; l++) {
                    if (matrix[k][l] != matrix[max][l])
                        fw = false;
                    if (matrix[k][l] == matrix[max][l])
                        rv = false;
                }

                if (!fw && !rv) {
                    yes = false;
                    break;
                }
            }
            if (yes) {
                out.println((max + 1) + " " + (j + 1));
                return;
            }
            matrix[max][j] = !matrix[max][j];
        }

        out.println(-1);
    }

    static String IN = "leftout.in";
    static String OUT = "leftout.out";
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
