package hr.element.doit.csv



object SlidingMatcher {
//  type MatcherResult = Either[Unit, Option[Char]]

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
        val head = (readPoint + last.length - length + 1) % last.length  //(readPoint + (buffLength - length)) % buffLength
        val tail = (readPoint) %  last.length
        val sub =
          if ( head > tail )
            last.drop(head-1) ++ last.take(tail)
            else
                last.drop(head).take(length)
         println("r:"+readPoint+"c"+buffTake+":"+ new String(sub) + ":" + new String(revolver((length + readPoint) % length)))
      sub sameElements
         revolver((length + readPoint) % length)  //todo
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
      // ()
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
    println("f:" + buffTake+ " dif:"+ (tail-head))
    buffTake = 0
    writePoint = 0

          if ( head > tail )
            last.drop(head-1) ++ last.take(tail)
            else
                last.drop(head).take(tail - head)

//
//    if (buffTake < last.length) {
//
//      val readPoint = buffTake
//      buffTake = 0
//      last.take(readPoint)
//    } else {
//      println("f:"+buffTake)
//      val readWritePoint = buffTake % last.length
//      buffTake = 0;
//      last.takeRight(last.length - readWritePoint) ++ last.take(readWritePoint)
//    }
  }
}


//    //@tailrec
//    if (quotedMode) {           // quotes mode uses only first qoute.length of buffer
//      val readWritePoint = (cooldown - 1) % quote.length
//      if (cooldown < quote.length){
//        last(readWritePoint) = read
//        Cooldown
//      } else {      //  quotes mode  cooldown > qoutes length
//        if (qr.is) {  // qoutes
//          cooldown -= quote.length
//          Quote
//        }
//        else {    //    no quotes, no cooldown return char
//          val ret= last((readWritePoint + 1) % quote.length)
//          last(readWritepoint) = read
//          Ch3(ret)
//        }
//      }                   //   end of quotesmode
//    }
//    else {
//      val readWritePoint = (cooldown - 1) % quote.length
//      last(readWritePoint) = read
//      if (rH(0).is){
//          rH(0).matchMsg
//      }
//      else {
//        if (rH(1).is){
//          rH(1).matchMsg
//        }
//        else {
//          if(rH(2).is) {
//            rH(2).matchMsg
//          }
//          else {
//            if(last.length < cooldown) {
//              Cooldown
//            }
//            else {
//              Ch3(last((readWritePoint + 1) % quotes.length))
//            }
//          }
//        }
//      }
//    }
//  }

/*class TwoCharacterMatcher(head: Char, tail: Char) extends SlidingMatcher {
  var last: Option[Char] = None
  def consume(read: Char, qoutedMode: Boolean) =
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

}*/
//    if (cooldown < delimiter.length) {
//      last(readWritePoint) = read
//      Cooldown
//    } else {
//      val res = last((readWritePoint + 1) % delimiter.length)
//      last(readWritePoint) = read;
//      if (last sameElements
//        cyclic((delimiter.length - readWritePoint) % delimiter.length)) {
//        cooldown = 0;
//        Delimiter
//      } else {
//        Ch3(res)
//      }
//    }