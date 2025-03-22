Trying to find the maximum value of $h$?
Sounds like binary search to me.
How can we verify that we can use $h$?
We first see how many papers have $\geq h$ citations.
Then, we start adding citations to the ones with $< h$ citations until they have that number.
We of course start this process on the papers with the most citations $< h$.
Keep doing this until we have $h$ papers or you just give up.