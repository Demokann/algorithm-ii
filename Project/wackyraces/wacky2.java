import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
public class wacky2 {
    public static void main(String[] args) {

        String warmUpText = "This a warm up sample";
        String warmUpPattern = "sample";
        sundaySearch(warmUpText, warmUpPattern);
        gustfieldZSearch(warmUpText, warmUpPattern);
        
        
        String textPath1 = "text3.txt";
        String T = "";
        try {
            T = readBookFromFile(textPath1).replace("\n", "").replace("\r", "");
        }catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return;
        } 
        String P = "xyzxyzxyzxyzxyzxyzxyzxyzxyzxyzxyzxyzxyzxyzxyzxyzxyzxyzxyzxyzxyzxyzxyzxyzxyzxyzxyzxyzxyzxyzxyzxyzxyzxyzxyzxyzxyzxyzxyzxyzxyzxyzxyzxyzxyzxyzxyz";
        
        long startTime = System.nanoTime();
        long sundayCount = sundaySearch(T, P);
        long endTime = System.nanoTime();
        double sundayResult = (endTime - startTime) / 1_000_000.0;
        System.out.println("Count: " + sundayCount);
        System.out.printf("Execution time for Sunday: %.3f milliseconds%n",  + sundayResult);

        startTime = System.nanoTime();
        long gustfieldZCount = sundaySearch(T, P);
        endTime = System.nanoTime();
        double gustfieldZResult = (endTime - startTime) / 1_000_000.0;
        System.out.println("Count: " + gustfieldZCount);
        System.out.printf("Execution time for Gustfield-Z: %.3f milliseconds%n",  + gustfieldZResult);
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

    public static int sundaySearch(String T, String P) {
        int n = T.length();
        int m = P.length();
        int count = 0;        

        int[] lastPosition = new int[128];
        for (int a = 0; a < 128; a++) {
            lastPosition[a] = -1;
        }
        for (int j = 0; j < m; j++) {
            lastPosition[P.charAt(j)] = j;
        }

        for (int i = 0; i <= n - m; ) {
            int j = 0;
            while (j < m && P.charAt(j) == T.charAt(i + j)) {
                j++;
            }
            if (j == m) {
                count += 1;
            }


            if (i + m < n) {
                char nextChar = T.charAt(i + m);
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
        return count;
    }

    //Compute the Z-array gor gustfield algorithm
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

    //Gustfield-z search algorithm
    public static int gustfieldZSearch(String text, String pattern) {
        // Concatenate pattern, a special character, and text
        String concat = pattern + "$" + text;
        int[] Z = computeZArray(concat);
        int count = 0;

        int patternLength = pattern.length();
        int concatLength = concat.length();


        for (int i = 0; i < concatLength; i++) {
            if (Z[i] == patternLength) {
                // Convert position in concatenated string to position in original text
                count += 1;
            }
        }

        return count;
    }
}
