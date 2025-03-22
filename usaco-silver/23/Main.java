import java.util.*;
import java.io.*;
import java.math.*;

// http://www.usaco.org/index.php?page=viewproblem2&cpid=1087&lang=en
public class Main {
    void run() {
        int N = in.nextInt();
        int M = in.nextInt();
        String s = in.next();
        int[] array = new int[N];
        for (int i = 0; i < N; i++)
            array[i] = s.charAt(i) - 'A';
        int[][] pre = new int[26][N + 1];
        TreeSet<Integer>[] set = new TreeSet[26];
        for (int i = 0; i < 26; i++) set[i] = new TreeSet<>();
        for (int i = 0; i < N; i++)
            set[array[i]].add(i);

        for (int i = 0; i < 26; i++) {
            boolean started = false;
            for (int j = 0; j < N; j++) {
                if (array[j] == i && !started) {
                    started = true;
                    pre[i][j + 1]++;
                }

                if (started && array[j] < i)
                    started = false;
                pre[i][j + 1] += pre[i][j];
            }
        }

        while (M-- > 0) {
            int a = in.nextInt();
            int b = in.nextInt();
            int sum = 0;
            for (int i = 0; i < 26; i++) {
                sum += (pre[i][a - 1] - pre[i][0]) + (pre[i][N] - pre[i][b]);
                Integer ceil = set[i].ceiling(b);
                if (ceil == null) continue;
                if (pre[i][ceil+1] == pre[i][b]) sum++;
            }
            out.println(sum);
        }
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
