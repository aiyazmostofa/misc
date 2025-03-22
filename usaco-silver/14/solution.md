I almost got this problem!
9/10 TC passed.
The problem with my initial solution was that after finding the cycle length for each element (which was done effeciently), I would manually simulate each index to find the correct value.
I read the editorial, and couldn't really understand the code they gave, but it gave me a great idea to use a hash map to store each cycle's values, so all I have to do is some math to find the correct value.

Let's simulate a reversal and map it's corresponding values to an array.
This will be our "translation" array.
A cycle that appears in this array will be at most $N$, so we can reduce our $K$ by modding by at most $N$.
Find and store all cycles.
Let's go through each index.
We can find which cycle it's apart of, and we can find where it is in the cycle (using another array that you could make earlier), and just add $K$ to it and mod it by the cycle length to find the index of the new value (which is in the cycle).
