First, sort the cows by time of arrival, breaking ties with index (which is already done because merge sort is a stable algorithm).
We put the first cow in a priority queue (that sorts by seniority), and we keep track of the current time and max wait.
In each priority queue poll, we loop through the cows that fall in between the time range of our current cow, and add them to the priority queue.
If we have cows left, but the priority queue is empty, we add a new cow as well.
And because of this, a cow can start completely after another cow has finished, so we need to change the time accordingly.
Finding the max can be done by comparing the current time and the cows arrival time after each poll.
