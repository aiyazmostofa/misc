I did not solve this problem by myself, but this is the solution I came up with after I looked at the editorial.
Let's start from the leaves, and try to make them 12.
Let's keep doing this until we reach the root.
If the root is 12, then it is possible.
One edge case is that if we are in a child of the root, and the root is already 12, then we don't need to traverse back up as long as there isn't another child of the root that is unvisited.