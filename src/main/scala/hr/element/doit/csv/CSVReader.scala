package hr.element.doit.csv

import scala.annotation.tailrec
import scala.collection.Traversable

import java.io._

abstract class CSVReaderLike[+T <: LineReader](config: CSVConfig, reader: Reader) extends Traversable[T]{
  def readLn() = new LineReader(config, reader)
}

class CSVReader(config: CSVConfig, reader: Reader)
    extends CSVReaderLike[LineReader](config, reader) {

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

class CSVReaderWithHeaders(config: CSVConfig, reader: Reader)
    extends CSVReaderLike[LineReaderWithHeader](config, reader) {

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
