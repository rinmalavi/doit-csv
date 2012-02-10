package hr.element.doit.csv

object SlidingMatcher {
  type MatcherResult = Either[Unit, Option[Char]]

  def apply(delimiter: Char): SlidingMatcher =
    apply(Array(delimiter))

  def apply(delimiter: String): SlidingMatcher =
    apply(delimiter.toCharArray)

  def apply(delimiter: Array[Char]): SlidingMatcher =
    delimiter.length match {
      case 1 =>
        new SingleCharacterMatcher(delimiter.head)
      case 2 =>
        new TwoCharacterMatcher(delimiter(0), delimiter(1))
      case x if x > 2 =>
        new CyclicCharacterMatcher(delimiter)
      case _ =>
        sys.error("Invalid delimiter length!")
    }
}

import SlidingMatcher._

trait SlidingMatcher {
  def consume(read: Char): Smr
  def flush(): Array[Char]
}

class SingleCharacterMatcher(delimiter: Char) extends SlidingMatcher {
  def consume(read: Char) =
    if (delimiter == read) {
      Delimiter
    } else {
      Ch3(read)
    }
  def flush(): Array[Char] =
    Array.empty

}

class TwoCharacterMatcher(head: Char, tail: Char) extends SlidingMatcher {
  var last: Option[Char] = None
  def consume(read: Char) =
    last match {
      case None =>
        last = Some(read)
        Cooldown

      case Some(ch) if (ch == head) && (read == tail) =>
        last = None
        Delimiter

      case _ =>
        val res = last
        last = Some(read)

        res.map(Ch3(_)).getOrElse(Cooldown)
    }

  def flush(): Array[Char] =
    last.toArray

}

class CyclicCharacterMatcher(delimiter: Array[Char]) extends SlidingMatcher {
  val last: Array[Char] = delimiter.clone()
  last(0) = last(0) + 1 toChar
  var cooldown = 0
  //val cyclic = delimiter.indices.map(i => rot(delimiter, i))
  val cyclic =
    for(i <- delimiter.indices) yield {
      delimiter.drop(i) ++ delimiter.take(i)
    }

  /* def rot(x: Array[Char], shift: Int): Array[Char] =
      last.slice(shift, delimiter.length) ++ last.slice(0, shift)*/

  def consume(read: Char) = {

    val readWritePoint = cooldown % delimiter.length
    cooldown += 1
    if (cooldown < delimiter.length) {
      last(readWritePoint) = read
      Cooldown
    } else {
      val res = last((readWritePoint + 1) % delimiter.length)
      last(readWritePoint) = read;
      if (last sameElements
        cyclic((delimiter.length - readWritePoint) % delimiter.length)) {
        cooldown = 0;
        Delimiter
      } else {
        Ch3(res)
      }
    }
  }

  def flush(): Array[Char] =
    if (cooldown < delimiter.length) {
      last.take(cooldown)
    } else {
      val readWritePoint = cooldown % delimiter.length
      cooldown = 0;
      last.takeRight(delimiter.length - readWritePoint) ++ last.take(readWritePoint)
    }

  def currMap: Array[Char] = cyclic(cooldown % delimiter.length)
}
