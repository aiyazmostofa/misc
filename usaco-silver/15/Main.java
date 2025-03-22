import java.util.*;
import java.io.*;
import java.math.*;

// http://www.usaco.org/index.php?page=viewproblem2&cpid=1015
public class Main {
    void run() {
        int N = in.nextInt();
        Point[] array = new Point[N];
        for (int i = 0; i < N; i++) {
            array[i] = new Point(in.nextLong(), in.nextLong());
        }

        HashMap<Long, Storage> xMap = new HashMap<>();
        HashMap<Long, Storage> yMap = new HashMap<>();

        for (Point p : array) {
            xMap.putIfAbsent(p.x, new Storage());
            xMap.get(p.x).list.add(p.y);
            yMap.putIfAbsent(p.y, new Storage());
            yMap.get(p.y).list.add(p.x);
        }

        for (long x : xMap.keySet())
            xMap.get(x).comp();

        for (long y : yMap.keySet())
            yMap.get(y).comp();

        long sum = 0;
        for (Point p : array) {
            long[] xA = xMap.get(p.x).pref(p.y);
            long[] yA = yMap.get(p.y).pref(p.x);

            long xL = xA[2] * p.y - xA[0];
            long xG = xA[1] - (xA[3] - xA[2] - 1) * p.y;

            long yL = yA[2] * p.x - yA[0];
            long yG = yA[1] - (yA[3] - yA[2] - 1) * p.x;

            sum += (xL * yL) + (xG * yL) + (xL * yG) + (xG * yG);
            sum %= 1_000_000_007;
        }
        out.println(sum);
    }

    class Point {
        long x;
        long y;

        Point(long _x, long _y) {
            x = _x;
            y = _y;
        }
    }

    class Storage {
        ArrayList<Long> list = new ArrayList<>();
        private ArrayList<Long> pref;

        void comp() {
            pref = new ArrayList<>();
            Collections.sort(list);
            pref.add(0L);
            for (int i = 0; i < list.size(); i++) {
                pref.add(list.get(i) + pref.get(i));
            }
        }

        long[] pref(long v) {
            int l = 0;
            int r = list.size() - 1;
            int ans = -1;
            while (l <= r) {
                int m = (l + r) / 2;
                if (list.get(m) == v) {
                    ans = m;
                    break;
                } else if (list.get(m) < v) {
                    l = m + 1;
                } else {
                    r = m - 1;
                }
            }

            return new long[] {
                    pref.get(ans),
                    pref.get(list.size()) - pref.get(ans + 1),
                    ans,
                    list.size()
            };
        }
    }

    static String IN = "triangles.in";
    static String OUT = "triangles.out";
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
