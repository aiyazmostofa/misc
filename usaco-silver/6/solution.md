When I first tried to do this problem, my initial approach was to assign colors to each pasture going through each cow.
This did not work because you do not know what order the cows came in, so you would've assigned a pasture type that could've been restricted by another cow later on.
The more I thought, the more this looked like a graph problem.

This problem boils down to how many connected components $(k)$ are there in the graph.
That is simple DFS.
Our answer would be $2^k$, which we can represent in binary as 1 followed be $k$ zeroes.

But the catch is that we could have an invalid set of cow wants.
So in our DFS method where we check if we have visited a pasture, we need to check if that relationship can still be held.
For example, let's say we visited pastures $A$, $B$, $C$.
If $A = B$ and $B \neq C$, then you need to check that $A \neq C$.
