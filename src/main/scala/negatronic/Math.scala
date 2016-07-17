package negatronic

import scala.collection.mutable

/**
 * Created by darnold on 7/16/16.
 */
object Math {

  private val memoizedFactorials = mutable.Map.empty[Int, BigInt]

  def factorial(n: Int): BigInt = {
    if (n <= 1)
      BigInt(1)
    else if (memoizedFactorials.contains(n))
      memoizedFactorials(n)
    else {
      val result = BigInt(n) * factorial(n - 1)
      memoizedFactorials += (n -> result)
      result
    }
  }

  def binomialCoefficient(n: Int, k: Int): BigInt = {
    factorial(n) / (factorial(k) * factorial(n - k))
  }

  def binomialPMF(p: Double, n: Int, k: Int): Double = {
    binomialCoefficient(n, k).toDouble * scala.math.pow(p, k) * scala.math.pow(1 - p, n - k)
  }

  private val memoizedBinomialCDFs = mutable.Map.empty[(Double, Int, Int), Double]

  def binomialCDF(p: Double, n: Int, k: Int): Double = {
    if (k < 0)
      0
    else if (k > n)
      1
    else if (memoizedBinomialCDFs.contains(p, n, k))
      memoizedBinomialCDFs(p, n, k)
    else {
      val result = binomialPMF(p, n, k) + binomialCDF(p, n, k - 1)
      memoizedBinomialCDFs += ((p, n, k) -> result)
      result
    }
  }
}
