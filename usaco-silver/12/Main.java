import java.util.*;
import java.io.*;
import java.math.*;

// http://www.usaco.org/index.php?page=viewproblem2&cpid=990
public class Main {
    void run() {
        int N = in.nextInt();
        int K = in.nextInt();
        int[] array = new int[N];
        for (int i = 0; i < N; i++) {
            array[i] = in.nextInt();
        }

        Arrays.sort(array);
        int B = array[N - 1];
        int max = 0;
        for (int i = 1; i <= B; i++) {
            int count = 0;
            for (int j = N - 1; j >= 0 && array[j] >= i && count < K; j--)
                count += array[j] / i;
            count = Math.min(K, count);
            ArrayList<Integer> temp = new ArrayList<>();
            for (int j = 0; j < count; j++)
                temp.add(i);

            PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.reverseOrder());
            for (int j : array)
                pq.add(j % i);
            while (count < K && !pq.isEmpty()) {
                temp.add(pq.poll());
                count++;
            }

            int sum = 0;
            for (int j = K / 2; j < temp.size(); j++) {
                sum += temp.get(j);
            }
            max = Math.max(max, sum);
        }

        out.println(max);
    }

    static String IN = "berries.in";
    static String OUT = "berries.out";
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
