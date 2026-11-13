import java.util.*;

public class AhoCorasick {
    private static final int alpha = 26;
    private int[][] next;
    private int[] endid;
    private int nodes = 1;
    private final String[] patterns;

    public AhoCorasick(String[] patterns) {
        this.patterns = patterns;
        int total = 1;
        for (String p : patterns) total += p.length();
        next = new int[total][alpha];
        for (int[] row : next) Arrays.fill(row, -1);
        endid = new int[total];
        Arrays.fill(endid, -1);
        for (int i = 0; i < patterns.length; i++) insert(patterns[i], i);
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

    public int nodeCount() {
        return nodes;
    }
}