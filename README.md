
# 🧮 Matrix Multiplication using Hadoop MapReduce (Java)

Here's a step-by-step guide to implement Matrix Multiplication using MapReduce in Hadoop (Java):

---

## 🔧 Step 1: Understand the Input Format

Assume you want to compute **C = A × B**, where:

- A is an `m x n` matrix  
- B is an `n x p` matrix  
- Result matrix C is `m x p`

We'll represent matrices in the following text input format for Hadoop:

```
matrix_name i j value
```

### Example

**matrixA.txt**:
```
A 0 0 1
A 0 1 2
A 1 0 3
A 1 1 4
```

**matrixB.txt**:
```
B 0 0 5
B 0 1 6
B 1 0 7
B 1 1 8
```

---

## 📁 Step 2: Set Up Directory Structure in Hadoop Project

```
MatrixMultiplication/
├── src/
│   ├── MatrixMultiplyMapper.java
│   ├── MatrixMultiplyReducer.java
│   └── MatrixMultiplicationDriver.java
├── matrixA.txt
├── matrixB.txt
└── build/
```

---

## 👨‍💻 Step 3: Write Java Code

### 🔹 MatrixMultiplyMapper.java

```java
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import java.io.IOException;

public class MatrixMultiplyMapper extends Mapper<LongWritable, Text, Text, Text> {
    public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        String[] tokens = value.toString().split("\s+");
        String matrixName = tokens[0];
        int i = Integer.parseInt(tokens[1]);
        int j = Integer.parseInt(tokens[2]);
        int val = Integer.parseInt(tokens[3]);

        if (matrixName.equals("A")) {
            for (int k = 0; k < 10; k++)  // assuming B has 10 columns
                context.write(new Text(i + "," + k), new Text("A," + j + "," + val));
        } else {
            for (int k = 0; k < 10; k++)  // assuming A has 10 rows
                context.write(new Text(k + "," + j), new Text("B," + i + "," + val));
        }
    }
}
```

---

### 🔹 MatrixMultiplyReducer.java

```java
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import java.io.IOException;
import java.util.*;

public class MatrixMultiplyReducer extends Reducer<Text, Text, Text, IntWritable> {
    public void reduce(Text key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException {
        Map<Integer, Integer> amap = new HashMap<>();
        Map<Integer, Integer> bmap = new HashMap<>();

        for (Text val : values) {
            String[] tokens = val.toString().split(",");
            if (tokens[0].equals("A")) {
                amap.put(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
            } else {
                bmap.put(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
            }
        }

        int sum = 0;
        for (int k : amap.keySet()) {
            if (bmap.containsKey(k)) {
                sum += amap.get(k) * bmap.get(k);
            }
        }

        context.write(key, new IntWritable(sum));
    }
}
```

---

### 🔹 MatrixMultiplicationDriver.java

```java
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;

public class MatrixMultiplicationDriver {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Matrix Multiplication");

        job.setJarByClass(MatrixMultiplicationDriver.class);
        job.setMapperClass(MatrixMultiplyMapper.class);
        job.setReducerClass(MatrixMultiplyReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(job, new Path(args[0])); // path to A and B combined
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
```

---

## 🚀 Step 4: Compile and Package

```bash
mkdir build
javac -classpath `hadoop classpath` -d build src/*.java
jar -cvf matrixmult.jar -C build/ .
```

---

## 📤 Step 5: Run on Hadoop

```bash
hadoop fs -mkdir /matrix
hadoop fs -put matrixA.txt /matrix/
hadoop fs -put matrixB.txt /matrix/

hadoop jar matrixmult.jar MatrixMultiplicationDriver /matrix /matrix_output
```

---

## 📥 Step 6: Check Output

```bash
hadoop fs -cat /matrix_output/part-r-00000
```

---


# Student Grades Calculation using Hadoop MapReduce

This project demonstrates a simple **MapReduce** program written in **Java** using **Hadoop** to calculate grades of students based on their average marks.

---

## 📝 Input Format

The input file should be structured as follows:

```
student_id subject marks
```

### Example (`students.txt`):
```
101 Math 80
101 Science 70
102 Math 60
102 Science 90
103 Math 50
103 Science 40
```

---

## 🎯 Objective

- Compute the **average marks** of each student.
- Assign a **grade** based on the average.

### 🏷️ Grading Criteria

- **A**: 85–100  
- **B**: 70–84  
- **C**: 50–69  
- **D**: 35–49  
- **F**: Below 35  

---

## 📁 Project Structure (Eclipse-style)

```
StudentGrades/
├── src/
│   ├── StudentGradeMapper.java
│   ├── StudentGradeReducer.java
│   └── StudentGradeDriver.java
├── input/
│   └── students.txt
├── build/
└── studentgrades.jar
```

---

## 👨‍💻 Java Code

### 🔹 StudentGradeMapper.java

```java
import java.io.IOException;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

public class StudentGradeMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] parts = value.toString().split("\s+");
        String studentId = parts[0];
        int marks = Integer.parseInt(parts[2]);

        context.write(new Text(studentId), new IntWritable(marks));
    }
}
```

### 🔹 StudentGradeReducer.java

```java
import java.io.IOException;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

public class StudentGradeReducer extends Reducer<Text, IntWritable, Text, Text> {
    public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int total = 0;
        int count = 0;

        for (IntWritable val : values) {
            total += val.get();
            count++;
        }

        int avg = total / count;
        String grade;

        if (avg >= 85) grade = "A";
        else if (avg >= 70) grade = "B";
        else if (avg >= 50) grade = "C";
        else if (avg >= 35) grade = "D";
        else grade = "F";

        context.write(key, new Text("Average: " + avg + ", Grade: " + grade));
    }
}
```

### 🔹 StudentGradeDriver.java

```java
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;

public class StudentGradeDriver {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Student Grades");

        job.setJarByClass(StudentGradeDriver.class);
        job.setMapperClass(StudentGradeMapper.class);
        job.setReducerClass(StudentGradeReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
```

---

## ⚙️ To Compile and Package

```bash
javac -classpath `hadoop classpath` -d build src/*.java
jar -cvf studentgrades.jar -C build/ .
```

---

## 🚀 Run on Hadoop

```bash
# Create HDFS directory and upload input
hadoop fs -mkdir /grades
hadoop fs -put students.txt /grades/

# Run the MapReduce job
hadoop jar studentgrades.jar StudentGradeDriver /grades /grades_output

# View the results
hadoop fs -cat /grades_output/part-r-00000
```

---

## ✅ Output Example

```
101    Average: 75, Grade: B
102    Average: 75, Grade: B
103    Average: 45, Grade: D
```

