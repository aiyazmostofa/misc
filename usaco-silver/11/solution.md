Let's do some case work.

- If the two endpoints are different, then we will be satisfied.
- If the two endpoints are the same and they match our visitor, then we will be satisfied.
- If the two endpoints are the same and they don't match our visitor, if they are apart of the same connected component, we are not satisfied.
  Connected component meaning sharing the same breed of cow.

That's it.
We can easily query each visitor in $O(1)$ time by either using DFS or a disjoint set.
I used a disjoint set.
