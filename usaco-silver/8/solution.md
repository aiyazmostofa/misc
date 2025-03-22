This problem made me almost quit this website.

I know line sweeping, I knew this algorithm existed.
But this is USACO Silver!
Them pulling out a topic like line sweeping really messed me up.
Then, after learning the algorithm properly, I got everything except test case 10 correct.
It was only after I took one bite of a carolina reaper tender, spending my time on the lavatory, that I realized that my initial solution used lower key and higher key instead of floor key and ceiling key.
This was problematic because what if two $y$'s were the same.

The main sticking point for me for this algorithm is the idea that segments intersect only if they are at point neighbors, either at a closing or at an opening.
The way I convinced myself of this is the following:

Let's say you have 2 segments.
They are automatically neighbors, so the can either intersect or they don't.
Pretty simple.
Let's add a third segment.
This one can intersect with the first or the second segments.
But this segment will be out of sync with the other segments in terms of insertion and deletion.
So when given the oppurtunity, they could be tested for intersection.

Now let's say there are two segments that are never neighbors.
Since they are never neighbors, we know that the $y_\mathrm{max}$ of one of them will never be greater than or equal to the $y_\mathrm{min}$ of another one.
So they will never intersect, proving that the algorithm does work (at least to me).

For more reading on this algorithm:
[Wikipedia](https://en.wikipedia.org/wiki/Bentley%E2%80%93Ottmann_algorithm)

My code is a little bit funky.
Instead of doing line segment intersection logic, I used the built in Java Line2D class.
Pro tip: if you are dealing with the cartesian plane, you need to flip the y coordinates.
This is because the in built Line2D class is meant for graphics, where the top left is the origin (oriented like a matrix).