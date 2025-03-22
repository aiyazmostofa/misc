import java.util.*;
import java.io.*;
import java.math.*;

// http://www.usaco.org/index.php?page=viewproblem2&cpid=1351
public class Main {
    void run() {
        int N = in.nextInt();
        int K = in.nextInt();

        int[] a = new int[K];
        int[] b = new int[K];

        HashSet<Integer> aSet = new HashSet<>();
        HashSet<Integer> bSet = new HashSet<>();

        for (int i = 0; i < K; i++) {
            a[i] = in.nextInt() - 1;
            aSet.add(a[i]);
        }

        for (int i = 0; i < K; i++) {
            b[i] = in.nextInt() - 1;
            bSet.add(b[i]);
        }

        int[] bx = new int[N];
        for (int i = 0; i < K; i++)
            bx[b[i]] = i;

        int count = 0;
        for (int i = 0; i < N; i++) {
            if (!aSet.contains(i) && !bSet.contains(i))
                count++;
        }

        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < K; i++) {
            if (bSet.contains(a[i])) {
                if (bx[a[i]] < i)
                    map.merge(i - bx[a[i]], 1, Integer::sum);
                else
                    map.merge(K - bx[a[i]] + i, 1, Integer::sum);
            }
        }

        int max = 0;
        for (int k : map.keySet())
            max = Math.max(map.get(k), max);

        for (int i = 0; i < K / 2; i++) {
            b[i] ^= b[K - i - 1];
            b[K - i - 1] ^= b[i];
            b[i] ^= b[K - i - 1];
        }

        for (int i = 0; i < K; i++)
            bx[b[i]] = i;

        map = new HashMap<>();
        for (int i = 0; i < K; i++) {
            if (bSet.contains(a[i])) {
                if (bx[a[i]] < i)
                    map.merge(i - bx[a[i]], 1, Integer::sum);
                else
                    map.merge(K - bx[a[i]] + i, 1, Integer::sum);
            }
        }

        for (int k : map.keySet())
            max = Math.max(map.get(k), max);

        out.println(count + max);
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
