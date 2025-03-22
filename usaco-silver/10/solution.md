This was a pretty fun problem (because I solved it!).
This reminded me of a Leetcode problem I did a couple of months back.
[Last Moment Before All Ants Fall Out of a Plank](https://leetcode.com/problems/last-moment-before-all-ants-fall-out-of-a-plank/)
In fact, this problem is just a more juiced up version of that problem!

There are two observations that are used in this solution.

- The second leftmost cow cannot reach the left barn before the left cow can.
  This is true for all cows, and applies to the right barn in the opposite direction.
- When two cows "collide", we can treat it as it never happening.

The first one can easily be seen with some drawing out.
The second one might not be clear, let me draw it out.
```
C  C
```

```
 CC 
```

```
 CC 
```

```
C  C
```

Depending on how you see it, it just looks like they go past each other.
But what if I told they actually collided?
It doesn't actually matter!

What can we do with this?
Well, now we can easily tell which cows fall into barns first.
If they are moving right, what distance (same as time) they are from the right barn.
Same for left barn.
Use a priority queue to simulate until you have $\sum w /2$ worth of cows in your barn.
Make sure though, that you subtract from the sum either the leftmost or the rightmost (tracked with two pointers).
A cow that goes left but collides with some cows will cause a chain reaction that will eventually lead to the leftmost cow going into the barn.

With this, we can find $T$, which is the time our last cow reached a barn.
How can we find how many collisions happened?
Let's take each cow, and let's assume this cow is going to the right direction.
It will "collide" with all cows going to the left that it can reach in time $T$.
Since the cows going right travels a distance $T$, and a cow going left travels a distance $T$ but in the opposite direction, we can binary search all cows that go left of our right-going cow that is within a distance $2 \times T$ to the right.
Now how about the left cows colliding with right cows?
We can skip this because this overcounts by exactly 2 times.
