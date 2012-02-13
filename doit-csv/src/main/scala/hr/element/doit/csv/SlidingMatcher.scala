package hr.element.doit.csv



object SlidingMatcher {
  type MatcherResult = Either[Unit, Option[Char]]

 /* def apply(delimiter: Char): SlidingMatcher =
    apply(Array(delimiter))

  def apply(delimiter: String): SlidingMatcher =
    apply(delimiter.toCharArray)*/

  def apply(qoute: Array[Char], delimiter: Array[Char], newLine: Array[Char]): SlidingMatcher =
    delimiter.length match {
      case 1 =>
        new SingleCharacterMatcher(delimiter.head)
      case 2 =>
        new TwoCharacterMatcher(delimiter(0), delimiter(1))
      case x if x > 2 =>
        new CyclicCharacterMatcher(qoute, delimiter, newLine)
      case _ =>
        sys.error("Invalid delimiter length!")
    }
}

import SlidingMatcher._
trait SlidingMatcher {
  def consume(read: Char, qoutedMode: Boolean): Smr
  def flush(): Array[Char]
}

class SingleCharacterMatcher(delimiter: Char) extends SlidingMatcher {
  def consume(read: Char, qoutedMode: Boolean) =
    if (delimiter == read) {
      Delimiter
    } else {
      Ch3(read)
    }
  def flush(): Array[Char] =
    Array.empty

}

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

import scala.annotation.tailrec

class CyclicCharacterMatcher(
                      quote:      Array[Char],
                      delimiter:  Array[Char],
                      newLine:    Array[Char])
                      extends SlidingMatcher {

  var cooldown = 0
//
//  trait Revolver {
//    val matchMsg: Smr
//    def is: Boolean
//    val length : Int
//  }
  class Revolver(matchMsg: Smr) {
    val refArray = matchMsg match {
      case Delimiter  => delimiter
      case NewLine    => newLine
      case Quote      => quote
    }
    val length = refArray.length
    val revolver =
      for(i <- quote.indices)
        yield {
          refArray.drop(i) ++ refArray.take(i)
        }
    def isAt(readPoint: Int) = {
      last sameElements
         revolver((length - readPoint) % length)  //todo
      
    }
  }
  
//
//  class QuoteRevolver extends Revolver {
//    val qouteRevolver =
//      for(i <- quote.indices)
//        yield {
//          quote.drop(i) ++ quote.take(i)
//        }
//    val matchMsg = Quote
//    def is = false
//    val length = quote.length
//  }
//  class DelimiterRevolver extends Revolver {
//    val delimiterRevolver =
//      for(i <- delimiter.indices)
//        yield {
//          delimiter.drop(i) ++ delimiter.take(i)
//          }
//    val matchMsg = Delimiter
//    def is = true
//    val length = delimiter.length
//  }
//  class NewLineRevolver extends Revolver {
//    val newLineRevolver =
//      for(i <- delimiter.indices)
//        yield {
//          newLine.drop(i) ++ newLine.take(i)
//        }
//    val matchMsg = NewLine
//    def is = true
//    val length = newLine.length
//  }
  val nl = new Revolver(NewLine)
  val qr = new Revolver(Quote)
  val dr = new Revolver(Delimiter)
  val rH = Array[Revolver](nl, qr, dr)  //sort ascending

  val last: Array[Char] =
    rH.maxBy(x => x.length).refArray.
      clone()

  def consume(read: Char, quotedMode: Boolean)= {
    //  should return error if buffer is not empty after special cases
    cooldown += 1
    //@tailrec
    if (quotedMode) {           // quotes mode uses only first qoute.length of buffer
      val readWritePoint = (cooldown - 1) % quote.length
      if (cooldown < quote.length){
        last(readWritePoint) = read
        Cooldown
      } else {      //  quotes mode  cooldown > qoutes length
        if (qr.is) {  // qoutes
          cooldown -= quote.length
          Quote
        }
        else {    //    no quotes, no cooldown return char
          val ret= last((readWritePoint + 1) % quote.length)
          last(readWritepoint) = read
          Ch3(ret)
        }
      }                   //   end of quotesmode
    }
    else {
      val readWritePoint = (cooldown - 1) % quote.length
      last(readWritePoint) = read
      if (rH(0).is){
          rH(0).matchMsg
      }
      else {
        if (rH(1).is){
          rH(1).matchMsg
        }
        else {
          if(rH(2).is) {
            rH(2).matchMsg
          }
          else {
            if(last.length < cooldown) {
              Cooldown
            }
            else {
              Ch3(last((readWritePoint + 1) % quotes.length))
            }
          }
        }
      }
    }
  }
  def flush(): Array[Char] = {
    if (cooldown < delimiter.length) {
      last.take(cooldown)
    } else {
      val readWritePoint = cooldown % delimiter.length
      cooldown = 0;
      last.takeRight(delimiter.length - readWritePoint) ++ last.take(readWritePoint)
    }
  }
}




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