package matrixMultiplication;

import java.util.*;

public class StudentGradesMapReduce {

    static class StudentRecord {
        String name;
        String subject;
        int marks;

        StudentRecord(String name, String subject, int marks) {
            this.name = name;
            this.subject = subject;
            this.marks = marks;
        }
    }

    public static List<Map.Entry<String, Integer>> map(List<StudentRecord> records) {
        List<Map.Entry<String, Integer>> result = new ArrayList<>();

        for (StudentRecord record : records) {
            result.add(new AbstractMap.SimpleEntry<>(record.name, record.marks));
        }

        return result;
    }

    public static Map<String, String> reduce(List<Map.Entry<String, Integer>> mappedData) {
        Map<String, List<Integer>> grouped = new HashMap<>();

        for (Map.Entry<String, Integer> entry : mappedData) {
            grouped.putIfAbsent(entry.getKey(), new ArrayList<>());
            grouped.get(entry.getKey()).add(entry.getValue());
        }

        Map<String, String> grades = new HashMap<>();

        for (Map.Entry<String, List<Integer>> entry : grouped.entrySet()) {
            String name = entry.getKey();
            List<Integer> marks = entry.getValue();

            double average = marks.stream().mapToInt(Integer::intValue).average().orElse(0);

            String grade;
            if (average >= 90) grade = "A";
            else if (average >= 80) grade = "B";
            else if (average >= 70) grade = "C";
            else if (average >= 60) grade = "D";
            else grade = "F";

            grades.put(name, "Average: " + average + ", Grade: " + grade);
        }

        return grades;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<StudentRecord> records = new ArrayList<>();

        System.out.print("Enter number of student records: ");
        int count = sc.nextInt();
        sc.nextLine(); // consume newline

        for (int i = 0; i < count; i++) {
            System.out.println("Record " + (i + 1) + ":");
            System.out.print("Student name: ");
            String name = sc.nextLine();

            System.out.print("Subject: ");
            String subject = sc.nextLine();

            System.out.print("Marks: ");
            int marks = sc.nextInt();
            sc.nextLine(); // consume newline

            records.add(new StudentRecord(name, subject, marks));
        }

        List<Map.Entry<String, Integer>> mapped = map(records);
        Map<String, String> grades = reduce(mapped);

        System.out.println("\nStudent Grades:");
        for (Map.Entry<String, String> entry : grades.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }

        sc.close();
    }
}
