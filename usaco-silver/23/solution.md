Let's not worry about time complexity, and only think about how to solve this problem.
For each query, find how many paints are needed on the left and the right side.
I went iteratively through the 26 letters, seeing how many paints are needed for each letter.
For each letter, if we come across the letter, we start painting.
If we find a letter that is less than our letter, we stop painting.
This will be too slow, but notice that all we are doing is counting how many transitions from a letter to a letter lower for each letter.
So we can use a prefix sum for effecient queries.
The edge case comes from the right segment.
In our current prefix sum, if a stroke is started before our right segment starts, it won't be counted.
It should be counted if that letter appears after the start of our right segment but before a previously accounted for new stroke.
We can do this effeciently with a tree set.