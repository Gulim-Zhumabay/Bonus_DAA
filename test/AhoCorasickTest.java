import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class AhoCorasickTest {
    static int naive(String text, String[] ps) {
        int c = 0;
        for (String p : ps)
            for (int i = 0; i + p.length() <= text.length(); i++)
                if (text.startsWith(p, i)) c++;
        return c;
    }

    static String[] dedupe(String[] p) {
        return new LinkedHashSet<>(Arrays.asList(p)).toArray(new String[0]);
    }

    static String rand(Random r, int n, int letters) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) sb.append((char) ('a' + r.nextInt(letters)));
        return sb.toString();
    }

    @Test
    void ushers_example() {
        String[] ps = {"he", "she", "his", "hers"};
        Set<String> got = new HashSet<>();
        for (int[] m : new AhoCorasick(ps).search("ushers")) got.add(ps[m[1]] + "@" + m[0]);
        assertEquals(Set.of("she@3", "he@3", "hers@5"), got);
    }

    @Test
    void no_match() {
        assertTrue(new AhoCorasick(new String[]{"abc"}).search("xyz").isEmpty());
    }

    @Test
    void overlapping_matches() {
        assertEquals(3, new AhoCorasick(new String[]{"aa"}).search("aaaa").size());
    }

    @Test
    void nested_patterns() {
        String[] nest = {"a", "ab", "bab", "bc", "bca", "c", "caa"};
        assertEquals(naive("abccab", nest), new AhoCorasick(nest).search("abccab").size());
    }

    @Test
    void random_vs_naive() {
        Random rnd = new Random(7);
        for (int t = 0; t < 200; t++) {
            String[] p = new String[1 + rnd.nextInt(8)];
            for (int i = 0; i < p.length; i++) p[i] = rand(rnd, 1 + rnd.nextInt(4), 3);
            p = dedupe(p);
            String text = rand(rnd, 50 + rnd.nextInt(100), 3);
            assertEquals(naive(text, p), new AhoCorasick(p).search(text).size());
        }
    }
}