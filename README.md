# Aho-Corasick algorithm in Java

This project is a Java implementation of the Aho-Corasick algorithm for searching many patterns in one text. I also compared it with running KMP separately for every pattern.

## Files

src/AhoCorasick.java has the trie, failure links, output links and the search.

src/Kmp.java is the KMP I used for the comparison.

src/Benchmark.java runs the experiments.

test/AhoCorasickTest.java has the JUnit tests.


## Complexity

Let L be the total length of all patterns, n the length of the text, z the number of matches, k the number of patterns and 26 the alphabet size.

Building the automaton takes O(L * 26) in my implementation because every node has a row of 26 transitions. For a fixed alphabet this is O(L). The search takes O(n + z). The memory is O(L * 26), since the table has at most L + 1 rows. For 1000 patterns of length up to 8 it is about 0.8 MB.

KMP for one pattern takes O(n + m), so for k patterns it is about O(k * n) for the search and almost no extra memory.

Aho-Corasick fits many patterns because the trie joins all of them into one structure and the failure links let the automaton continue after a mismatch without returning in the text. So the text is read only once, no matter how many patterns we have. KMP has to read the whole text again for every pattern.

## Experiments

The patterns are random lowercase words of length 4 to 8 without duplicates, and the text is a random lowercase string. The random seed is fixed (42) so the results can be repeated. Each case was run 5 times and I took the average. The time was measured with System.nanoTime(). Before the measurements I did a warm up so the JIT compiler does not make the first cases slower. The code also checks that both algorithms find the same number of matches.

All times are in milliseconds.

```
patterns  text    AC build  AC search  AC total  KMP      matches
10        1000    0.054     0.013      0.067     0.074    0
10        10000   0.027     0.087      0.115     0.738    0
10        100000  0.050     0.901      0.950     18.121   0
100       1000    0.255     0.011      0.266     0.789    0
100       10000   0.219     0.323      0.542     11.728   1
100       100000  0.205     1.234      1.439     65.960   7
1000      1000    1.812     0.017      1.829     6.519    0
1000      10000   1.264     0.156      1.421     56.288   7
1000      100000  1.410     1.681      3.091     510.967  39
```

## Discussion of the results

The search time of Aho-Corasick depends mostly on the length of the text and not on the number of patterns. For a text of 100000 characters the search took about 0.9 ms with 10 patterns and about 1.7 ms with 1000 patterns. So 100 times more patterns made it less than 2 times slower. The small growth is probably because the table gets bigger and works worse with the CPU cache.

The build time grows with the total length of the patterns. It went from about 0.05 ms for 10 patterns to about 1.4 ms for 1000 patterns.

KMP gets slower both with longer text and with more patterns. For 1000 patterns and 100000 characters KMP needed about 511 ms, and Aho-Corasick needed about 3 ms in total including the build, which is around 165 times faster. For 10 patterns and the same text the difference is about 19 times. In the smallest case (10 patterns, text 1000) both are almost the same.

Very small times, under 0.1 ms, are not very reliable because of the JIT and timer precision. For example the build time for 10 patterns is bigger on the short text than on the longer one, which is just noise.

The number of matches is small because the text is random and the patterns are fairly long. Most of the time in both algorithms goes to scanning the text, so this does not change the conclusion.

## Advantages and limitations

Aho-Corasick reads the text only once for any number of patterns, so it works well for big pattern sets and for streaming text. Its time is predictable, O(n + z). The downsides are the extra memory for the transition table, and the automaton has to be built again if the set of patterns changes. For one or two patterns it is too much.

KMP is simple and needs almost no extra memory, and it is fine for one pattern or a few patterns that change often. Its downside is that the time grows linearly with the number of patterns because the text is scanned once per pattern.
