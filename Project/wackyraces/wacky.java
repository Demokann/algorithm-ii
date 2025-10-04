import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
public class wacky {

    public static final int S = 128;

    public static void main(String[] args) {
         String textPath1 = "text1.txt";
        String T = "";
        try {
            T = readBookFromFile(textPath1);
        }catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return;
        } 
        String P = "AAAAAAAAAB";


        long startTime = System.nanoTime();
        int rabinKarpCount = rabinKarpSearch(T, P);
        long endTime = System.nanoTime();
        double rabinKarpResult = (endTime - startTime) / 1_000_000.0;
        System.out.println("Count : " + rabinKarpCount);
        System.out.printf("Execution time for Rabin-Karp: %.3f milliseconds%n", rabinKarpResult);

        startTime = System.nanoTime();
        int kmpCount = kmpSearch(T, P);
        endTime = System.nanoTime();
        double kmpResult = (endTime - startTime) / 1_000_000.0;
        System.out.println("Count: " + kmpCount);
        System.out.printf("Execution time for KMP: %.3f milliseconds%n", kmpResult);
    }
    private static String readBookFromFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }


    public static int rabinKarpSearch(String T, String P) {
        int n = T.length();
        int m = P.length();
        int L = 10007;
        int p = 0;  //Hash value for pattern
        int t = 0;  //Hash value for text
        int h = 1;
        int count = 0;

        // The value of h would be "pow(S, M-1)%L"
        for (int i = 0; i < m - 1; i++) {
            h = (h * S) % L;
        }

        for (int i = 0; i < m; i++) {
            p = (S * p + P.charAt(i)) % L;
            t = (S * t + T.charAt(i)) % L;
        }

        for (int i = 0; i <= n - m; i++) {
            if (p == t) {
                int j = 0;
                while (j < m && P.charAt(j) == T.charAt(i + j)) {
                    j++;
                }

                if (j == m) {
                    count+=1;
                }
            }

            if (i < n - m) {
                //Substract previous character and recalculate next hash value.
                t = (S * (t - T.charAt(i) * h) + T.charAt(i + m)) % L;

            }

            if (t < 0) {
                t = (t + L); //converting negative t to positive
            }
        }
        return count;
    }

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
    public static int kmpSearch(String text, String pattern) {
        int n = text.length();
        int m = pattern.length();
        int count = 0;

        // Edge case
        if (m == 0) {
            System.out.println("Pattern is empty");
            return 1;
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
                
                count += 1;

                // Continue searching for more occurrences
                q = pi[q - 1];
            }
        }
        return count;
    }
}

