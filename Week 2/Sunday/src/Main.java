public class Main {
    public static void main(String[] args) {
        String T = "carc got hit by another car";
        String P = "car";

        int n = T.length();
        int m = P.length();

        // Start timing
    
        long startTime = System.nanoTime();

        int[] LastPosOf = new int[128];
        for (int a = 0; a < 128; a++) {
            LastPosOf[a] = -1;
        }
        for (int j = 0; j < m; j++) {
            LastPosOf[P.charAt(j)] = j;
        }

        for (int i = 0; i <= n - m; ) {
            int j = 0;
            while (j < m && P.charAt(j) == T.charAt(i + j)) {
                j++;
            }
            if (j == m) {
                System.out.println("Valid shift at: " + i);
            }

            if (i < n - m) {
                i += m - LastPosOf[T.charAt(i + m)];
            }
            else {
                break;
            }

        }
        // End timing
        long endTime = System.nanoTime();
        double durationInMilliseconds = (endTime - startTime) / 1_000_000.0;
        System.out.printf("Execution time: %.3f milliseconds%n", durationInMilliseconds);
    }
}