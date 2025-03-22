$N$ nodes connected with $N-1$ edges is a fancy way of saying trees.
Lets call the set of colors $c$ and our nodes $a$.
Let's assign $a_1$ to $c_1$.
Let's do DFS starting from $a_1$, assigning the neighbors of $a_1$: $c_2$, $c_3$, etc.
Then, let's traverse to one of the neighbors of $a_1$.
For example, let's say $a_1 \leftrightarrow a_2$ and $a_2$ has been assigned $c_2$.
What can we assign the neighbors of $a_2$?
We can't assign it $c_1$ or $c_2$, but can we assign it $c_3$?
Let's say you couldn't.
That would mean there is a neighbor of $a_2$ with $c_3$.
But that would mean that there are connections $a_1 \leftrightarrow a_3$, $a_1 \leftrightarrow a_2$, and $a_3 \leftrightarrow a_2$, which indicates a cycle.
Since trees don't have cycles, this is a contradiction, proving that you can assign $c_3$.
Generalizing this, let's say the current node $a_v$ is assigned $c_v$ and the node before $a_v$ was assigned $c_p$.
To assign the first neighbor of $a_v$, we just need to find the lowest value of $i$ (starting from 1) such that $c_i \neq c_v$ and $c_i \neq c_p$.
And for the next neighbors of $a_v$, we find an $i$ that is greater than the one assigned to the first neighbor and that still matches those conditions.
For the sake of the code, we will subtract everything by 1, and the solution will be 0-indexed.
