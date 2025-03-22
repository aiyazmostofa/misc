import java.util.*;
import java.io.*;
import java.math.*;

// http://www.usaco.org/index.php?page=viewproblem2&cpid=1135
public class Main {
    int N;
    long[] array;
    HashSet<Result> set;

    void run() {
        int T = in.nextInt();
        while (T-- > 0) {
            set = new HashSet<>();
            N = in.nextInt();
            array = new long[N];
            for (int i = 0; i < N; i++)
                array[i] = in.nextLong();
            Arrays.sort(array);
            rec(N - 1, new ArrayList<>(N), new HashSet<>());
            out.println(set.size());
        }
    }

    void rec(int index, ArrayList<Integer> list, HashSet<Integer> v) {
        if (index == -1) {
            int i = 2;
            long[] p = null;
            while (i < N && p == null) {
                p = solve(list.get(0), list.get(1), list.get(i), N - 1, N - 2, N - i - 1);
                i++;
            }
            if (p == null || p[0] < 1 || p[1] < 1 || p[2] < 1)
                return;
            for (i = 0; i < N; i++) {
                long a = (list.get(i) & 1) > 0 ? p[0] : 0;
                long b = (list.get(i) & 2) > 0 ? p[1] : 0;
                long d = (list.get(i) & 4) > 0 ? p[2] : 0;
                if (a + b + d != array[N - 1 - i])
                    return;
            }
            set.add(new Result(p));
            return;
        }

        for (int i = 1; i <= 7; i++) {
            if (v.contains(i)) continue;
            v.add(i);
            list.add(i);
            rec(index - 1, list, v);
            list.remove(list.size() - 1);
            v.remove(i);
        }
    }

    class Result {
        long a;
        long b;
        long c;

        Result(long... ls) {
            Arrays.sort(ls);
            a = ls[0];
            b = ls[1];
            c = ls[2];
        }

        @Override
        public int hashCode() {
            int result = (int) (a ^ (a >>> 32));
            result = 31 * result + (int) (b ^ (b >>> 32));
            result = 31 * result + (int) (c ^ (c >>> 32));
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            Result r = (Result) obj;
            return a == r.a && b == r.b && c == r.c;
        }

        @Override
        public String toString() {
            return "(" + a + ", " + b + ", " + c + ")";
        }
    }

    long[] solve(int a, int b, int c, int i, int j, int k) {
        long[][] co = new long[3][4];
        co[0][0] = (a & 1) > 0 ? 1 : 0;
        co[0][1] = (a & 2) > 0 ? 1 : 0;
        co[0][2] = (a & 4) > 0 ? 1 : 0;
        co[0][3] = array[i];

        co[1][0] = (b & 1) > 0 ? 1 : 0;
        co[1][1] = (b & 2) > 0 ? 1 : 0;
        co[1][2] = (b & 4) > 0 ? 1 : 0;
        co[1][3] = array[j];

        co[2][0] = (c & 1) > 0 ? 1 : 0;
        co[2][1] = (c & 2) > 0 ? 1 : 0;
        co[2][2] = (c & 4) > 0 ? 1 : 0;
        co[2][3] = array[k];

        long d = det(new long[][] {
                { co[0][0], co[0][1], co[0][2] },
                { co[1][0], co[1][1], co[1][2] },
                { co[2][0], co[2][1], co[2][2] },
        });

        long d1 = det(new long[][] {
                { co[0][3], co[0][1], co[0][2] },
                { co[1][3], co[1][1], co[1][2] },
                { co[2][3], co[2][1], co[2][2] },
        });

        long d2 = det(new long[][] {
                { co[0][0], co[0][3], co[0][2] },
                { co[1][0], co[1][3], co[1][2] },
                { co[2][0], co[2][3], co[2][2] },
        });

        long d3 = det(new long[][] {
                { co[0][0], co[0][1], co[0][3] },
                { co[1][0], co[1][1], co[1][3] },
                { co[2][0], co[2][1], co[2][3] },
        });

        if (d == 0)
            return null;
        return new long[] { d1 / d, d2 / d, d3 / d };
    }

    long det(long[][] m) {
        return m[0][0] * (m[1][1] * m[2][2] - m[2][1] * m[1][2])
                - m[0][1] * (m[1][0] * m[2][2] - m[1][2] * m[2][0])
                + m[0][2] * (m[1][0] * m[2][1] - m[1][1] * m[2][0]);
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
