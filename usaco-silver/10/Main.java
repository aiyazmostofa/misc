import java.util.*;
import java.io.*;
import java.math.*;

// http://www.usaco.org/index.php?page=viewproblem2&cpid=967
public class Main {
    void run() {
        int N = in.nextInt();
        int L = in.nextInt();

        Cow[] array = new Cow[N];
        long w = 0;
        PriorityQueue<Cow> pq = new PriorityQueue<>((a, b) -> {
            int c = a.d == -1 ? a.x : (L - a.x);
            int d = b.d == -1 ? b.x : (L - b.x);
            return Integer.compare(c, d);
        });

        for (int i = 0; i < N; i++) {
            array[i] = new Cow(in.nextInt(), in.nextInt(), in.nextInt());
            pq.add(array[i]);
            w += array[i].w;
        }
        Arrays.sort(array, Comparator.comparingInt(c -> c.x));

        w = (long) Math.ceil(w / 2.0);
        int T = 0;
        int l = 0;
        int r = N - 1;
        while (w > 0) {
            Cow cow = pq.poll();
            if (cow.d == -1) {
                T = cow.x;
                w -= array[l++].w;
            } else {
                T = L - cow.x;
                w -= array[r--].w;
            }
        }

        ArrayList<Cow> left = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            if (array[i].d == -1)
                left.add(array[i]);
        }

        Collections.sort(left, Comparator.comparingInt(c -> c.x));

        int coll = 0;
        for (int i = 0; i < N; i++) {
            if (array[i].d == -1)
                continue;
            int s = -1;
            {
                l = 0;
                r = left.size() - 1;
                while (l <= r) {
                    int m = (l + r) / 2;
                    if (array[i].x >= left.get(m).x) {
                        l = m + 1;
                        s = m;
                    } else
                        r = m - 1;
                }
            }

            int e = -1;
            {
                l = 0;
                r = left.size() - 1;
                while (l <= r) {
                    int m = (l + r) / 2;
                    if (array[i].x + T * 2 >= left.get(m).x) {
                        l = m + 1;
                        e = m;
                    } else
                        r = m - 1;
                }
            }

            coll += e - s;
        }
        out.println(coll);
    }

    class Cow {
        int w;
        int x;
        int d;

        Cow(int _w, int _x, int _d) {
            w = _w;
            x = _x;
            d = _d;
        }
    }

    static String IN = "meetings.in";
    static String OUT = "meetings.out";
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
