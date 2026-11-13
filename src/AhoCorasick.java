import java.util.*;

public class AhoCorasick {
    private static final int alpha = 26;
    private int[][] next;
    private int[] fail;
    private int[] link;
    private int[] endid;
    private int nodes = 1;
    private final String[] patterns;

    public AhoCorasick(String[] patterns) {
        this.patterns = patterns;
        int total = 1;
        for (String p : patterns) total += p.length();
        next = new int[total][alpha];
        for (int[] row : next) Arrays.fill(row, -1);
        fail = new int[total];
        link = new int[total];
        endid = new int[total];
        Arrays.fill(endid, -1);
        Arrays.fill(link, -1);
        for (int i = 0; i < patterns.length; i++) insert(patterns[i], i);
        build();
    }

    private void insert(String p, int id) {
        int v = 0;
        for (int i = 0; i < p.length(); i++) {
            int c = p.charAt(i) - 'a';
            if (next[v][c] == -1) next[v][c] = nodes++;
            v = next[v][c];
        }
        if (endid[v] == -1) endid[v] = id;
    }

    private void build() {
        Queue<Integer> q = new ArrayDeque<>();
        for (int c = 0; c < alpha; c++) {
            int u = next[0][c];
            if (u == -1) next[0][c] = 0;
            else {
                fail[u] = 0;
                q.add(u);
            }
        }
        while (!q.isEmpty()) {
            int v = q.poll();
            for (int c = 0; c < alpha; c++) {
                int u = next[v][c];
                if (u == -1) {
                    next[v][c] = next[fail[v]][c];
                } else {
                    fail[u] = next[fail[v]][c];
                    int f = fail[u];
                    link[u] = endid[f] != -1 ? f : link[f];
                    q.add(u);
                }
            }
        }
    }

    public List<int[]> search(String text) {
        List<int[]> found = new ArrayList<>();
        int v = 0;
        for (int i = 0; i < text.length(); i++) {
            v = next[v][text.charAt(i) - 'a'];
            int w = endid[v] != -1 ? v : link[v];
            while (w != -1) {
                found.add(new int[]{i, endid[w]});
                w = link[w];
            }
        }
        return found;
    }

    public int nodeCount() {
        return nodes;
    }
}