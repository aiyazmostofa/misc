This is a cycle question.
First we simulate the $K$ swaps, storing what positions each cow land on in a set dedicated for that cow.
The list of these sets, we will call $s$.
Then let's think about how to brute force this.
For each cow, we will keep a new set that will contain ALL possible positions, including ones in the future.
Let's think about it like this.
Let's say our cycle is $1 \rightarrow 3 \rightarrow 2 \rightarrow 1$.
Then our new set $t$ will be composed like $t = s_1 + s_3 + s_2$.
In Java, we can keep using the addAll method.
This will run in $O(N^2)$ time.
But since cow $2$ and $3$ also share the same set of positions, we can skip a lot of work by not recomputing those cycles.
This will now run in $O(N)$ time.