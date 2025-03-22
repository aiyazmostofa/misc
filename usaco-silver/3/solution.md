Simulation is the key to this problem.
Let's first make a function that simulates the gravity.
Let's look at each column, and make a list of all non-zero values.
Then, just replace that column with that list and a bunch of zeros.
You would do this in reverse of course.
Next, we can use DFS on every point (ignoring points that were already visited or is zero), and see how many items are in a connected and have the same color.
If that point has more than or equal to $K$ items connected to it that are the same color, we can add that point to a list.
If the final point list is empty, then we are done simulating.
Otherwise, run DFS on all those points to remove the connected components.
Finally, run the gravity function.
I ran the gravity function before anything else just in case it the initial board wasn't already floored.
