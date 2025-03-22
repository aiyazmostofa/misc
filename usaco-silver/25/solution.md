When I first tried this problem, I was too focused on trying to add all the cows into a map, instead of a matrix.
I fixed it when I eventually looked at the solution.
But I had the logic down.
Every time you add a cow $(x,y)$, you see if $(x+1,y)$, $(x-1,y)$, $(x,y+1)$, $(x,y-1)$, or $(x,y)$ itself is adjacent to 3 cows.
If it is, add the necessary cows and repeat.
I copied the solution and used BFS.