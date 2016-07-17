package negatronic

/**
 * Created by darnold on 7/17/16.
 */
trait GameState {

  val color: Double

  val isTerminal: Boolean

  def heuristic: Double

  def enumerateMoves: Seq[GameState]
}
