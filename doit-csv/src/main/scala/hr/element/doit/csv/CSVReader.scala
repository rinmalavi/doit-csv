package hr.element.doit.csv

import scala.collection.Traversable
import java.io.InputStream
import java.io.Reader
import java.io.InputStreamReader

trait StringHelpers{ self: LineReader =>
  val header =
  def apply(column: String) : String
  def get(column:String) :String
/*  Def Headers: IndexedSeq[String] ={
     Header.toIndexedSeq
  }
  */
  def toMap(): Map[String,String] =
}


trait CVSReaderLike extends Traversable[LineReaderLike]


trait LineReaderLike {
  val words: List[String]
}



class CSVReader(config: CSVFactory, iS: InputStream)
  extends CSVReaderLike with Traversable[LineReader] {

  val reader: Reader = new InputStreamReader(iS, config.encoding)


  def readln() = new LineReader(config, reader).words
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

class CSVReaderWithHeaders[](config: CSVFactory,iS: InputStream)
    extends CSVReader(config, iS) extends Traversable[T]
{

}