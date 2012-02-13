package hr.element.doit.csv

import java.io.Reader
import scala.annotation.tailrec
import scala.collection.mutable.ArrayBuffer

object LineReader {
  type SmrMode = Smr => ModeCase
}

import LineReader._

class LineReader(config: CSVFactory, reader: Reader) extends Traversable[String] {

//  val quote = config.quotes
//  val delimiter = config.delimiter
//  val newLine = config.newLine
//  val quoteM = SlidingMatcher(quote)
//  val delimiterM = SlidingMatcher(delimiter)
//  val newLineM = SlidingMatcher(newLine)



  val sliM = SlidingMatcher(config)

  val startMode: SmrMode = (_: Smr) => Watch


  val vebroseMode: SmrMode ={
    _ match {
      case Delimiter => Watch
      case Quote => Unexpected
      case NewLine => Watch
      case Ch3(x) => Watch
    }
  }
  val escapeMode: SmrMode ={
      _ match {
        case Delimiter => Watch
        case Quote => Watch
        case NewLine => Watch
        case Ch3(x) => Unexpected
        case _ => Unexpected
      }
  }
  val quotedMode: SmrMode = {
    _ match {
      case Delimiter => Ignore
      case Quote => Watch
      case NewLine => Ignore
      case Ch3(x) => Watch
    }
  }
  val EndMode: SmrMode = (_: Smr) => Unexpected

  def conv(curMode: SmrMode, matcher: Smr): SmrMode = {
    (curMode, matcher) match {
      case (startMode, Delimiter)   => startMode
      case (escapeMode, Delimiter)  => startMode
      case (vebroseMode, Delimiter) => startMode
      case (quotedMode, Quote)      => escapeMode
      case (escapeMode, Quote)      => quotedMode
    }
  }


  val words: List[String] = {

//    def flushAll(): Unit = {
//      quoteM.flush()
//      delimiterM.flush()
//      newLineM.flush()
//    }

    val res = new ArrayBuffer[String]

    def loop(
        mode: SmrMode,
        curr: StringBuilder = new StringBuilder()) {
      val read = reader.read()
      if (read == -1){                                  // End Of File
        if (mode == quotedMode) sys.error("Malformated CSV, unexpected eof!")
        else
          if (curr.nonEmpty)
              res += (curr ++= sliM.flush()).result
            else
              res
        } else {
          val result = sliM.consume(read.toChar, mode)
          if (mode(result) == Unexpected ) sys.error("Malformated CSV!")
          else {
            result match {
              case Delimiter  =>
                        curr.append(sliM.flush())
                        res += curr.result
                        loop(conv(mode, Delimiter))

              case Quote      =>
                if (quotedMode == mode) loop(conv(mode, Quote), curr)
                else loop(conv(mode, Quote), curr append config.quotes)

              case NewLine    =>
                curr.append(sliM.flush())
                res += curr.result()

              case Ch3(x)      =>
                loop(conv(mode, result),  curr.append(x))
            }
          }

      }

      }

    }



  def foreach[U](f: String => U) = words.foreach(f) //util.Random.shuffle(words).foreach(f)

//    @tailrec
//    def loop(
//      qoutedMode: Boolean = false,
//      qoutedValue: Boolean = false,
//      curr: StringBuilder = new StringBuilder()) {
//
//      val read = reader.read()
//
//      val reedChar = read.toChar
//      println("lr>"+reedChar+" "+curr)
//      if (read == -1) {
//        if(qoutedMode)
//          sys.error("Malformated CSV")
//        if (curr.nonEmpty)
//          res += (curr ++= quoteM.flush()).result
//        else
//          res
//      } else {
//        val qouteConsumeRes = quoteM.consume(reedChar)
//        if (qoutedMode) {
//
//          qouteConsumeRes match {
//
//            case Delimiter =>
//              flushAll()
//              loop(false, true, curr)
//
//            case Ch3(x) =>
//              loop(true, true, curr += x)
//
//            case Cooldown =>
//              loop(true, true, curr)
//
//            case _ =>
//              sys.error("Could not happen 0!")
//          }
//        } else { // !qoutedMode
//          qouteConsumeRes match {
//            case Delimiter =>
//              flushAll()
//              if (qoutedValue)
//                loop(true, true, curr.append(quote))
//              else
//                if (curr.isEmpty)
//                loop(true, true)
//
//              else
//                sys.error("Malformated CSV")
//            case Ch3(_) | Cooldown =>
//              newLineM.consume(reedChar) match {
//                case Delimiter =>
//                  //
//                  res += curr.result
//                case Ch3(_) | Cooldown =>
//                  delimiterM.consume(reedChar) match {
//                    case Delimiter =>
//
//                      flushAll()
//                      res += curr.result
//                      loop(false, false) // new Value
//
//                    case Ch3(x) =>
//                      if (qoutedValue)
//                        sys.error("Malformed CSV")
//                      else
//                        loop(false, qoutedValue, curr += x)
//
//                    case Cooldown =>
//                      loop(false, qoutedValue, curr)
//                    case _ =>
//                      sys.error("Could not happen 1!")
//                  } // end delimiter match
//                case _ =>
//                  sys.error("Could not happen 2!")
//              } // end newLine match
//            case _ =>
//              sys.error("Could not happen 3!")
//          } //end quote match
//        }
//      }
//    }
//
//    loop()
//    //for (s <- res) println("lr > " + s)
//    res.toList
//  }

}
