Stuck in a Rut was the most memorable bronze problem I did.
This was because when I did a DSA Coursera course, they covered simulation of intersections, where they said to focus on the "interesting times".
We will use a similar idea here.
First put all possible intersections in a priority queue, including ones that might never happen because a cow stopped before it could ever happen.
Going through the priority queue, see if each intersection is possible by seeing if the path a cow hits still is there.
If it is, then keep track of where the cow stopped.
Then, draw a unidirectional edge from the blamer to the blamee.
Repeat until your priority queue is empty.
Then for each cow, run DFS on the graph and see how many children of this node are there.