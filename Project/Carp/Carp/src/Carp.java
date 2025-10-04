import java.util.Random;

public class Carp {

    public static class Hash2D {
        private final int BASE1 = 257; // Row base
        private final int BASE2 = 263; // Column base
        private final int MASK = (1 << 31) - 1;

        int[][] matrix;
        int m, n, k;
        long[] basePowCol, basePowRow;

        public Hash2D(int[][] matrix, int k) {
            this.matrix = matrix;
            this.m = matrix.length;
            this.n = matrix[0].length;
            this.k = k;

            basePowCol = new long[k];
            basePowRow = new long[k];
            basePowCol[0] = basePowRow[0] = 1;

            for (int i = 1; i < k; i++) {
                basePowCol[i] = (basePowCol[i - 1] * BASE2) & MASK;
                basePowRow[i] = (basePowRow[i - 1] * BASE1) & MASK;
            }
        }

        /**
         * Compute hash of K×K submatrix from scratch at (i, j)
         */
        public long computeHash(int i, int j) {
            long hash = 0;
            for (int r = 0; r < k; r++) {
                long rowHash = 0;
                for (int c = 0; c < k; c++) {
                    rowHash = (rowHash * BASE2 + matrix[i + r][j + c]) & MASK;
                }
                hash = (hash * BASE1 + rowHash) & MASK;
            }
            return hash;
        }

        /**
         * Efficient search for first duplicate of top-right K×K submatrix
         */
        public int[] findFirstDuplicate() {
            long[][] rowHashes = new long[m][n - k + 1];

            // Step 1: compute rolling row hashes
            for (int i = 0; i < m; i++) {
                long hash = 0;
                for (int j = 0; j < k; j++) {
                    hash = (hash * BASE2 + matrix[i][j]) & MASK;
                }
                rowHashes[i][0] = hash;
                for (int j = 1; j <= n - k; j++) {
                    hash = (
                        ((hash - matrix[i][j - 1] * basePowCol[k - 1]) * BASE2 + matrix[i][j + k - 1])
                        & MASK
                    );
                    rowHashes[i][j] = hash;
                }
            }

            // Step 2: vertical rolling over row hashes
            long targetHash = computeHash(0, n - k);

            for (int j = 0; j <= n - k; j++) {
                long hash = 0;
                for (int i = 0; i < k; i++) {
                    hash = (hash * BASE1 + rowHashes[i][j]) & MASK;
                }

                if (!(0 == 0 && j == n - k) && hash == targetHash &&
                    areSubmatricesEqual(matrix, 0, n - k, 0, j, k)) {
                    return new int[]{0, j};
                }

                for (int i = 1; i <= m - k; i++) {
                    hash = (
                        ((hash - rowHashes[i - 1][j] * basePowRow[k - 1]) * BASE1 + rowHashes[i + k - 1][j])
                        & MASK
                    );

                    if (!(i == 0 && j == n - k) && hash == targetHash &&
                        areSubmatricesEqual(matrix, 0, n - k, i, j, k)) {
                        return new int[]{i, j};
                    }
                }
            }

            return null;
        }
    }

    private static boolean areSubmatricesEqual(int[][] matrix, int r1, int c1, int r2, int c2, int k) {
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
                if (matrix[r1 + i][c1 + j] != matrix[r2 + i][c2 + j]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Generate matrix with duplicate top-right K×K block
     */
    public static TestResult generateMatrixWithDuplicate(int M, int N, int K, Random rand) {
        if (K > M || K > N || M < K + 1 || N < K + 1) {
            throw new IllegalArgumentException("Invalid dimensions");
        }

        int[][] matrix = new int[M][N];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                matrix[i][j] = rand.nextInt(10);
            }
        }

        int duplicateRow, duplicateCol;
        do {
            duplicateRow = rand.nextInt(M - K + 1);
            duplicateCol = rand.nextInt(N - K + 1);
        } while (duplicateRow == 0 && duplicateCol == N - K);

        for (int i = 0; i < K; i++) {
            for (int j = 0; j < K; j++) {
                matrix[duplicateRow + i][duplicateCol + j] = matrix[i][N - K + j];
            }
        }

        return new TestResult(matrix, duplicateRow, duplicateCol);
    }

    public static class TestResult {
        public final int[][] matrix;
        public final int expectedRow;
        public final int expectedCol;

        public TestResult(int[][] matrix, int expectedRow, int expectedCol) {
            this.matrix = matrix;
            this.expectedRow = expectedRow;
            this.expectedCol = expectedCol;
        }
    }

    public static void printMatrix(int[][] matrix, int K) {
        int M = matrix.length, N = matrix[0].length;
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                if (i < K && j >= N - K) {
                    System.out.print("[" + matrix[i][j] + "]");
                } else {
                    System.out.printf("%2d ", matrix[i][j]);
                }
            }
            System.out.println();
        }
    }

    public static void printSubmatrix(int[][] matrix, int row, int col, int K, String label) {
        System.out.println(label + " at (" + row + ", " + col + "):");
        for (int i = 0; i < K; i++) {
            for (int j = 0; j < K; j++) {
                System.out.printf("%2d ", matrix[row + i][col + j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int M = 8, N = 10, K = 3;
        Random rand = new Random();

        TestResult result = generateMatrixWithDuplicate(M, N, K, rand);
        int[][] matrix = result.matrix;
        int expectedRow = result.expectedRow;
        int expectedCol = result.expectedCol;

        System.out.println("Generated Matrix:");
        printMatrix(matrix, K);
        printSubmatrix(matrix, 0, N - K, K, "Top-right");
        printSubmatrix(matrix, expectedRow, expectedCol, K, "Expected duplicate");

        long start = System.nanoTime();
        Hash2D hasher = new Hash2D(matrix, K);
        int[] found = hasher.findFirstDuplicate();
        long end = System.nanoTime();

        System.out.println("=== Result ===");
        if (found != null) {
            System.out.println("Found at: (" + found[0] + ", " + found[1] + ")");
            System.out.println("Matches expected: " + (found[0] == expectedRow && found[1] == expectedCol));
            if (found[0] != expectedRow || found[1] != expectedCol) {
                printSubmatrix(matrix, found[0], found[1], K, "Actually Found");
            }
        } else {
            System.out.println("No duplicate found.");
        }

        System.out.printf("Execution time: %.4f ms\n", (end - start) / 1_000_000.0);
    }
}
