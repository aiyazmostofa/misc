It took me a while to do this problem, but I finally found the trick to this problem.
Everything has to be aligned by its columns (or rows depending on how you look at it) for the matrix to be solvable.
```
LLR
LLR
LLR
```
Any of it's rows can be flipped and it would still be valid.
```
LLR
RRL
LLR
```
Any row that doesn't match "LLR" or "RRL" is the one we need to look at.
To find this row, we can do $N^2$ comparisons and compare each row.
If two rows don't match, they should both be added to collision counter.
This will take $O(N^3)$ time.
The row with the most counted collisions is our row in question (the earliest row if two are equal).

Then, we can see which cow in our row can we flip so that the it matches with all other rows.
This can be done in $O(N^3)$ time.

If we don't find an answer, we can print "-1".
