import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.nio.file.*;

public class Main {
    public static void main(String[] args) {
        try {
            cleanFile("/Users/demokan/Documents/Projects/algorithm-ii/Project/BookSearch/src/the-hobbit-1.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Define your book file path and character name
        String bookFilePath = "/Users/demokan/Documents/Projects/algorithm-ii/Project/BookSearch/src/the-hobbit-1.txt"; // Replace
                                                                                                                        // with
        // path to your
        // book file
        String wordPattern = "Baggins"; // Replace with your character name
        String paragraphPattern = """
                In a hole in the ground there lived a hobbit. Not a nasty, dirty, wet hole,
                filled with the ends of worms and an oozy smell, nor yet a dry, bare, sandy
                hole with nothing in it to sit down on or to eat: it was a hobbit-hole, and
                that means comfort.
                """;
        try {
            // Read the book content
            String bookContent = readBookFromFile(bookFilePath);

            // Warm up the JVM by running each algorithm once with a small sample
            System.out.println("WARMING UP JVM...");
            String warmupText = "This is a sample text for warming up the JVM";
            String warmupPattern = "sample";

            // Call each algorithm once to warm up the JVM
            bruteForceSearch(warmupText, warmupPattern);
            sundaySearch(warmupText, warmupPattern);
            rabinKarpSearch(warmupText, warmupPattern);
            fsmSearch(warmupText, warmupPattern);
            kmpSearch(warmupText, warmupPattern);
            gustfieldSearch(warmupText, warmupPattern);
            System.out.println("WARM-UP COMPLETE\n");

            // Run pattern matching for paragraph
            System.out.println("\n\nSEARCHING FOR PARAGRAPH:");
            runAllSearches(bookContent, paragraphPattern);
            // Run pattern matching for word
            System.out.println("SEARCHING FOR WORD: " + wordPattern);
            runAllSearches(bookContent, wordPattern);

        } catch (IOException e) {
            System.err.println("Error reading book file: " + e.getMessage());
        }
    }

    public static void runAllSearches(String text, String pattern) {
        // Search using brute force algorithm
        System.out.println("BRUTE FORCE SEARCH:");
        long startTime = System.nanoTime();
        List<Integer> bruteForceResults = bruteForceSearch(text, pattern);
        long endTime = System.nanoTime();
        double bruteForceDuration = (endTime - startTime) / 1_000_000.0;

        // Print results for brute force
        printResults(pattern, bruteForceResults, bruteForceDuration, text.length());

        // Search using Sunday algorithm
        System.out.println("\nSUNDAY ALGORITHM SEARCH:");
        startTime = System.nanoTime();
        List<Integer> sundayResults = sundaySearch(text, pattern);
        endTime = System.nanoTime();
        double sundayDuration = (endTime - startTime) / 1_000_000.0;

        // Print results for Sunday algorithm
        printResults(pattern, sundayResults, sundayDuration, text.length());

        // Search using Rabin-Karp algorithm
        System.out.println("\nRABIN KARP SEARCH");
        startTime = System.nanoTime();
        List<Integer> rabinKarpResults = rabinKarpSearch(text, pattern);
        endTime = System.nanoTime();
        double rabinKarpDuration = (endTime - startTime) / 1_000_000.0;
        // Print results for Rabin-Karp algorithm
        printResults(pattern, rabinKarpResults, rabinKarpDuration, text.length());

        System.out.println("\nFSM SEARCH: ");
        startTime = System.nanoTime();
        List<Integer> fsmResults = fsmSearch(text, pattern);
        endTime = System.nanoTime();
        double fsmDuration = (endTime - startTime) / 1_000_000.0;
        // Print result for FSM algorithm
        printResults(pattern, fsmResults, fsmDuration, text.length());

        // Search using KMP algorithm
        System.out.println("\nKMP SEARCH");
        startTime = System.nanoTime();
        List<Integer> kmpResults = kmpSearch(text, pattern);
        endTime = System.nanoTime();
        double kmpDuration = (endTime - startTime) / 1_000_000.0;
        // Print results for KMP algorithm
        printResults(pattern, kmpResults, kmpDuration, text.length());
        // Search using Gustfield-z algorithm
        System.out.println("\nGUSTFIELD-Z SEARCH");
        startTime = System.nanoTime();
        List<Integer> gustfieldResults = gustfieldSearch(text, pattern);
        endTime = System.nanoTime();
        double gustfieldDuration = (endTime - startTime) / 1_000_000.0;
        // Print results for Gustfield-z algorithm
        printResults(pattern, gustfieldResults, gustfieldDuration, text.length());
    }

    // Method for cleaning NON-ASCII character from text
    public static void cleanFile(String filename) throws IOException {
        // Create a temporary file
        Path tempFile = Files.createTempFile("cleaned", ".txt");

        // Read from original file and write to temp file
        try (BufferedReader reader = new BufferedReader(new FileReader(filename));
                PrintWriter writer = new PrintWriter(new FileWriter(tempFile.toFile()))) {

            String line;
            while ((line = reader.readLine()) != null) {
                // Remove non-ASCII characters
                String cleanedLine = line.replaceAll("[^\\p{ASCII}]", "");
                writer.println(cleanedLine);
            }
        }

        // Replace original file with cleaned temp file
        Files.move(tempFile, Paths.get(filename), StandardCopyOption.REPLACE_EXISTING);
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

    // Method to print the search results and statistics
    private static void printResults(String pattern, List<Integer> positions, double duration, int textLength) {
        // Print summary statistics
        System.out.println("----------------------------");
        System.out.println("Search Statistics:");
        System.out.println("Character: " + pattern);
        System.out.println("Total occurrences found: " + positions.size());
        System.out.printf("Search execution time: %.3f milliseconds%n", duration);
        System.out.println("Book length: " + textLength + " characters");
    }

    // brute force search algorithm
    public static List<Integer> bruteForceSearch(String text, String pattern) {
        int n = text.length();
        int m = pattern.length();
        List<Integer> positions = new ArrayList<>();

        // Standard brute force - try each position in the text
        for (int i = 0; i <= n - m; i++) {
            int j = 0;
            // Compare pattern with text at current position
            while (j < m && pattern.charAt(j) == text.charAt(i + j)) {
                j++;
            }
            // If we matched the entire pattern
            if (j == m) {
                positions.add(i);
            }
        }

        return positions;
    }

    // Sunday algorithm
    public static List<Integer> sundaySearch(String text, String pattern) {
        int n = text.length();
        int m = pattern.length();
        List<Integer> positions = new ArrayList<>();

        // Preprocess - create array to store rightmost position of each character in
        // pattern
        int[] lastPosition = new int[128]; // ASCII character set

        // Initialize with default value (if character not in pattern)
        for (int i = 0; i < 128; i++) {
            lastPosition[i] = -1; // -1 means not found in pattern
        }

        // Fill in the actual last positions
        for (int i = 0; i < m; i++) {
            lastPosition[pattern.charAt(i)] = i;
        }

        // Main search algorithm
        for (int i = 0; i <= n - m;) {
            int j = 0;
            // Check for match at current position
            while (j < m && pattern.charAt(j) == text.charAt(i + j)) {
                j++;
            }

            // If we found a match
            if (j == m) {
                positions.add(i);
            }

            // Look at the character immediately after the current alignment
            if (i + m < n) {
                char nextChar = text.charAt(i + m);
                if (lastPosition[nextChar] == -1) {
                    // Character not in pattern, skip the entire pattern length plus 1
                    i += m + 1;
                } else {
                    // Character is in pattern, align the rightmost occurrence with it
                    i += m - lastPosition[nextChar];
                }
            } else {
                // We've reached the end of the text
                break;
            }
        }

        return positions;
    }

    // Corrected Rabin-Karp algorithm
    public static List<Integer> rabinKarpSearch(String text, String pattern) {
        List<Integer> positions = new ArrayList<>();
        int S = 128; // Base value (alphabet size)
        int L = 101991; // Prime number for modulo
        int n = text.length();
        int m = pattern.length();

        // Special case: empty pattern or text
        if (m == 0 || n < m) {
            return positions;
        }

        // Calculate S^(m-1) % L for rolling hash
        long h = 1;
        for (int i = 0; i < m - 1; i++) {
            h = (h * S) % L;
        }

        // Calculate initial hash values for pattern and first text window
        long patternHash = 0;
        long textHash = 0;
        for (int i = 0; i < m; i++) {
            patternHash = (patternHash * S + pattern.charAt(i)) % L;
            textHash = (textHash * S + text.charAt(i)) % L;
        }

        // Slide the pattern over text one by one
        for (int i = 0; i <= n - m; i++) {
            // Check for hash match
            if (patternHash == textHash) {
                // Verify character by character (for hash collision cases)
                int j = 0;
                while (j < m && pattern.charAt(j) == text.charAt(i + j)) {
                    j++;
                }
                if (j == m) {
                    positions.add(i);
                }
            }

            // Calculate hash for next window: Remove leading digit,
            // add trailing digit
            if (i < n - m) {
                // Remove contribution of first character
                textHash = (S * (textHash - text.charAt(i) * h) + text.charAt(i + m)) % L;

                // Handle negative values
                if (textHash < 0) {
                    textHash += L;
                }
            }
        }

        return positions;
    }

    // FSM search algorithm
    public static List<Integer> fsmSearch(String text, String pattern) {
        List<Integer> positions = new ArrayList<>();
        int n = text.length();
        int m = pattern.length();

        // Create the transition table
        int[][] engine = new int[m + 1][128];

        // Build the transition table
        for (int a = 0; a < 128; a++) {
            for (int s = 0; s <= m; s++) {
                int k = Math.min(m, s + 1);
                while (k > 0 && !cond(k, s, a, pattern)) {
                    k--;
                }
                engine[s][a] = k;
            }
        }

        // Search for the pattern
        int state = 0;
        for (int i = 0; i < n; i++) {
            state = engine[state][text.charAt(i)];
            if (state == m) {
                positions.add(i - m + 1);
            }
        }
        return positions;
    }

    // Condition function for FSM
    private static boolean cond(int k, int s, int a, String pattern) {
        // If k is positive, the character at index k-1 must match 'a'
        if (k > 0 && pattern.charAt(k - 1) != a) {
            return false;
        }

        // If s < k-1, we also need to verify the prefix condition
        // This ensures the k characters we're considering form a valid prefix of the
        // pattern
        if (s < k - 1) {
            for (int i = 0; i < s; i++) {
                if (pattern.charAt(i) != pattern.charAt(i + (k - s - 1))) {
                    return false;
                }
            }
        }

        return true;
    }

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
    public static List<Integer> kmpSearch(String text, String pattern) {
        List<Integer> positions = new ArrayList<>();
        int n = text.length();
        int m = pattern.length();

        // Edge case
        if (m == 0) {
            System.out.println("Pattern is empty");
            return positions;
        }

        // Step 1: Compute the failure function
        int[] pi = computeFailureFunction(pattern);

        // Step 2: Perform the search
        int q = 0; // Number of characters matched

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
                positions.add(i - m + 1);

                // Continue searching for more occurrences
                q = pi[q - 1];
            }
        }
        return positions;
    }

    // Compute the Z-array gor gustfield algorithm
    public static int[] computeZArray(String str) {
        int n = str.length();
        int[] Z = new int[n];

        // Z box boundaries
        int left = 0;
        int right = 0;

        // Z[0] is always 0 (or n, depending on implementation)
        // Start from index 1
        for (int i = 1; i < n; i++) {
            // Case 1: i is outside the current Z-box
            if (i > right) {
                left = right = i;

                // Explicitly compare characters starting from position i with prefix at 0
                while (right < n && str.charAt(right - left) == str.charAt(right)) {
                    right++;
                }

                // Set Z[i] as the length of the match
                Z[i] = right - left;
                right--; // Adjust the right boundary
            }
            // Case 2: i is inside the current Z-box
            else {
                // Get the corresponding value from the prefix
                int k = i - left;

                // Case 2a: The value fits within the Z-box
                if (Z[k] < right - i + 1) {
                    Z[i] = Z[k];
                }
                // Case 2b: The value might exceed the Z-box boundary
                else {
                    left = i;

                    // Explicitly compare characters beyond the Z-box boundary
                    while (right < n && str.charAt(right - left) == str.charAt(right)) {
                        right++;
                    }

                    Z[i] = right - left;
                    right--; // Adjust the right boundary
                }
            }
        }

        return Z;
    }

    // Gustfield-z search algorithm
    public static List<Integer> gustfieldSearch(String text, String pattern) {
        List<Integer> positions = new ArrayList<>();
        // Concatenate pattern, a special character, and text
        String concat = pattern + "$" + text;
        int[] Z = computeZArray(concat);

        int patternLength = pattern.length();
        int concatLength = concat.length();

        for (int i = 0; i < concatLength; i++) {
            if (Z[i] == patternLength) {
                // Convert position in concatenated string to position in original text
                positions.add(i - patternLength - 1);
            }
        }

        return positions;
    }

}