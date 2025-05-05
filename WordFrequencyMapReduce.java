package matrixMultiplication;

import java.io.*;
import java.util.*;

public class WordFrequencyMapReduce {

    // Mapper function: emits (word, 1) for each occurrence
    public static List<Map.Entry<String, Integer>> map(List<String> lines, String targetWord) {
        List<Map.Entry<String, Integer>> mapped = new ArrayList<>();
        for (String line : lines) {
            String[] words = line.toLowerCase().split("\\W+"); // split on non-word characters
            for (String word : words) {
                if (word.equals(targetWord.toLowerCase())) {
                    mapped.add(new AbstractMap.SimpleEntry<>(word, 1));
                }
            }
        }
        return mapped;
    }

    // Reducer function: sums counts for each word
    public static Map<String, Integer> reduce(List<Map.Entry<String, Integer>> mappedData) {
        Map<String, Integer> frequencyMap = new HashMap<>();
        for (Map.Entry<String, Integer> entry : mappedData) {
            frequencyMap.put(entry.getKey(), frequencyMap.getOrDefault(entry.getKey(), 0) + entry.getValue());
        }
        return frequencyMap;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Get file path and word from user
        System.out.print("Enter the path to the text file: ");
        String filePath = sc.nextLine();

        System.out.print("Enter the word to search for: ");
        String targetWord = sc.nextLine();

        List<String> lines = new ArrayList<>();

        // Read file
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
            return;
        }

        // Map and Reduce
        List<Map.Entry<String, Integer>> mapped = map(lines, targetWord);
        Map<String, Integer> result = reduce(mapped);

        // Output
        System.out.println("\nWord Frequency Result:");
        if (result.containsKey(targetWord.toLowerCase())) {
            System.out.println("'" + targetWord + "' occurred " + result.get(targetWord.toLowerCase()) + " time(s).");
        } else {
            System.out.println("'" + targetWord + "' did not appear in the file.");
        }

        sc.close();
    }
}
