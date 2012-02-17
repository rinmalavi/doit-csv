package hr.element.doit.csv

import scala.annotation.tailrec
import scala.collection.Traversable

import java.io.InputStream
import java.io.Reader
import java.io.InputStreamReader

abstract class CSVR[+T <:LineReader](config: CSVFactory, iS: InputStream) extends Traversable[T]{
  val reader: Reader = new InputStreamReader(iS, config.encoding)
  def readLn() = new LineReader(config, reader)
}

object CSVReader {
  def apply(config: CSVFactory, iS: InputStream) =
    new CSVReader(config: CSVFactory, iS: InputStream)

  def withHeaders(config: CSVFactory, iS: InputStream) = {
    new CSVReaderWithHeaders(config: CSVFactory, iS: InputStream)
  }
}

class CSVReader( config: CSVFactory, iS: InputStream)
  extends CSVR[LineReader](config, iS)
{
  def foreach[U](f: LineReader => U) = {
    @tailrec
    def next() {
      val line = new LineReader(config, reader)
      if (line.nonEmpty) {
        f(line)
        next()
      }
    }

    next()
  }
}

class CSVReaderWithHeaders(config: CSVFactory, iS: InputStream)
    extends  CSVR[LineReaderWithHeader](config, iS) {

  val header =
    new LineReader(config, reader).words
      .zipWithIndex.toMap

  def foreach[U](f: LineReaderWithHeader => U) = {
    @tailrec
    def next() {
      val line = new LineReaderWithHeader(config, reader, header)
      if (line.nonEmpty) {
        f(line)
        next()
      }
    }

    next()
  }
}
