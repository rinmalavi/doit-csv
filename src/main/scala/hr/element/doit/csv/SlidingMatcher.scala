package hr.element.doit.csv

object SlidingMatcher {

  def apply(config: CSVConfig): SlidingMatcher =
    Seq(config.quotes, config.newLine, config.delimiter).maxBy(_.length).length match {
      case 1 =>
        new SingleCharacterMatcher(
          config.quotes(0),
          config.delimiter(0),
          config.newLine(0))

        // case 2 =>        new TwoCharacterMatcher(delimiter(0), delimiter(1))

        case x if x > 1 =>
          new CyclicCharacterMatcher(
            config.quotes.toCharArray,
            config.delimiter.toCharArray,
            config.newLine.toCharArray)
        case _ =>
          sys.error("Invalid delimiter length!")
      }
}

import SlidingMatcher._
import LineReader._

trait SlidingMatcher {
  def consume(read: Char, mode: SmrMode): Smr
  def flush(): Array[Char]
}

class SingleCharacterMatcher(
    Aquote: Char,
    Adelimiter: Char,
    AnewLine: Char) extends SlidingMatcher {

  def consume(read: Char, mode: SmrMode) = {
    val exp = Vector(Delimiter, Quote, NewLine, ReadCh(read)).filter(mode(_) != Ignore)

    exp.find(_ match {
      case Delimiter if (Adelimiter == read) => true
      case Quote if (Aquote == read) => true
      case NewLine if (AnewLine == read) => true
      case ReadCh(x) => true
      case _ => false
    }).getOrElse(NewLine)
  }

  def flush(): Array[Char] =
    Array.empty
}

import scala.annotation.tailrec

class CyclicCharacterMatcher(
    QuoteA: Array[Char],
    DelimiterA: Array[Char],
    NewLineA: Array[Char]) extends SlidingMatcher {

  var buffTake = 0

  class Revolver(ma: Smr) {
    val matchMsg = ma
    val refArray = matchMsg match {
      case Delimiter => DelimiterA
      case NewLine => NewLineA
      case Quote => QuoteA
      case _ => sys.error("Should not happen!")
    }

    val length = refArray.length
    val revolver =
      for (i <- refArray.indices) yield {
        refArray.drop(length - i) ++ refArray.take(length - i)
      }

    def isAt(readPoint: Int) = {
      if (buffTake < length) false
      else {
        val head = (readPoint + buffer.length - length + 1) % buffer.length
        val tail = (readPoint) % buffer.length + 1
        val revNo = if (head < tail) 0 else length - (buffer.length - head)

        if (length == buffer.length)
          revolver(revNo) sameElements buffer
        else if (head > tail) // + 1
          (buffer.take(tail) ++ buffer.drop(head)) sameElements
            revolver(revNo)
        else
          buffer.take(tail).drop(head) sameElements revolver(revNo)
      }
    }
  }

  val rH = Array[Revolver](
    new Revolver(NewLine),
    new Revolver(Quote),
    new Revolver(Delimiter)) //sort ascending

  val buffer: Array[Char] =
    rH.maxBy(x => x.length).refArray.
      clone()

  var writePoint = 0
  def consume(read: Char, mode: (Smr => ModeCase)) = {
    val modedMatchers = rH.filter(x => mode(x.matchMsg) != Ignore)
    val windowLen = modedMatchers.maxBy(_.length).length
    buffer(writePoint) = read

    buffTake += 1
    val result = modedMatchers.find(_.isAt(writePoint))
    writePoint += 1
    writePoint %= buffer.length
    result match {
      case Some(x) =>
        buffTake -= x.length
        writePoint += buffer.length - x.length
        writePoint %= buffer.length
        x.matchMsg
      case None =>
        if (buffTake < windowLen) {
          Cooldown
        } else {
          val readPoint = (writePoint + buffer.length - windowLen) % buffer.length
          buffTake -= 1
          ReadCh(buffer(readPoint))
        }
    }
  }

  def flush(): Array[Char] = {

    val tail = writePoint
    val head = (tail + buffer.length - buffTake) % buffer.length
    if (head > tail)
      buffer.drop(head) ++ buffer.take(tail)
    else
      buffer.drop(head).take(tail - head)
  }
}
