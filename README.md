# negatronic

A simple AI demonstration of the [Negamax algorithm](https://en.wikipedia.org/wiki/Negamax) with alpha/beta pruning.

Game rules are a Mancala variant known as [das Bohnenspiel](https://en.wikipedia.org/wiki/Das_Bohnenspiel).

There are two additional "house" rules to facilitate computer play:

  - A player without stones passes until they have stones to play (instead of the game ending)
  - A position that has been repeated three times results in the game ending with remaining stones divided equally

The first rule is my personal preference to prevent hoarding / starvation strategies. There is also some historical precedent to suggest it has been played this way in the past.

The second rule is not usually used in human play, but is common in computer tournaments for a similar Mancala game, [Oware](https://en.wikipedia.org/wiki/Oware).  I also prefer using this rule in human play since it is quite feasible for a losing player to force an endless cycle with two remaining stones.

## Using the software

Requires Scala 2.11.8 and SBT 0.13.11.  [ScalaTest](http://www.scalatest.org) is required to run tests.  No other libraries are required.

To run unit tests, execute `sbt test` in the project directory.

To play against the computer, execute `sbt run`.

## Implementation notes

The Negamax walk algorithm has been reimplemented to be more functional in style as compared to the usual reference imperative code.  The depth parameter is dynamically scaled to increase as the board simplifies.

The state heuristic uses a probabilistic model of the expected value of the state (+1.0 for player one winning, 0 for draw, and -1.0 for player two winning) under a hypothetical random fair division of the remaining stones.  This has some nice properties, such as automatically detecting when a lead in score is insurmountable, preferring trades when ahead in score, and avoiding trades when behind.

For speed, the heuristic is calculated using a custom `Math` module that provides memoized [binomial distribution](https://en.wikipedia.org/wiki/Binomial_distribution) functions.
