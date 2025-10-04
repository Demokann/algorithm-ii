import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class BookCharacterSearch {
    public static void main(String[] args) {
        // Define your book file path and character name
        String bookFilePath = "yourBookFile.txt"; // Replace with path to your book file
        String characterName = "Elizabeth"; // Replace with your character name
        
        try {
            // Read the book content
            String bookContent = readBookFromFile(bookFilePath);
            
            // Search for the character name in the book
            searchCharacterInBook(bookContent, characterName);
            
        } catch (IOException e) {
            System.err.println("Error reading book file: " + e.getMessage());
        }
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
    
    private static void searchCharacterInBook(String T, String P) {
        int n = T.length();
        int m = P.length();
        int occurrenceCount = 0;
        
        // Start timing
        long startTime = System.nanoTime();
        
        // Preprocess - create boolean array to check if character is in pattern P
        boolean[] isInP = new boolean[128]; // ASCII character set
        for (int j = 0; j < m; j++) {
            isInP[P.charAt(j)] = true;
        }

        // Main search algorithm
        for (int i = 0; i <= n - m; ) {
            int j = 0;
            while (j < m && P.charAt(j) == T.charAt(i + j)) {
                j++;
            }
            
            if (j == m) {
                System.out.println("Character '" + P + "' found at position: " + i);
                occurrenceCount++;
            }
            
            // Determine next position to check
            if (i < n - m && isInP[T.charAt(i + m)]) {
                i++;
            } else {
                i += m + 1; // Skip ahead more aggressively if possible
            }
        }
        
        // End timing
        long endTime = System.nanoTime();
        double durationInMilliseconds = (endTime - startTime) / 1_000_000.0;
        
        // Print results
        System.out.println("----------------------------");
        System.out.println("Search Statistics:");
        System.out.println("Character: " + P);
        System.out.println("Total occurrences found: " + occurrenceCount);
        System.out.printf("Search execution time: %.3f milliseconds%n", durationInMilliseconds);
        System.out.println("Book length: " + n + " characters");
    }
}
