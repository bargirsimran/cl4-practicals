from io import StringIO
import sys
import os

def mapper(text, target_word):
    sys.stdin = StringIO(text.lower())  # Convert to lowercase for case-insensitive comparison
    output = StringIO()
    sys.stdout = output

    for line in sys.stdin:
        line = line.strip()
        words = line.split()
        for word in words:
            clean_word = word.strip('.,!?;:\'"')  # Clean punctuation
            if clean_word == target_word.lower():
                print(f"{clean_word}\t1")

    sys.stdout = sys.__stdout__
    return output.getvalue()

def reducer(mapped_data):
    sys.stdin = StringIO(mapped_data)
    current_word = None
    current_count = 0

    for line in sys.stdin:
        line = line.strip()
        if not line:
            continue
        word, count = line.split('\t')
        count = int(count)

        if word == current_word:
            current_count += count
        else:
            if current_word:
                print(f"{current_word}\t{current_count}")
            current_word = word
            current_count = count

    if current_word == word:
        print(f"{current_word}\t{current_count}")

def main():
    file_path = input("Enter the path to the text file: ").strip()
    if not os.path.exists(file_path):
        print(f"File not found: {file_path}")
        return

    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            input_text = f.read()
    except Exception as e:
        print(f"Error reading file: {e}")
        return

    target_word = input("Enter the word to count: ").strip()

    mapped = mapper(input_text, target_word)
    reducer(mapped)

if __name__ == "__main__":
    main()
