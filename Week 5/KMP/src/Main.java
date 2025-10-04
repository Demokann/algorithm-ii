public class Main {
    // Compute the failure function (pi)
    private static int[] computeFailureFunction(String pattern) {
        int m = pattern.length();
        int[] pi = new int[m];

        // Initialize pi[0] = 0 (no proper prefix exists for single character)
        pi[0] = 0;

        // Compute pi[1] to pi[m-1]
        for (int s = 1; s < m; s++) {
            // Start with the value of pi for the previous position
            int q = pi[s - 1];

            // Keep trying shorter prefixes until we find a match or reach 0
            while (q > 0 && pattern.charAt(s) != pattern.charAt(q)) {
                q = pi[q - 1];
            }

            // If current characters match, increment the prefix length
            if (pattern.charAt(s) == pattern.charAt(q)) {
                q++;
            }

            // Store the computed failure function value
            pi[s] = q;
        }

        return pi;
    }

    // KMP search algorithm
    public static void kmpSearch(String text, String pattern) {
        int n = text.length();
        int m = pattern.length();

        // Edge case
        if (m == 0) {
            System.out.println("Pattern is empty");
            return;
        }

        // Step 1: Compute the failure function
        int[] pi = computeFailureFunction(pattern);

        // Step 2: Perform the search
        int q = 0;  // Number of characters matched

        for (int i = 0; i < n; i++) {
            // If there's a mismatch, use the failure function to shift the pattern
            while (q > 0 && pattern.charAt(q) != text.charAt(i)) {
                q = pi[q - 1];
            }

            // If characters match, increment matched count
            if (pattern.charAt(q) == text.charAt(i)) {
                q++;
            }

            // If we've matched the entire pattern
            if (q == m) {
                int position = i - m + 1;
                System.out.println("Pattern found at index: " + position);

                // Continue searching for more occurrences
                q = pi[q - 1];
            }
        }
    }

    public static void main(String[] args) {
        String text = "ABABDABACDABABCABAB";
        String pattern = "ABABCABAB";

        System.out.println("Text: " + text);
        System.out.println("Pattern: " + pattern);
        kmpSearch(text, pattern);

        // To demonstrate the computation of the failure function
        int[] pi = computeFailureFunction(pattern);
        System.out.println("Failure function values:");
        for (int i = 0; i < pattern.length(); i++) {
            System.out.println("π(" + i + ") = " + pi[i] + " for prefix: " + pattern.substring(0, i + 1));
        }
    }
}