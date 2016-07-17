package negatronic

/**
 * Created by darnold on 7/9/16.
 */
class NegamaxAlgorithm {
  def walk(state: GameState, depth: Int, alpha: Double = Double.NegativeInfinity, beta: Double = Double.PositiveInfinity): Double = {
    if (depth == 0 || state.isTerminal)
      state.color * state.heuristic
    else
      state.enumerateMoves.foldLeft((Double.NegativeInfinity, alpha)) { case ((bestValue, bestAlpha), child) =>
        val v = if (bestAlpha < beta) -walk(child, depth - 1, -beta, -bestAlpha) else Double.NegativeInfinity
        (bestValue max v, bestAlpha max v)
      }._1
  }

  def rateMoves(state: GameState, depth: Int): Seq[(GameState, Double)] = {
    state.enumerateMoves.map(child => (child, walk(child, depth)))
  }
}
