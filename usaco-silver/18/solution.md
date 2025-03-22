This problem was a little confusing to type up, but it wasn't too hard.
Think about the cereals being laid out in a row, and cows are waiting in line for a cereal box.
If they are in the front of the line, they "have" the box.
We can put the cows in both of the lines of the cereals they want.
But, if they get to the front of the line of their top choice, that means that they should not be in line of their second choice.

Let's build a set for each line and assign the cows according to our rules.
Cows should be ordered by their index, which is their "time of arrival".
Count out how many cereal boxes have cows assigned to them, this is our starting "count" value.

For each cow we remove, we see if the new front cow (if there is one) is in another line and our current line is the top choice for the cow, in which case remove it from the other line and repeat on the other line.
Each time we remove, check if the set has been emptied, if which case we should decrement from the count.