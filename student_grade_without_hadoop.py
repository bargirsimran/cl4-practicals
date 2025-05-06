import sys
from io import StringIO

def mapper():
    for line in sys.stdin:
        line = line.strip()
        if not line:
            continue
        parts = line.split()
        if len(parts) != 2:
            continue
        name, score = parts
        score = float(score)
        if score >= 90:
            grade = 'A'
        elif score >= 80:
            grade = 'B'
        elif score >= 70:
            grade = 'C'
        else:
            grade = 'F'
        print(f"{name}\t{grade}")

def reducer():
    for line in sys.stdin:
        print(line.strip())

def main():
    print("Enter student name and score separated by space (Press Enter without input to stop):")
    input_lines = []
    while True:
        line = input()
        if line.strip() == "":
            break
        input_lines.append(line)

    # Prepare mapper input
    sys.stdin = StringIO("\n".join(input_lines))
    mapper_output = StringIO()

    # Run Mapper
    print("\nRunning Mapper...")
    sys.stdout = mapper_output
    mapper()
    
    # Prepare reducer input
    sys.stdin = StringIO(mapper_output.getvalue())
    sys.stdout = sys.__stdout__

    # Run Reducer
    print("\nRunning Reducer...")
    reducer()

if __name__ == "__main__":
    main()
