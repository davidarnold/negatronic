package negatronic

import org.scalatest.{FlatSpec, Matchers}

import scala.math.abs

/**
 * Created by darnold on 7/16/16.
 */
class MathSpec extends FlatSpec with Matchers {
  "Factorial" should "map 0 to 1" in {
    Math.factorial(0) should be (1)
  }

  it should "map 1 to 1" in {
    Math.factorial(1) should be (1)
  }

  it should "map 2 to 2" in {
    Math.factorial(2) should be (2)
  }

  it should "map 6 to 720" in {
    Math.factorial(6) should be (720)
  }

  it should "map 52 to the correct value" in {
    Math.factorial(52) should be (BigInt("80658175170943878571660636856403766975289505440883277824000000000000"))
  }

  "Binomial coefficient" should "map 1 choose 1 to 1" in {
    Math.binomialCoefficient(1, 1) should be (1)
  }

  it should "map 5 choose 3 to 10" in {
    Math.binomialCoefficient(5, 3) should be (10)
  }

  it should "map 72 choose 36 to the correct value" in {
    Math.binomialCoefficient(72, 36) should be (BigInt("442512540276836779204"))
  }

  "Binomial probability mass function" should "map pmf(.5, 5, 3) to .3125" in {
    Math.binomialPMF(.5, 5, 3) should be (.3125)
  }

  it should "map pmf(.1, 6, 5) to approximately 0.000054" in {
    val actual = Math.binomialPMF(.1, 6, 5)
    val expected = 0.000054
    val eps = 1E-7

    abs(actual - expected) should be < eps
  }

  "Binomial cumulatve distribution function" should "map cdf(.3, 5, 2) to approximately 0.837" in {
    val actual = Math.binomialCDF(.3, 5, 2)
    val expected = 0.837
    val eps = 1E-3

    abs(actual - expected) should be < eps
  }

  it should "map cdf(.9, 7, 5) to approximately 0.150" in {
    val actual = Math.binomialCDF(.9, 7, 5)
    val expected = 0.150
    val eps = 1E-3

    abs(actual - expected) should be < eps
  }

  it should "map any k less than 0 to 0" in {
    Math.binomialCDF(.7, 8, -1) should be (0)
    Math.binomialCDF(.7, 8, -10) should be (0)
  }

  it should "map any k greater than n to 1" in {
    Math.binomialCDF(.4, 6, 7) should be (1)
    Math.binomialCDF(.4, 6, 17) should be (1)
  }
}
