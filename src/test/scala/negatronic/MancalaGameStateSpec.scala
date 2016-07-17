package negatronic

import org.scalatest._

/**
 * Created by darnold on 7/10/16.
 */
class MancalaGameStateSpec extends FlatSpec with Matchers {
  "State" should "be empty if all pits are 0" in {
    val state = new MancalaGameState(Seq(0, 0, 0, 0), 4, 4, 1)

    state.isTerminal should be (true)
  }

  it should "not be empty if any pit is not 0" in {
    val state = new MancalaGameState(Seq(0, 0, 1, 0), 0, 0, 1)

    state.isTerminal should be (false)
  }

  it should "only list non empty pits on player one's side as legal pits" in {
    val state = new MancalaGameState(Seq(1, 0, 1, 0, 0, 1, 1, 0), 2, 3, 1)

    state.legalPits should be (Seq(0, 2))
  }

  "A flipped state" should "be the reverse of the original" in {
    val state = new MancalaGameState(Seq(1, 2, 3, 4), 5, 6, 1)
    val flippedState = state.flip

    flippedState.pits should be (Seq(3, 4, 1, 2))
    flippedState.player1Score should be (6)
    flippedState.player2Score should be (5)
    flippedState.color should be (-1)
  }

  it should "flip back to the same state" in {
    val state = new MancalaGameState(Seq(1, 2, 3, 4), 5, 6, 1)
    val doubleFlippedState = state.flip.flip

    doubleFlippedState.pits should be (Seq(1, 2, 3, 4))
    doubleFlippedState.player1Score should be (5)
    doubleFlippedState.player1Score should be (5)
    doubleFlippedState.color should be (1)
  }

  "A state heuristic" should "be 0 when no stones are captured" in {
    val state = new MancalaGameState(Seq(1, 2, 2, 3), 0, 0, 1)

    state.heuristic should be (0)
  }

  it should "be 0 when the captured stones are equal" in {
    val state = new MancalaGameState(Seq(0, 1, 1, 2), 2, 2, 1)

    state.heuristic should be (0)
  }

  it should "be positive when player one's captured stones are greater" in {
    val state = new MancalaGameState(Seq(1, 2, 3, 4), 6, 4, 1)

    state.heuristic should be > 0.0
  }

  it should "be negative when player one's captured stones are fewer" in {
    val state = new MancalaGameState(Seq(1, 2, 3, 4), 4, 6, 1)

    state.heuristic should be < 0.0
  }

  it should "be negative when player one's captured stones are greater and it's player two's turn" in {
    val state = new MancalaGameState(Seq(1, 2, 3, 4), 6, 4, -1)

    state.heuristic should be < 0.0
  }

  it should "be positive when player one's captured stones are fewer and it's player two's turn" in {
    val state = new MancalaGameState(Seq(1, 2, 3, 4), 4, 6, -1)

    state.heuristic should be > 0.0
  }

  it should "be +1 when player one has won" in {
    val state = new MancalaGameState(Seq(0, 0, 0, 0), 2, 6, -1)

    state.heuristic should be (1)
  }

  it should "be -1 when player two has won" in {
    val state = new MancalaGameState(Seq(0, 0, 0, 0), 6, 2, -1)

    state.heuristic should be (-1)
  }

  it should "have a maximum of 1" in {
    val state = new MancalaGameState(Seq(0, 0, 0, 0), 8, 0, 1)

    state.heuristic should be (1.0)
  }

  it should "have a minimum of -1" in {
    val state = new MancalaGameState(Seq(0, 0, 0, 0), 0, 8, 1)

    state.heuristic should be (-1.0)
  }

  "A move" should "distribute stones anticlockwise" in {
    val state = new MancalaGameState(Seq(2, 2, 2, 2), 0, 0, 1)
    val movedState = state.move(1).flip

    movedState.pits should be (Seq(2, 0, 3, 3))
  }

  it should "capture on 2 stones" in {
    val state = new MancalaGameState(Seq(1, 1, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6), 6, 4, 1)
    val movedState = state.move(0).flip

    movedState.pits should be (Seq(0, 0, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6))
    movedState.player1Score should be (8)
    movedState.player2Score should be (4)
  }

