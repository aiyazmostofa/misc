I was able to figure out that we need to find how many connected components are there, an edge being defined as two particles interacting.
I had an initial solution that went from the left side with a tree set, but I forgot to account for the interactions that could happen on the right of a particle.
You can notice that the "parent" of every connect component is a part of the top right part of the convex hull of all the particles.
Because of this, if the smallest current particle has a larger $y$ coordinate than all the particles to it's right, it is the start of a new connected component.
We can effeciently compute this with a prefix min and max.