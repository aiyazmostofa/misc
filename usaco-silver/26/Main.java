import java.util.*;
import java.io.*;
import java.math.*;

// http://www.usaco.org/index.php?page=viewproblem2&cpid=1111
public class Main {
    void run() {
        int N = in.nextInt();
        int K = in.nextInt();
        int[] array = new int[N+1];
        for (int i = 0; i < N; i++)
            array[i] = -in.nextInt();
        array[N] = 0;
        Arrays.sort(array);
        for (int i = 0; i <= N; i++)
            array[i] = -array[i];

        int pointer = (array[0] + 11) / 12 * 12;
        int sum = pointer;
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (int i = 0; i < N; i++) {
            int og = array[i] - array[i + 1];
            int s = array[i] / 12 * 12;
            if (s < array[i + 1])
                continue;
            int a = array[i] - s;
            s = (array[i + 1] + 11) / 12 * 12;
            if (s > array[i])
                continue;
            int b = s - array[i + 1];
            pq.add(a + b - og);
        }

        for (int i = 0; i < K - 1 && !pq.isEmpty(); i++) {
            int v = pq.poll();
            if (v > 0)
                break;
            sum += v;
        }
        out.println(sum);
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
