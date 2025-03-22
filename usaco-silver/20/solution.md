Let's start at our starting node with 1 cow.
We can show that it is always optimal to keep cloning until we have enough to pass on to our immediate neighbors.
This is because $2^x \geq x$ for all nonnegative integers.
I used DFS to simulate that travel of cows, but the smartest solution is using a for loop to count edges.