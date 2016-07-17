package negatronic

/**
 * Created by darnold on 7/9/16.
 */
class MancalaGameState(val pits: Seq[Int], val player1Score: Int, val player2Score: Int, val color: Double, val history: Map[MancalaGameState, Int] = Map.empty[MancalaGameState, Int]) extends GameState {
  require(color == 1.0 || color == -1.0, "color must be 1 or -1")

  private[this] val side = pits.length / 2

  private[this] val maxCount = side

  private[this] val stonesRemaining: Int = pits.sum

  private[this] val totalStones = stonesRemaining + player1Score + player2Score

  private[this] val isBoardEmpty: Boolean = stonesRemaining == 0

  private[this] val occurrences: Int = history.getOrElse(this, 0) + 1

  val isRepeat: Boolean = occurrences == 3

  override val isTerminal: Boolean = isBoardEmpty || isRepeat

  val legalPits: Seq[Int] = {
    pits.take(side).zipWithIndex.collect {
      case (stones, pit) if stones > 0 => pit
    }
  }

  override def heuristic: Double = {
    if (isRepeat) {
      if (player1Score > player2Score)
        color
      else if (player2Score > player1Score)
        -color
      else
        0
    } else {
      val pairsRemaining = stonesRemaining / 2
      val pairsToDraw = (totalStones / 2 - player1Score) / 2
      val pairsToLose = pairsToDraw - 1

      val probabilityOfNotWinning = Math.binomialCDF(.5, pairsRemaining, pairsToDraw)
      val probabilityOfWinning = 1.0 - probabilityOfNotWinning
      val probabilityOfLosing = Math.binomialCDF(.5, pairsRemaining, pairsToLose)

      color * (probabilityOfWinning - probabilityOfLosing)
    }
  }

  def flip: MancalaGameState = {
    new MancalaGameState(pits.indices map { index => pits((index + side) % pits.length) }, player2Score, player1Score, -color, history + (this -> occurrences))
  }

  def move(startingPit: Int): MancalaGameState = {
    assert(!isTerminal)

    val scratch = pits.toArray
    var stones = scratch(startingPit)
    scratch(startingPit) = 0

    assert(stones != 0)

    var pit = startingPit
    while (stones > 0) {
      pit = (pit + 1) % pits.length

      scratch(pit) += 1

      stones -= 1
    }

    var captured = 0
    while (scratch(pit) % 2 == 0 && 0 < scratch(pit) && scratch(pit) <= maxCount) {
      captured += scratch(pit)
      scratch(pit) = 0

      pit = (pit + pits.length - 1) % pits.length
    }

    new MancalaGameState(scratch, player1Score + captured, player2Score, color, history + (this -> occurrences)).flip
  }

  override def enumerateMoves: Seq[MancalaGameState] = {
    if (isTerminal) {
      Seq.empty[MancalaGameState]
    } else if (legalPits.isEmpty) {
      Seq(flip)
    } else {
      legalPits.map(pit => move(pit))
    }
  }

  override def toString = {
    player2Score.formatted("(%2d) ") +
      pits.drop(side).reverse.map(_.formatted("%2d")).mkString(" ") +
      player1Score.formatted(" (%2d)") +
      "\n     " +
      pits.take(side).map(_.formatted("%2d")).mkString(" ")
  }

  override def equals(other: Any): Boolean = other match {
    case that: MancalaGameState =>
      this.pits == that.pits &&
        this.player1Score == that.player1Score &&
        this.player2Score == that.player2Score &&
        this.color == that.color
    case _ => false
  }

  // FNV-1a hash algorithm
  override def hashCode: Int = {
    val init = 0x811c9dc5
    val prime = 0x01000193
    val elements = pits ++ Seq(player1Score, player2Score, color)

    elements.foldLeft(init)((hash, element) => (hash ^ element.hashCode()) * prime)
  }
}
