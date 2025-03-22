The critical thing to notice is that our condition of $2/4$ cows in every $2 \times 2$ square is met iff either there are no touching vertical cows or no touching horizontal cows.
It can be either or, so let's look at an optimal vertical.
```
XXX
OOO
XXX
```
We can pretty easily see that row 1 and 3 repeat, and presumably 2 and 4 would as well.
So we will take either the biggest of columns $1, 3, 5 \ldots$ or $2, 4, 6 \ldots$.
We should do this for the horizontal case as well and see which of the two orientations produces the maximum.