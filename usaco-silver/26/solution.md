For this problem, the first thing that is obvious is that we need to travel back far enough to reach the oldest ancestor.
Then, there are gaps between each ancestor (which we can find with sorting).
We can calculate how much a portal can save us time for each gap, then greedily use the $K-1$ portals that save us the most time.
To calculate the floor and ceiling multiples of 12, we can do $\left\lfloor{\frac{x}{12}}\right\rfloor \times 12$ and $\left\lfloor{\frac{x+11}{12}}\right\rfloor \times 12$.