Let's address the elephant in the room.
The problem is asking for $2 \times A$.
Well, if the sides has to be parallel to the $X$ and $Y$ axis, then it has to be a right triangle.
The area of a right triangle is $\frac{L \times W}{2}$, so we are trying to find $L \times W$, which is an area of a rectangle!
How do we find the sum of all areas?
We can use a property of FOILing.
Let's consider the equation

$(L_1, L_2) \times (W_1, W_2)$.

If we expand this, we get 

$L_1 \times W_1 + L_2 \times W_1 + L_1 \times W_2 + L_2 \times W_2$,

which is the sum of all rectangles that can be made with those lengths and widths.
Now that we have these two observations, let's talk about the solution.

Let's take each point, and take the sum of all $Y$'s that have the same $X$ as our point, and vice versa.
Let's look at just the $Y$ list.
Further more, let's only look at $Y$'s that have a smaller $Y$ than our current point.
We want to convert this to sum of lengths, not sum of coordinates.
I used binary search to find the our original point.
Let's say in our total list that the original point is at index $m$.
Then $m$ points are before our point.
Meaning the sum of the lengths are $m \times Y_m$ - $\sum Y_i$ for all $i < m$.
We can use a prefix sum to effeciently compute the sum.
Finally, just do it for all four cases.
