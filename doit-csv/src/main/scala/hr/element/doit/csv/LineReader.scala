package hr.element.doit.csv

import java.io.Reader
import scala.annotation.tailrec
import scala.collection.mutable.ArrayBuffer

object LineReader {
  type SmrMode = Smr => ModeCase
  type Columns = Map[String, Int]
}

import LineReader._

class LineReaderWithHeader(config: CSVFactory, reader: Reader, header: Columns)
  extends LineReader(config, reader) {

  def get(col: String): Option[String] =
    header.get(col).map(words)

  def get(i: Int): Option[String] = Some(i).map(words)
  def getMap(): Map[String, String] = header.mapValues(words(_))

}

class LineReader(config: CSVFactory, reader: Reader) extends Traversable[String] {

  def foreach[U](f: String => U) = words.foreach(f)

  val words = {

    val res = new ArrayBuffer[String]

    val sliM = SlidingMatcher(config)

    val StartMode: SmrMode = (_: Smr) => Watch

    val VerboseMode: SmrMode = {
      _ match {
        case Delimiter => Watch
        case Quote => Unexpected
        case NewLine => Watch
        case Ch3(x) => Watch
        case _ => Unexpected
      }
    }
    val EscapeMode: SmrMode = {
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
        case Cooldown => Ignore
        case _ => Unexpected
      }
    }
    val EndMode: SmrMode = (_: Smr) => Unexpected
    val StupidMode: SmrMode = (_: Smr) => Unexpected
    def conv(curMode: SmrMode, matcher: Smr): SmrMode = {
      (curMode, matcher) match {
        case (StartMode, Delimiter) => StartMode
        case (StartMode, Quote) => QuotedMode
        case (StartMode, Ch3(_)) => VerboseMode
        case (StartMode, Cooldown) => StartMode
        case (EscapeMode, Delimiter) => StartMode
        case (VerboseMode, Delimiter | NewLine) => StartMode
        case (VerboseMode, Ch3(x)) => VerboseMode
        case (QuotedMode, Quote) => EscapeMode
        case (QuotedMode, Cooldown) => QuotedMode
        case (QuotedMode, Ch3(x)) => QuotedMode
        case (EscapeMode, Quote) => QuotedMode
        case (EscapeMode, NewLine) => EndMode
        case (EscapeMode, Cooldown) => EscapeMode
        case (EscapeMode, x) => EscapeMode
        case (EndMode, _) => StupidMode
        case _ => EndMode
      }
    }

    def stringMode(mode: SmrMode) = {
      (mode) match {
        case (StartMode) => "Start mode"
        case (EscapeMode) => "Escape mode "
        case (VerboseMode) => "Verbrose mode"
        case (QuotedMode) => "Quote mode"
        case (EndMode) => "End Mode"
        case _ => "Stupid mode"
      }

    }
    def loop(
      mode: SmrMode,
      curr: StringBuilder = new StringBuilder()) {
      val read = reader.read()

      if (read == -1) { // End Of File
        if (mode == QuotedMode) sys.error("Malformated CSV, unexpected eof!")
        else if (curr.nonEmpty)
          res += (curr appendAll sliM.flush()).result()
        else
          res
      } else {
        val returnResult = sliM.consume(read.toChar, mode)
        if (mode(returnResult) == Unexpected)
          sys.error("Malformated CSV! " + stringMode(mode) + " with " + returnResult.getClass())
        else {
          returnResult match {
            case Delimiter =>
              curr.appendAll(sliM.flush())

              res += curr.result()
              loop(conv(mode, Delimiter))

            case Quote =>
              if (EscapeMode == mode)
                loop(
                  conv(mode, Quote),
                  curr appendAll sliM.flush() append config.quotes)
              else loop(conv(mode, Quote), curr appendAll sliM.flush())

            case NewLine =>
              curr appendAll sliM.flush()
              res += curr.result()

            case Ch3(x) =>
              loop(conv(mode, returnResult), curr append x)
            case Cooldown => loop(conv(mode, returnResult), curr)
          }
        }
      }

    }
    loop(StartMode)
    res.toIndexedSeq
  }
}
