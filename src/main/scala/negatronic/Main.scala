package negatronic

import scala.io.StdIn
import scala.util.{Failure, Success, Try}

/**
 * Created by darnold on 7/12/16.
 */
object Main extends App {
  val a = new NegamaxAlgorithm
  var state = new MancalaGameState(Seq(6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6), 0, 0, 1)

  println(state)

  while (!state.isTerminal) {
    if (state.color > 0) {
      if (state.legalPits.isEmpty) {
        state = state.flip
      } else {
        print("Enter your move: ")

        Try(StdIn.readInt()) match {
          case Success(input) =>
            // Shift from 1-based to 0-based indexing
            val move = input - 1

            if (state.legalPits.contains(move)) {
              state = state.move(move)

              println()
              println(state.flip)
            } else {
              println("Sorry, that is not a legal move")
            }
          case Failure(_) =>
            println("You must enter a number 1 - 6")
        }
      }
    } else {
      println("[thinking...]")

      // Scale depth from 10 - 15 as stones are removed
      val depth = (state.player1Score + state.player2Score)/16 + 10

      val tuple = a.rateMoves(state, depth).sortBy { case (_, value) => value }.head
      state = tuple._1.asInstanceOf[MancalaGameState]
      val score = tuple._2

      println()
      println(state + "     = " + score + " @ " + depth)
    }
  }
}
