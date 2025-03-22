There are $3^9$ game boards.
Since we have a $25^2$ grid, we have $3^9 \times 25^2$ states.
We can store a game instance in an int by using base 3 numbers.
Lets mark each cell $0 \ldots 8$.
We can have a state $s_p$ at position $p$ be blank (0), M (1), or O (2).
So we can represent the game as $\sum_{p=0}^{8} s_p \times 3^p$.
Then just do DFS.
You want to avoid doing straight DFS, as you are able to revisit cells.
However, you shouldn't revisit cells if the state of your cow has not changed.