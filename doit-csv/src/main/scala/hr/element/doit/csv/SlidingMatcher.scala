package hr.element.doit.csv

object SlidingMatcher {

  def apply(config: CSVFactory): SlidingMatcher =
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
  AnewLine: Char)
  extends SlidingMatcher {
  def consume(read: Char, mode: SmrMode) = {
    val exp = Vector(Delimiter,Quote, NewLine, Ch3(read)).filter( mode(_) != Ignore)

    exp.find( _ match {
          case Delimiter if (Adelimiter == read)=> true
          case Quote if (Aquote == read)=> true
          case NewLine if (AnewLine == read)=> true
          case Ch3(x) =>  true
          case _=> false
          }) match {
      case Some(x) => x
      case None => NewLine
    }
}

  def flush(): Array[Char] =
    Array.empty
}

import scala.annotation.tailrec

class CyclicCharacterMatcher(
  QuoteA: Array[Char],
  DelimiterA: Array[Char],
  NewLineA: Array[Char])
  extends SlidingMatcher {

  var buffTake = 0

  class Revolver(ma: Smr) {
    val matchMsg = ma
    val refArray = matchMsg match {
      case Delimiter => DelimiterA
      case NewLine => NewLineA
      case Quote => QuoteA
    }
    val length = refArray.length
    //    val revolver =
    //      for(i <- refArray.indices)
    //        yield {
    //          refArray.drop(i) ++ refArray.take(i)
    //        }
    def isAt(readPoint: Int) = {
      if (buffTake < length) false
      else {
        val head = (readPoint + last.length - length + 1) % last.length
        val tail = (readPoint) % last.length
        // val revolverNo = if ( readPoint >= length) 0
        //       else (length -(last.length -readPoint)+2) % length          //(math.min((readPoint+ length) % last.length, length)+1) % length
        val sub =
          if (head > tail)
            last.drop(head) ++ last.take(tail + 1)
          else
            last.drop(head).take(length)
        //println("r:"+readPoint+"c"+buffTake+":  "+ new String(sub)  + " : " + new String(refArray)+ "   last:" +new String(last))
        sub sameElements refArray //revolver(0)
      }
    }
  }

  val rH = Array[Revolver](
    new Revolver(NewLine),
    new Revolver(Quote),
    new Revolver(Delimiter)) //sort ascending

  val last: Array[Char] =
    rH.maxBy(x => x.length).refArray.
      clone()

  var writePoint = 0
  def consume(read: Char, mode: (Smr => ModeCase)) = {
    val modedMatchers = rH.filter(x => mode(x.matchMsg) != Ignore)
    val bufflength = modedMatchers.maxBy(_.length).length
    last(writePoint) = read

    buffTake += 1
    val result = modedMatchers.find(_.isAt(writePoint))
    writePoint += 1
    writePoint %= last.length
    result match {
      case Some(x) =>
        buffTake -= x.length
        writePoint += last.length - x.length
        writePoint %= last.length
        x.matchMsg
      case None =>
        if (buffTake < bufflength) {
          Cooldown
        } else {
          val readPoint = (writePoint + last.length - bufflength) % last.length
          buffTake -= 1
          Ch3(last(readPoint))
        }

    }
  }

  def flush(): Array[Char] = {

    val tail = writePoint
    val head = (tail + last.length - buffTake) % last.length

    if (head > tail)
      last.drop(head) ++ last.take(tail)
    else
      last.drop(head).take(tail - head)
  }
}


