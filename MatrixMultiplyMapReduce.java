package matrixMultiplication;


import java.util.*;

public class MatrixMultiplyMapReduce {

    // Intermediate key-value pair for map output
    static class Pair {
        int i, j;
        int value;

        Pair(int i, int j, int value) {
            this.i = i;
            this.j = j;
            this.value = value;
        }
    }

    // Map function (simulated)
    public static List<Pair> map(int[][] A, int[][] B) {
        List<Pair> intermediate = new ArrayList<>();

        int m = A.length;
        int n = A[0].length;
        int p = B[0].length;

        for (int i = 0; i < m; i++) {
            for (int k = 0; k < n; k++) {
                for (int j = 0; j < p; j++) {
                    intermediate.add(new Pair(i * 1000 + j, k, A[i][k] * B[k][j]));
                }
            }
        }

        return intermediate;
    }

    // Reduce function (simulated)
    public static int[][] reduce(List<Pair> intermediate, int m, int p) {
        Map<Integer, List<Pair>> grouped = new HashMap<>();

        for (Pair pair : intermediate) {
            grouped.putIfAbsent(pair.i, new ArrayList<>());
            grouped.get(pair.i).add(pair);
        }

        int[][] result = new int[m][p];

        for (Map.Entry<Integer, List<Pair>> entry : grouped.entrySet()) {
            int row = entry.getKey() / 1000;
            int col = entry.getKey() % 1000;
            int sum = 0;
            for (Pair pair : entry.getValue()) {
                sum += pair.value;
            }
            result[row][col] = sum;
        }

        return result;
    }

    // Main function with user input
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter dimensions of Matrix A (rows and columns):");
        int m = sc.nextInt();
        int n = sc.nextInt();
        int[][] matrixA = new int[m][n];

        System.out.println("Enter elements of Matrix A:");
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                matrixA[i][j] = sc.nextInt();

        System.out.println("Enter number of columns in Matrix B:");
        int p = sc.nextInt();
        int[][] matrixB = new int[n][p];

        System.out.println("Enter elements of Matrix B:");
        for (int i = 0; i < n; i++)
            for (int j = 0; j < p; j++)
                matrixB[i][j] = sc.nextInt();

        // Map and Reduce
        List<Pair> mapped = map(matrixA, matrixB);
        int[][] result = reduce(mapped, m, p);

        System.out.println("Matrix A × B =");
        for (int[] row : result) {
            System.out.println(Arrays.toString(row));
        }

        sc.close();
    }
}
