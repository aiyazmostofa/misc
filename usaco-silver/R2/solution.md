This was an easier problem than I thought.
Next time, I shouldn't just see cycle and think it's a graph problem.

If $a$ and $b$ don't have a common element, it is possible for both of them to share it.
If one of them contains an element and the other one doesn't, it's impossible for them to share that element, because if then it should be a part of the cycle for the other one.
So we just need to see how to shift elements so that they match up.
An element in $a$ is a certain distance away from that same element in $b$.
So we would need to shift it by that much in order for them to match.
Let's call this parity.
Find the parity of each shared element, and which parity has the most count is our winner.
And the cycle could be in reverse, as test case 3 indicates, so just reverse one of the lists and do it again.
