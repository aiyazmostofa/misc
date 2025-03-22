import java.util.*;
import java.io.*;
import java.math.*;
import java.awt.geom.*;

// http://www.usaco.org/index.php?page=viewproblem2&cpid=943
public class Main {
    int X1 = 0;
    int Y1 = 1;
    int X2 = 2;
    int Y2 = 3;

    void run() {
        int N = in.nextInt();
        int[][] matrix = new int[N][4];
        ArrayList<int[]> xEvents = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            matrix[i][X1] = in.nextInt();
            matrix[i][Y1] = in.nextInt();
            matrix[i][X2] = in.nextInt();
            matrix[i][Y2] = in.nextInt();
            if (matrix[i][X1] < matrix[i][X2]) {
                xEvents.add(new int[] { 1, i, matrix[i][Y1], matrix[i][Y2], matrix[i][X1] });
                xEvents.add(new int[] { -1, i, matrix[i][Y1], matrix[i][Y2], matrix[i][X2] });
            } else {
                xEvents.add(new int[] { 1, i, matrix[i][Y2], matrix[i][Y1], matrix[i][X2] });
                xEvents.add(new int[] { -1, i, matrix[i][Y2], matrix[i][Y1], matrix[i][X1] });
            }
        }

        Collections.sort(xEvents, (a, b) -> {
            int result = Integer.compare(a[4], b[4]);
            if (result == 0)
                result = Integer.compare(b[0], a[0]);
            return result;
        });

        TreeMap<Integer, Integer> yEvents = new TreeMap<>();
        int a = -1;
        int b = -1;
        for (int[] e : xEvents) {
            if (e[0] == 1) {
                Integer temp = yEvents.ceilingKey(e[2]);
                if (temp != null && intersects(matrix[yEvents.get(temp)], matrix[e[1]])) {
                    a = e[1];
                    b = yEvents.get(temp);
                    break;
                }

                temp = yEvents.floorKey(e[2]);
                if (temp != null && intersects(matrix[yEvents.get(temp)], matrix[e[1]])) {
                    a = e[1];
                    b = yEvents.get(temp);
                    break;
                }
                yEvents.put(e[2], e[1]);
            } else {
                yEvents.remove(e[2]);
                Integer lower = yEvents.lowerKey(e[3]);
                Integer higher = yEvents.higherKey(e[3]);
                if (lower != null && higher != null
                        && intersects(matrix[yEvents.get(lower)], matrix[yEvents.get(higher)])) {
                    a = yEvents.get(lower);
                    b = yEvents.get(higher);
                    break;
                }
            }
        }

        if (a == -1) {
            out.println(1);
            return;
        }

        int c = 0;
        int d = 0;
        for (int i = 0; i < N; i++) {
            if (intersects(matrix[a], matrix[i]))
                c++;
            if (intersects(matrix[b], matrix[i]))
                d++;
        }

        if (c == d) {
            out.println(Math.min(a, b) + 1);
        } else if (c > d) {
            out.println(a + 1);
        } else {
            out.println(b + 1);
        }
    }

    boolean intersects(int[] a, int[] b) {
        return Line2D.linesIntersect(a[0], -a[1], a[2], -a[3], b[0], -b[1], b[2], -b[3]);
    }

    static String IN = "cowjump.in";
    static String OUT = "cowjump.out";
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
