import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
public class wacky1 {
    public static final int S = 128;

    public static void main(String[] args) {
        String textPath1 = "text2.txt";
        String T = "";
        try {
            T = readBookFromFile(textPath1).replace("\n", "").replace("\r", "");
        }catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return;
        } 
        String P = "aaaaaaaaaaaaaab";

        long startTime = System.nanoTime();
        long rabinKarpCount = rabinKarpSearch(T, P);
        long endTime = System.nanoTime();
        double rabinKarpResult = (endTime - startTime) / 1_000_000.0;
        System.out.println("Count: " + rabinKarpCount);
        System.out.printf("Execution time for Rabin-Karp: %.3f milliseconds%n",  + rabinKarpResult);

        startTime = System.nanoTime();
        long sundayCount = sundaySearch(T, P);
        endTime = System.nanoTime();
        double sundayResult = (endTime - startTime) / 1_000_000.0;
        System.out.println("Count: " + sundayCount);
        System.out.printf("Execution time for Sunday: %.3f milliseconds%n",  + sundayResult);
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
}
