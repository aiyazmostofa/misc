Pretty quickly, I realized that the solution should be around $O(M^2)$.
The problem is, I couldn't implement a solution that worked.
Here is the basic idea of the solution that I ripped from the official solution.
There are only $2 \times M + 1$ beginning positions and $2 \times M + 1$ end positions.
Let's say we are trying at add 2 $a$'s.
To find how many ways we can do that, we can multiply the quantities of both $a$'s.
We can also do that for the $b$'s.
This will lead us to a new interval array that we can easily line sweep to extract the exact quantities of each value.