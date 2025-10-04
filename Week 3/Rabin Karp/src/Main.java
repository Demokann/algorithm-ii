public class Main {

    public static final int S = 128;

    public static void main(String[] args) {
        String T = "car got hit by another car";
        String P = "car";

        long startTime = System.nanoTime();

        int L = 997;

        search(T, P, L);

        long endTime = System.nanoTime();
        double durationInMilliseconds = (endTime - startTime) / 1_000_000.0;
        System.out.printf("Execution time: %.3f milliseconds%n", durationInMilliseconds);
    }

    public static void search(String T, String P, int L) {
        int n = T.length();
        int m = P.length();
        int p = 0;  //Hash value for pattern
        int t = 0;  //Hash value for text
        int h = 1;

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
                    System.out.println("Pattern found at index: " + i);
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
    }
}