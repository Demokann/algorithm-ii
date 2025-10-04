public class Main {
    public static void main(String[] args) {
        String T = "car got hit by another car";
        String P = "car";

        long startTime = System.nanoTime();

        search(T, P);

        long endTime = System.nanoTime();
        double durationInMilliseconds = (endTime - startTime) / 1_000_000.0;
        System.out.printf("Execution time: %.3f milliseconds%n",durationInMilliseconds);
    }

    public static void search(String T, String P) {
        int n = T.length();
        int m = P.length();

        // Create the engine (transition table)
        int[][] engine = new int[m + 1][128];

        // Build the transition table
        for (int a = 0; a < 128; a++) {
            for (int s = 0; s <= m; s++) {
                int k = Math.min(m, s + 1);
                while (k > 0 && !cond(k, s, a, P)) {
                    k--;
                }
                engine[s][a] = k;
            }
        }

        // Search for the pattern
        int s = 0;
        for (int i = 0; i < n; i++) {
            s = engine[s][T.charAt(i)];
            if (s == m) {
                System.out.println("Valid shift at " + (i - m + 1));
            }
        }
    }

    //State is the length of the longest prefix of P found on a suffix we have consumed
    // 0 <= state <= m

    // Condition function to check if character 'a' can extend the current state
    private static boolean cond(int k, int s, int a, String P) {
        // Check if the character we're considering (a) matches the kth character in the pattern
        if (k > 0 && P.charAt(k - 1) != a) {
            return false;
        }

        // If s < k-1, we also need to verify the prefix condition
        // This ensures the k characters we're considering form a valid prefix of the pattern
        if (s < k - 1) {
            for (int i = 0; i < s; i++) {
                if (P.charAt(i) != P.charAt(i + (k - s - 1))) {
                    return false;
                }
            }
        }

        return true;
    }
}
