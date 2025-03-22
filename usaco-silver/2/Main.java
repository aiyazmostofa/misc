import java.util.*;
import java.io.*;
import java.math.*;

// http://www.usaco.org/index.php?page=viewproblem2&cpid=859
public class Main {
    void run() {
        int N = in.nextInt();
        int[][] matrix = new int[N][3];
        for (int i = 0; i < N; i++) {
            matrix[i][0] = in.nextInt();
            matrix[i][1] = in.nextInt();
            matrix[i][2] = i;
        }

        Arrays.sort(matrix, (a, b) -> Integer.compare(a[0], b[0]));

        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[2], b[2]));
        pq.add(matrix[0]);
        int i = 1;
        int max = 0;
        int time = 0;
        while (!pq.isEmpty()) {
            int[] cow = pq.poll();
            max = Math.max(Math.max(time, cow[0]) - cow[0], max);
            time = Math.max(time, cow[0]) + cow[1];
            while (i < N && (matrix[i][0] <= time || pq.isEmpty()))
                pq.add(matrix[i++]);
        }
        out.println(max);
    }

    static String IN = "convention2.in";
    static String OUT = "convention2.out";
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
