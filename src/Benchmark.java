import java.util.*;

public class Benchmark {
    static String rand(Random r, int n) {
        char[] c = new char[n];
        for (int i = 0; i < n; i++) c[i] = (char) ('a' + r.nextInt(26));
        return new String(c);
    }

    public static void main(String[] args) {
        int[] pcounts = {10, 100, 1000};
        int[] tlens = {1000, 10000, 100000};
        int runs = 5;
        Random rnd = new Random(42);

        String[] wp = new String[50];
        for (int i = 0; i < wp.length; i++) wp[i] = rand(rnd, 5);
        String wt = rand(rnd, 50000);
        for (int i = 0; i < 20; i++) {
            new AhoCorasick(wp).search(wt);
            Kmp.countAll(wt, wp);
        }

        System.out.println("| patterns | text | ac build ms | ac search ms | ac total ms | kmp ms | matches |");
        System.out.println("|---|---|---|---|---|---|---|");
        for (int pc : pcounts) {
            for (int tl : tlens) {
                Set<String> set = new LinkedHashSet<>();
                while (set.size() < pc) set.add(rand(rnd, 4 + rnd.nextInt(5)));
                String[] pats = set.toArray(new String[0]);
                String text = rand(rnd, tl);

                long build = 0, search = 0, kmp = 0;
                int acMatches = 0, kmpMatches = 0;
                for (int r = 0; r < runs; r++) {
                    long t0 = System.nanoTime();
                    AhoCorasick ac = new AhoCorasick(pats);
                    long t1 = System.nanoTime();
                    acMatches = ac.search(text).size();
                    long t2 = System.nanoTime();
                    kmpMatches = Kmp.countAll(text, pats);
                    long t3 = System.nanoTime();
                    build += t1 - t0;
                    search += t2 - t1;
                    kmp += t3 - t2;
                }
                if (acMatches != kmpMatches) throw new IllegalStateException("match counts differ");
                double b = build / runs / 1e6;
                double s = search / runs / 1e6;
                double k = kmp / runs / 1e6;
                System.out.printf("| %d | %d | %.3f | %.3f | %.3f | %.3f | %d |%n",
                        pc, tl, b, s, b + s, k, acMatches);
            }
        }
    }
}