import sys

# Define matrix dimensions
m = 2  # rows of A
n = 2  # columns of A = rows of B
p = 2  # columns of B

def mapper():
    for line in sys.stdin:
        line = line.strip()
        matrix, i, j, value = line.split("\t")
        i, j, value = int(i), int(j), float(value)
        if matrix == "A":
            for k in range(1, p + 1):  # p columns in B
                print(f"{i}\t{k}\tA\t{j}\t{value}")
        elif matrix == "B":
            for k in range(1, m + 1):  # m rows in A
                print(f"{k}\t{j}\tB\t{i}\t{value}")

def reducer():
    current_key = None
    A_elements = {}
    B_elements = {}

    for line in sys.stdin:
        line = line.strip()
        i, j, matrix, k, value = line.split("\t")
        i, j, k, value = int(i), int(j), int(k), float(value)

        key = (i, j)

        if matrix == "A":
            if key not in A_elements:
                A_elements[key] = {}
            A_elements[key][k] = value
        elif matrix == "B":
            if key not in B_elements:
                B_elements[key] = {}
            B_elements[key][k] = value

    # Now multiply
    for i in range(1, m+1):
        for j in range(1, p+1):
            result = 0
            for k in range(1, n+1):
                a_val = A_elements.get((i, k), {}).get(k, 0)
                b_val = B_elements.get((k, j), {}).get(k, 0)
                result += a_val * b_val
            print(f"{i}\t{j}\t{result}")

def main():
    input_data = [
        "A\t1\t1\t1.0",
        "A\t1\t2\t2.0",
        "A\t2\t1\t3.0",
        "A\t2\t2\t4.0",
        "B\t1\t1\t5.0",
        "B\t1\t2\t6.0",
        "B\t2\t1\t7.0",
        "B\t2\t2\t8.0"
    ]

    from io import StringIO

    # Simulate mapper
    sys.stdin = StringIO("\n".join(input_data))
    mapper_output = StringIO()
    sys.stdout = mapper_output
    mapper()

    # Simulate reducer
    sys.stdin = StringIO(mapper_output.getvalue())
    sys.stdout = sys.__stdout__
    reducer()

if __name__ == "__main__":
    main()
