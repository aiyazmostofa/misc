import java.util.*;
import java.io.*;
import java.math.*;

// http://www.usaco.org/index.php?page=viewproblem2&cpid=1086
public class Main {
    void run() {
        int N = in.nextInt();
        int M = in.nextInt();
        HashSet<Integer>[] set = new HashSet[N];
        int[] test = new int[N];
        for (int i = 0; i < N; i++) {
            set[i] = new HashSet<>();
            test[i] = i;
            set[i].add(i);
        }

        for (int i = 0; i < M; i++) {
            int a = in.nextInt() - 1;
            int b = in.nextInt() - 1;
            test[a] ^= test[b];
            test[b] ^= test[a];
            test[a] ^= test[b];
            set[test[a]].add(a);
            set[test[b]].add(b);
        }

        int[] idx = new int[N];
        for (int i = 0; i < N; i++) {
            idx[test[i]] = i;
        }

        int[] array = new int[N];
        for (int i = 0; i < N; i++) {
            if (array[i] != 0)
                continue;
            int j = i;
            ArrayList<Integer> list = new ArrayList<>();
            HashSet<Integer> inner = new HashSet<>();
            do {
                list.add(j);
                inner.addAll(set[j]);
                j = idx[j];
            } while (j != i);
            for (int k : list)
                array[k] = inner.size();
        }

        for (int j = 0; j < N; j++)
            out.println(array[j]);
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
