package hr.element.doit.csv

import java.io.Reader
import scala.annotation.tailrec
import scala.collection.mutable.ArrayBuffer

class LineReader(config: CSVFactory, reader: Reader) extends Traversable[String] {

  val quote = config.quotes
  val delimiter = config.delimiter
  val newLine = config.newLine
  val quoteM = SlidingMatcher(quote)
  val delimiterM = SlidingMatcher(delimiter)
  val newLineM = SlidingMatcher(newLine)
  val words: List[String] = {

    def flushAll(): Unit = {
      quoteM.flush()
      delimiterM.flush()
      newLineM.flush()
    }

    val res = new ArrayBuffer[String]

    @tailrec
    def loop(
      qoutedMode: Boolean = false,
      qoutedValue: Boolean = false,
      curr: StringBuilder = new StringBuilder()) {

      val read = reader.read()

      val reedChar = read.toChar
      println("lr>"+reedChar+" "+curr)
      if (read == -1) {
        if(qoutedMode)
          sys.error("Malformated CSV")
        if (curr.nonEmpty)
          res += (curr ++= quoteM.flush()).result
        else
          res
      } else {
        val qouteConsumeRes = quoteM.consume(reedChar)
        if (qoutedMode) {

          qouteConsumeRes match {

            case Delimiter =>
              flushAll()
              loop(false, true, curr)

            case Ch3(x) =>
              loop(true, true, curr += x)

            case Cooldown =>
              loop(true, true, curr)

            case _ =>
              sys.error("Could not happen 0!")
          }
        } else { // !qoutedMode
          qouteConsumeRes match {
            case Delimiter =>
              flushAll()
              if (qoutedValue)
                loop(true, true, curr.append(quote))
              else
                if (curr.isEmpty)
                loop(true, true)

              else
                sys.error("Malformated CSV")
            case Ch3(_) | Cooldown =>
              newLineM.consume(reedChar) match {
                case Delimiter =>
                  //
                  res += curr.result
                case Ch3(_) | Cooldown =>
                  delimiterM.consume(reedChar) match {
                    case Delimiter =>

                      flushAll()
                      res += curr.result
                      loop(false, false) // new Value

                    case Ch3(x) =>
                      if (qoutedValue)
                        sys.error("Malformed CSV")
                      else
                        loop(false, qoutedValue, curr += x)

                    case Cooldown =>
                      loop(false, qoutedValue, curr)
                    case _ =>
                      sys.error("Could not happen 1!")
                  } // end delimiter match
                case _ =>
                  sys.error("Could not happen 2!")
              } // end newLine match
            case _ =>
              sys.error("Could not happen 3!")
          } //end quote match
        }
      }
    }

    loop()
    //for (s <- res) println("lr > " + s)
    res.toList
  }

  def foreach[U](f: String => U) = words.foreach(f) //util.Random.shuffle(words).foreach(f)
}
