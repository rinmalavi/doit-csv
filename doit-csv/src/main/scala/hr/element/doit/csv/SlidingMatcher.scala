package hr.element.doit.csv



object SlidingMatcher {

 /* def apply(delimiter: Char): SlidingMatcher =
    apply(Array(delimiter))

  def apply(delimiter: String): SlidingMatcher =
    apply(delimiter.toCharArray)*/

//  def apply(qoute: Array[Char], delimiter: Array[Char], newLine: Array[Char]): SlidingMatcher =
//    Array(config.quote, config.newLine, config.deimiter) match {
//      case 1 =>
//        new SingleCharacterMatcher(delimiter.head)
//     // case 2 =>        new TwoCharacterMatcher(delimiter(0), delimiter(1))
//      case x if x > 2 =>
//        new CyclicCharacterMatcher(qoute, delimiter, newLine)
//      case _ =>
//        sys.error("Invalid delimiter length!")
//    }
    def apply(config: CSVFactory): SlidingMatcher =
    Array(config.quotes, config.newLine, config.delimiter).maxBy(_.length).length
        match {
      case 1 =>
        new SingleCharacterMatcher(
            config.quotes.toCharArray,
            config.delimiter.toCharArray,
            config.newLine.toCharArray)
     // case 2 =>        new TwoCharacterMatcher(delimiter(0), delimiter(1))
      case x if x > 2 =>
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
                      Aquote:      Array[Char],
                      Adelimiter:  Array[Char],
                      AnewLine:    Array[Char])
                      extends SlidingMatcher {
  def consume(read: Char, mode: SmrMode) =
    Array(read) match {
      case Aquote     => Quote
      case Adelimiter => Delimiter
      case AnewLine   => NewLine
      case x => Ch3(x.head)
}
  def flush(): Array[Char] =
    Array.empty
}

import scala.annotation.tailrec

class CyclicCharacterMatcher(
                      QuoteA:      Array[Char],
                      DelimiterA:  Array[Char],
                      NewLineA:    Array[Char])
                      extends SlidingMatcher {

  var buffTake = 0

  class Revolver(ma: Smr) {
    val matchMsg = ma
    val refArray = matchMsg match {
      case Delimiter  => DelimiterA
      case NewLine    => NewLineA
      case Quote      => QuoteA
    }
    val length = refArray.length
    val revolver =
      for(i <- refArray.indices)
        yield {
          refArray.drop(i) ++ refArray.take(i)
        }
    def isAt(readPoint: Int) = {
      if ( buffTake < length) false
      else{
        val head = (readPoint + last.length - length + 1) % last.length
        val tail = (readPoint) %  last.length
       // val revolverNo = if ( readPoint >= length) 0
        //       else (length -(last.length -readPoint)+2) % length          //(math.min((readPoint+ length) % last.length, length)+1) % length
        val sub =
          if ( head > tail )
            last.drop(head)++last.take(tail+1)
            else
                last.drop(head).take(length)
         //println("r:"+readPoint+"c"+buffTake+":"+ new String(sub) + ":" + new String(revolver(0)))
      sub sameElements    revolver(0)
      }
    }
  }

  val rH = Array[Revolver](
      new Revolver(NewLine),
      new Revolver(Quote),
      new Revolver(Delimiter))  //sort ascending

  val last: Array[Char] =
    rH.maxBy(x => x.length).refArray.
      clone()

   var writePoint = 0
  def consume(read: Char, mode: ( Smr => ModeCase ) )= {    //  should return error if buffer is not empty after special cases
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
          x.matchMsg
      case None =>
        if (buffTake < bufflength) {

            Cooldown
      }
      else {
        val readPoint = (writePoint + last.length - bufflength) % last.length
        buffTake -=1
        Ch3(last(readPoint))}


    }
  }

  def flush(): Array[Char] = {

    val tail = (writePoint - 1+ last.length ) % last.length
    val head = (tail - buffTake + last.length ) % last.length
    //println("f:" + buffTake+ " dif:"+ (tail - head))
    buffTake = 0
    writePoint = 0

          if ( head > tail )
            last.drop(head - 1).take(tail)
            else
                last.drop(head).take(tail - head)


  }
}


