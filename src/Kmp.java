import java.util.*;

public class Kmp {
    private static int[] prefix(String p) {
        int[] pi = new int[p.length()];
        int k = 0;
        for (int i = 1; i < p.length(); i++) {
            while (k > 0 && p.charAt(i) != p.charAt(k)) k = pi[k - 1];
            if (p.charAt(i) == p.charAt(k)) k++;
            pi[i] = k;
        }
        return pi;
    }

    public static List<Integer> search(String text, String p) {
        List<Integer> res = new ArrayList<>();
        int[] pi = prefix(p);
        int k = 0;
        for (int i = 0; i < text.length(); i++) {
            while (k > 0 && text.charAt(i) != p.charAt(k)) k = pi[k - 1];
            if (text.charAt(i) == p.charAt(k)) k++;
            if (k == p.length()) {
                res.add(i);
                k = pi[k - 1];
            }
        }
        return res;
    }

    public static int countAll(String text, String[] patterns) {
        int total = 0;
        for (String p : patterns) total += search(text, p).size();
        return total;
    }
}