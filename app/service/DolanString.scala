package service

import scala.util.Random
import scala.util.matching.Regex

class DolanString(val wrappedString: String, val replaced: Int = 0) {
  def swapRandomCharacters(probability: Double = 0.2): DolanString = {
    if (replaced > 0)
      return this

    if (probability < Math.random())
      return this

    if (wrappedString.length < 4)
      return this

    val pos = 1 + new Random().nextInt(wrappedString.length - 3)
    new DolanString(wrappedString.substring(0, pos) + wrappedString(pos + 1) + wrappedString(pos) + wrappedString.substring(pos + 2), replaced + 1)
  }

  def replaceWithProbability(prefixParam: String, from: String, to: String, probability: Double = 0.3): DolanString = {
    val prefix = if (prefixParam == "") "." else prefixParam
    if (replaced > 1)
      return this

    if (probability < Math.random())
      return this

    val regex: Regex = s"($prefix)($from)".r("prefix", "from")
    if (regex.findAllIn(wrappedString).size == 0)
      return this

    new DolanString(regex.replaceAllIn(wrappedString, _.group("prefix") + to), replaced + 1)
  }

  override def toString: String = wrappedString
}
