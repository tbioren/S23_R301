# Generates a random CSV file of 100 1s and 0s
# Used for testing purposes

import random

if __name__ == "__main__":
    with open("test.csv", "w") as file:
        for i in range(100):
            file.write(str(random.randint(0, 1)) + ",")