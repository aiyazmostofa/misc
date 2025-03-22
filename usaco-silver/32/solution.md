Ok so we have a bunch of connected components, and we want to connect them with the least cost.

Let's think about the problem where only 1 edge is allowed to be added.
Then, we just need to find the closest nodes between the 2 connected components.
To do this, we can loop through each node in one set, which we will assign $a$, and find the floor $b$ and ceiling $c$ values of $a$ in the other set.
Then our value will be the minimum of $\mathrm{min}(|b - a|, |c - a|)$.
To speed this up, always iterate through the smaller set.

For the two edge problem, just loop through each connected component, and add the costs of connecting the start and a middle node, and the middle node to an end node.