import java.util.*;
import java.io.*;
import java.math.*;

// http://www.usaco.org/index.php?page=viewproblem2&cpid=1352
public class Main {
    int T, C;
    char[] s;
    HashSet<Integer> targets;
    int[] pos;

    void run() {
        T = in.nextInt();
        C = in.nextInt();
        targets = new HashSet<>();
        for (int i = 0; i < T; i++)
            targets.add(in.nextInt());
        s = in.next().toCharArray();
        int p = 0;
        pos = new int[C];
        for (int i = 0; i < C; i++) {
            if (s[i] == 'R')
                p++;
            else if (s[i] == 'L')
                p--;
            pos[i] = p;
        }

        int m = nothing();
        m = Math.max(m, rightToFire());
        m = Math.max(m, leftToFire());
        m = Math.max(m, fireToLeft());
        m = Math.max(m, fireToRight());
        m = Math.max(m, rightToLeft());
        m = Math.max(m, leftToRight());
        out.println(m);
    }

    int nothing() {
        HashSet<Integer> set = new HashSet<>();
        for (int i = 0; i < T; i++)
            if (s[i] == 'F' && targets.contains(pos[i]))
                set.add(pos[i]);
        return set.size();
    }

    int rightToFire() {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < C; i++)
            if (s[i] == 'F' && targets.contains(pos[i]))
                add(map, pos[i]);
        int m = 0;
        int prev = C;
        for (int i = C - 1; i >= 0; i--) {
            int p = pos[i];
            if (s[i] == 'F') {
                if (targets.contains(p - 1))
                    add(map, p - 1);
                remove(map, p);
            } else if (s[i] == 'R') {
                if (targets.contains(p - 1))
                    add(map, p - 1);
                if (prev != C) 
                    remove(map, pos[prev] - 1);
                prev = i;
                m = Math.max(m, map.size());
            }
        }
        return m;
    }

    int leftToFire() {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < C; i++)
            if (s[i] == 'F' && targets.contains(pos[i]))
                add(map, pos[i]);
        int m = 0;
        int prev = C;
        for (int i = C - 1; i >= 0; i--) {
            int p = pos[i];
            if (s[i] == 'F') {
                if (targets.contains(p + 1))
                    add(map, p + 1);
                remove(map, p);
            } else if (s[i] == 'L') {
                if (targets.contains(p + 1))
                    add(map, p + 1);
                if (prev != C) 
                    remove(map, pos[prev] + 1);
                prev = i;
                m = Math.max(m, map.size());
            }
        }
        return m;
    }

    int fireToRight() {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < C; i++)
            if (s[i] == 'F' && targets.contains(pos[i]))
                add(map, pos[i]);
        int m = 0;
        for (int i = C - 1; i >= 0; i--) {
            int p = pos[i];
            if (s[i] == 'F') {
                remove(map, p);
                m = Math.max(m, map.size());
                if (targets.contains(p + 1))
                    add(map, p + 1);
            }
        }
        return m;
    }

    int fireToLeft() {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < C; i++)
            if (s[i] == 'F' && targets.contains(pos[i]))
                add(map, pos[i]);
        int m = 0;
        for (int i = C - 1; i >= 0; i--) {
            int p = pos[i];
            if (s[i] == 'F') {
                remove(map, p);
                m = Math.max(m, map.size());
                if (targets.contains(p - 1))
                    add(map, p - 1);
            }
        }
        return m;
    }

    int leftToRight() {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < C; i++)
            if (s[i] == 'F' && targets.contains(pos[i]))
                add(map, pos[i]);
        int m = 0;
        for (int i = C - 1; i >= 0; i--) {
            int p = pos[i];
            if (s[i] == 'L')
                m = Math.max(m, map.size());
            else if (s[i] == 'F') {
                remove(map, p);
                if (targets.contains(p + 2))
                    add(map, p + 2);
            }
        }
        return m;
    }

    int rightToLeft() {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < C; i++)
            if (s[i] == 'F' && targets.contains(pos[i]))
                add(map, pos[i]);
        int m = 0;
        for (int i = C - 1; i >= 0; i--) {
            int p = pos[i];
            if (s[i] == 'R')
                m = Math.max(m, map.size());
            else if (s[i] == 'F') {
                remove(map, p);
                if (targets.contains(p - 2))
                    add(map, p - 2);
            }
        }
        return m;
    }

    // multiset helper
    void remove(Map<Integer, Integer> m, int v) {
        if (!get(m, v))
            return;
        m.merge(v, -1, Integer::sum);
        if (m.get(v) <= 0)
            m.remove(v);
    }

    void add(Map<Integer, Integer> m, int v) {
        m.merge(v, 1, Integer::sum);
    }

    boolean get(Map<Integer, Integer> m, int v) {
        return m.containsKey(v);
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
