import java.util.*;
import java.io.*;
import java.math.*;

// http://www.usaco.org/index.php?page=viewproblem2&cpid=1014
public class Main {
    void run() {
        int N = in.nextInt();
        int M = in.nextInt();
        int K = in.nextInt();
        int[] array = new int[N];
        for (int i = 0; i < N; i++)
            array[i] = i;

        for (int i = 0; i < M; i++) {
            int a = in.nextInt() - 1;
            int b = in.nextInt() - 1;
            for (int j = 0; j < (b - a + 1) / 2; j++) {
                array[a + j] ^= array[a + (b - a + 1) - 1 - j];
                array[a + (b - a + 1) - 1 - j] ^= array[a + j];
                array[a + j] ^= array[a + (b - a + 1) - 1 - j];
            }
        }

        HashMap<Integer, ArrayList<Integer>> map = new HashMap<>();
        int[] from = new int[N];
        int[] cycles = new int[N];
        Arrays.fill(from, -1);
        for (int i = 0; i < N; i++) {
            if (from[i] != -1)
                continue;
            ArrayList<Integer> list = new ArrayList<>();
            int c = i;
            int count = 0;
            do {
                from[c] = count++;
                list.add(c);
                cycles[c] = i;
                c = array[c];
            } while (c != i);
            map.put(i, list);
        }

        for (int i = 0; i < N; i++) {
            out.println(map.get(cycles[i]).get((from[i] + K) % map.get(cycles[i]).size()) + 1);
        }
    }

    static String IN = "swap.in";
    static String OUT = "swap.out";
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
