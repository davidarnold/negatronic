package negatronic

/**
 * Created by darnold on 7/9/16.
 */
class NegamaxAlgorithm {
  def walk(state: GameState, depth: Int, alpha: Double = -1.0, beta: Double = 1): Double = {
    if (depth == 0 || state.isTerminal)
      state.color * state.heuristic
    else
      state.enumerateMoves.foldLeft((-1.0, alpha)) { case ((bestValue, bestAlpha), child) =>
        val v = if (bestAlpha < beta) -walk(child, depth - 1, -beta, -bestAlpha) else -1.0
        (bestValue max v, bestAlpha max v)
      }._1
  }

  def rateMoves(state: GameState, depth: Int): Seq[(GameState, Double)] = {
    state.enumerateMoves.map(child => (child, walk(child, depth)))
  }
}
