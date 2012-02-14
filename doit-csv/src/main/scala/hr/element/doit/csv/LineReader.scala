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

  val StartMode: SmrMode = (_: Smr) => Watch


  val VerboseMode: SmrMode ={
    _ match {
      case Delimiter => Watch
      case Quote => Unexpected
      case NewLine => Watch
      case Ch3(x) => Watch
      case _ => Unexpected
    }
  }
  val EscapeMode: SmrMode ={
      _ match {
        case Delimiter => Watch
        case Quote => Watch
        case NewLine => Watch
        case Cooldown => Watch
        case Ch3(x) => Unexpected
        case _ => Unexpected
      }
  }
  val QuotedMode: SmrMode = {
    _ match {
      case Delimiter => Ignore
      case Quote => Watch
      case NewLine => Ignore
      case Ch3(x) => Watch
      case _ => Unexpected
    }
  }
  val EndMode: SmrMode = (_: Smr) => Unexpected
  val StupidMode: SmrMode = (_: Smr) => Unexpected

  def conv(curMode: SmrMode, matcher: Smr): SmrMode = {
    (curMode, matcher) match {
      case (StartMode, Delimiter)   => StartMode
      case (StartMode, Quote)       => QuotedMode
      case (StartMode, Ch3(_))      => VerboseMode
      case (StartMode, Cooldown)    => StartMode
      case (EscapeMode, Delimiter)  => StartMode
      case (VerboseMode, Delimiter) => StartMode
      case (VerboseMode, _        ) => StartMode
      case (QuotedMode, Quote)      => EscapeMode
      case (QuotedMode, Cooldown)   => QuotedMode
      case (QuotedMode, Ch3(x))     => QuotedMode
      case (EscapeMode, Quote)      => QuotedMode
      case (EscapeMode, Delimiter)  => StartMode
      case (EscapeMode, NewLine  )  => EndMode
      case (EscapeMode, Cooldown  )  => EscapeMode
      case (EscapeMode, x  )        => EscapeMode
      case (EndMode,_)  => StupidMode
      case _                        => EndMode
    }
  }

//  def checkvalid(mode: SmrModet, smr: Smr){
//    (mode, smr) match {
//      case (StartMode, x) => startMode(x)
//      case (VerboseMode, x) => verboseMode(x)
//      case (QuotedMode,x)   => quotedMode(x)
//    }
//  }
  val words: List[String] = {

    val res = new ArrayBuffer[String]

    def stringMode(mode: SmrMode) ={
    (mode) match {
      case (StartMode)   => "Start mode"
      case (EscapeMode)  => "Escape mode "
      case (VerboseMode) => "Verbrose mode"
      case (QuotedMode)  => "Quote mode"
      case (EndMode)     => "End Mode"
      case _             => "Stupid mode"
    }

    }
    def loop(
        mode: SmrMode,
        curr: StringBuilder = new StringBuilder()) {
      val read = reader.read()

      //println
      //println( "loop: " + curr + " " + stringMode(mode)+" read :"+read.toChar)
      //res.foreach(x => println("res> "+ x ))

      if (read == -1){                                  // End Of File
        if (mode == QuotedMode) sys.error("Malformated CSV, unexpected eof!")
        else
          if (curr.nonEmpty)
              res += (curr appendAll sliM.flush()).result
            else
              res
        } else {

          val result = sliM.consume(read.toChar, mode)
          //println(result.getClass())
          if (mode(result) == Unexpected ) sys.error("Malformated CSV! "+ stringMode(mode)+" with " + result.getClass())
          else {
            result match {
              case Delimiter    =>
                        curr.appendAll(sliM.flush())
                        //println("delblibliteretarterd: " + curr)
                        res += curr.result
                        loop(conv(mode, Delimiter))

              case Quote        =>
                if (EscapeMode == mode)
                  loop(
                      conv(mode, Quote),
                      curr append sliM.flush() append config.quotes)
                else loop(conv(mode, Quote), curr append sliM.flush() )

              case NewLine      =>
                curr.appendAll(sliM.flush())
                res += curr.result()

              case Ch3(x)       =>
                loop(conv(mode, result),  curr.append(x))
              case Cooldown     => loop(conv(mode, result),  curr)
              }
            }
          }
      }
    loop(StartMode)
    res.toList
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
