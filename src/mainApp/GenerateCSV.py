# Generates a random CSV file of 100 1s and 0s
# Used for testing purposes

import random

FILE_LOCATION = "test.csv"

with open(FILE_LOCATION, "w") as file:
    for i in range(100):
        file.write(str(random.randint(0, 1)) + ",")
    print("Generated file " + FILE_LOCATION)