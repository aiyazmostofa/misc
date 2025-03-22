First and foremost, sort the array.
This is a silver problem!
So one of the first things you should do is see if binary searching works.
If we would binary search, we would do it on the min-max wait time (we will call this $m$).
The subproblem would then be how many buses would you need if the max wait time for all buses must be less than or equal to $m$.
We can take a greedy approach to solve this subproblem.
Keep adding cows to a bus until you have more than $C$ cows, or you have a max wait time greater than $m$.
Then, add to a bus counter, reset our bus, and keep going.
If the required amount of buses is greater than $M$, then you need more time allocated to you.
Otherwise, you can try to find answers with lower times.
