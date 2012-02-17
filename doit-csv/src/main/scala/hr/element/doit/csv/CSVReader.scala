package hr.element.doit.csv

import scala.collection.Traversable
import java.io.InputStream
import java.io.Reader
import java.io.InputStreamReader

object CSVReader {
  def apply(config: CSVFactory, iS: InputStream) =
    new CSVReader(config: CSVFactory, iS: InputStream)

  def apply(config: CSVFactory, iS: InputStream, otp: Headers) = {
    new CSVReaderWithHeaders(config: CSVFactory, iS: InputStream)
  }
}

abstract class CSVR[+T <:LineReader](config: CSVFactory, iS: InputStream) extends Traversable[T]{
  val reader: Reader = new InputStreamReader(iS, config.encoding)
  def readLn() = new LineReader(config, reader)
}


class CSVReader( config: CSVFactory, iS: InputStream)
  extends CSVR[LineReader](config, iS)
{


  def foreach[U](f: LineReader => U) = {
    next()
    def next() {
      val line = new LineReader(config, reader) //lr.next()
      if (line.nonEmpty) {
        f(line)
        next()
      }
    }
  }
}

class CSVReaderWithHeaders(config: CSVFactory, iS: InputStream)
    extends  CSVR[LineReaderWithHeader](config, iS) //CSVReader(config, iS) with
{
  val header = new LineReader(config, reader).words.zipWithIndex.toMap
  def foreach[U](f: LineReaderWithHeader => U) = {
    next()
    def next() {
      val line = new LineReaderWithHeader(config, reader, header) //lr.next()
      if (line.nonEmpty) {
        f(line)
        next()
      }
    }

  }

}

//trait StringHelpers{ self: LineReader =>
//  val header =
//  def apply(column: String) : String
//  def get(column:String) :String
///*  Def Headers: IndexedSeq[String] ={
//     Header.toIndexedSeq
//  }
//  */
//  def toMap(): Map[String,String] =
//}

//
//trait CVSReaderLike extends Traversable[LineReaderLike]
//
//
//trait LineReaderLike {
//  val words: List[String]
//}
//
//trait MyReader[T] extends Traversable[T]{
//
//  self: CSVReader =>
//  val header = new LineReader(config, reader)
//  def foreach[U](f: LineReader with Maper => U) = {
//    next()
//    def next() {
//      val line = new LineReader(config, reader) with Maper//lr.next()
//      if (line.nonEmpty) {
//        f(line)
//        next()
//      }
//    }
//
//  }
//}