  it should "capture on 4 stones" in {
    val state = new MancalaGameState(Seq(1, 3, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6), 4, 4, 1)
    val movedState = state.move(0).flip

    movedState.pits should be (Seq(0, 0, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6))
    movedState.player1Score should be (8)
    movedState.player2Score should be (4)
  }

  it should "capture on 6 stones" in {
    val state = new MancalaGameState(Seq(1, 5, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6), 4, 2, 1)
    val movedState = state.move(0).flip

    movedState.pits should be (Seq(0, 0, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6))
    movedState.player1Score should be (10)
    movedState.player2Score should be (2)
  }

  it should "not capture on 8 stones" in {
    val state = new MancalaGameState(Seq(1, 7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6), 4, 0, 1)
    val movedState = state.move(0).flip

    movedState.pits should be (Seq(0, 8, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6))
    movedState.player1Score should be (4)
    movedState.player2Score should be (0)
  }

  it should "chain captures in reverse order" in {
    val state = new MancalaGameState(Seq(5, 1, 7, 1, 3, 5, 6, 6, 6, 6, 6, 6), 8, 6, 1)
    val movedState = state.move(0).flip

    movedState.pits should be (Seq(0, 2, 8, 0, 0, 0, 6, 6, 6, 6, 6, 6))
    movedState.player1Score should be (20)
    movedState.player2Score should be (6)
  }

  it should "wrap around the board" in {
    val state = new MancalaGameState(Seq(0, 0, 0, 18, 0, 0, 0, 0, 0, 0, 0, 0), 28, 26, 1)
    val movedState = state.move(3).flip

    movedState.pits should be (Seq(1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1))
    movedState.player1Score should be (40)
    movedState.player2Score should be (26)
  }

  "A generated move" should "be empty if there are no stones on the board" in {
    val state = new MancalaGameState(Seq(0, 0, 0, 0), 6, 2, 1)

    state.enumerateMoves should be (empty)
  }

  it should "be flipped if the player has no stones" in {
    val state = new MancalaGameState(Seq(0, 0, 1, 1), 4, 2, 1)

    val generatedMoves = state.enumerateMoves
    generatedMoves should have length 1

    val generatedMove = generatedMoves.head
    generatedMove.pits should be (Seq(1, 1, 0, 0))
    generatedMove.player1Score should be (2)
    generatedMove.player2Score should be (4)
    generatedMove.color should be (-1)
  }

  it should "map the legal pits" in {
    val state = new MancalaGameState(Seq(2, 2, 2, 2), 4, 6, 1)

    val generatedMoves = state.enumerateMoves
    generatedMoves should have length 2

    val move1 = generatedMoves(0)
    move1.pits should be (Seq(3, 2, 0, 3))
    move1.player1Score should be (6)
    move1.player2Score should be (4)
    move1.color should be (-1)

    val move2 = generatedMoves(1)
    move2.pits should be (Seq(3, 3, 2, 0))
    move2.player1Score should be (6)
    move2.player2Score should be (4)
    move2.color should be (-1)
  }

  "A repeated state" should "be detected when it has occurred for the third time" in {
    val state = new MancalaGameState(Seq(1, 1, 0, 0), 0, 6, -1)

    val firstRepeat = state.move(1).move(0).move(0).move(1)
    firstRepeat.isRepeat should be (false)

    val secondRepeat = firstRepeat.move(1).move(0).move(0).move(1)
    secondRepeat.isRepeat should be (true)
  }

  it should "be terminal" in {
    val state = new MancalaGameState(Seq(1, 1, 0, 0), 0, 6, -1)

    val firstRepeat = state.move(1).move(0).move(0).move(1)
    firstRepeat.isTerminal should be (false)

    val secondRepeat = firstRepeat.move(1).move(0).move(0).move(1)
    secondRepeat.isTerminal should be (true)
  }

  it should "have a heuristic from splitting the remaining stones" in {
    val state = new MancalaGameState(Seq(1, 1, 0, 0), 2, 4, -1)

    val firstRepeat = state.move(1).move(0).move(0).move(1)
    firstRepeat.heuristic should be (.5)

    val secondRepeat = firstRepeat.move(1).move(0).move(0).move(1)
    secondRepeat.heuristic should be (1)
  }
}
