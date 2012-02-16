package hr.element.doit.csv

import scala.collection.Traversable
import java.io.InputStream
import java.io.Reader
import java.io.InputStreamReader

class CSVReader(config: CSVFactory, iS: InputStream) extends Traversable[LineReader] {

  val reader: Reader = new InputStreamReader(iS, config.encoding)

  def foreach[U](f: LineReader => U) = {
    next()
    def next() {
      val lr = new LineReader(config, reader)
      if (lr.nonEmpty) {
        f(lr)
        next()
      }
    }
  }
}