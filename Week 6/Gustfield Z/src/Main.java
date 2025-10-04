public class Main {

    /**
     * Computes the Z-array for a given string
     * Z[i] represents the length of the longest substring starting at i
     * that is also a prefix of the string
     *
     * @param str The input string
     * @return The Z-array
     */
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

    /**
     * Searches for all occurrences of a pattern within a text using the Z algorithm
     *
     * @param text The text to search in
     * @param pattern The pattern to search for
     * @return Array of indices where the pattern is found in the text
     */
    public static int[] search(String text, String pattern) {
        // Concatenate pattern, a special character, and text
        String concat = pattern + "$" + text;
        int[] Z = computeZArray(concat);

        int patternLength = pattern.length();
        int concatLength = concat.length();

        // Count the number of matches to allocate the result array
        int matchCount = 0;
        for (int i = 0; i < concatLength; i++) {
            if (Z[i] == patternLength) {
                matchCount++;
            }
        }

        // Create and fill the result array with match positions
        int[] result = new int[matchCount];
        int index = 0;

        for (int i = 0; i < concatLength; i++) {
            if (Z[i] == patternLength) {
                // Convert position in concatenated string to position in original text
                result[index++] = i - patternLength - 1;
            }
        }

        return result;
    }

    /**
     * Main method with example usage
     */
    public static void main(String[] args) {
        String text = "abcabcabdabc";
        String pattern = "abc";

        System.out.println("Text: " + text);
        System.out.println("Pattern: " + pattern);

        int[] matches = search(text, pattern);

        System.out.println("Pattern found at positions: ");
        for (int pos : matches) {
            System.out.print(pos + " ");
        }

        // Demonstrating the Z-array computation
        System.out.println("\n\nDemonstrating Z-array computation:");
        String example = "aabcaabxaaz";
        System.out.println("String: " + example);

        int[] zArray = computeZArray(example);
        System.out.println("Z-array: ");
        for (int i = 0; i < zArray.length; i++) {
            System.out.print(zArray[i] + " ");
        }
    }
}