I didn't complete any problems during December 2023.
I need to go over them, so I'm going to shoe horn in solutions whenever I feel like.
However, I eventually did figure this out by myself without the editorial.

We can use a greedy approach because let's say we start off a tower with the second fattest cow.
Can't we just use the first fattest cow then instead?
We can do that for all cow comparisons.

So the hard part is now trying to simulate this.
Let's think about it like this.
Let's sort the cows by heaviest (I just traversed backwards).
Let's call our current cow the fattest.
Let's make new towers with our fattest cows if now already allocated.
Use a sorted set to find the fattest cow that still is $K$ less weight than fattest.
Then, just add them on top of the fattest cows.
Go the next fattest cow and repeat.
Do this until all of our fattest cows have been covered.
Then, go to the next fattest cow.
We can keep track of this using maps.
