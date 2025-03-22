My first intuition was to use RMQ.
The problem is first that silver doesn't cover RMQ, and also I am not very good at implementing it.
It is clear that the intended solution was around $O(N^3)$, so I could probably fix the top and bottom rows.
Then, just focus on all of the rows in the middle.
When you think about it, we know that there is a minimum of 100 iff the number of elements that are 100 are greater than 0, and the number of elements less than 100 is 0.
We can easily use a prefix sum to compute these.
After we compute which columns have these properties, we need to also realize that columns that don't contain 100, but have only elements greater than 100 still should be included in our calculation, only if they can be grouped in with a column with 100.

Ok, so let's say we have figured all this out.
Let's go through each column that has 100, and has no elements smaller.
Find the farthest left and right pointers from our point such that the columns only have cells with values greater than or equal 100.
Then, we can multiply our left distance by our right distance to find all subarrays that have our desired property, and add that to a total sum.
To prevent overcounting, the left pointer stops if it finds a column with a cell of 100.
I was too lazy to optimize the left pointer, so this solution is technically $O(N^4)$.