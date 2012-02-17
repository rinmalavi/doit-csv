package hr.element.doit.csv
import java.nio.charset.Charset
import java.io.InputStream
import CSVFactory._
import java.io.OutputStream
import java.io.FileWriter
import java.io.OutputStreamWriter

object CSVFactory {

  def apply(): CSVFactory = {
    new CSVFactory("\"", ";", "\n", Charset.forName("UTF-8"))
  }
}

//sealed trait CSVFactory {
//  val quotes: String
//  val delimiter: String
//  val newLine: String
//  val encoding: Charset
//
//  def getReader(iS: InputStream): CSVReader
//  def getWriter(oS: OutputStream): CSVWriter
//}

class CSVFactory private(
    val quotes: String,
    val delimiter: String,
    val newLine: String,
    val encoding: Charset){
  //----------------------------------------------------------------
  def getReader(in: InputStream) = CSVReader(this, in);
  def getReader(in: InputStream, opt: Headers) = CSVReader(this, in, opt)
  def getWriter(out: OutputStream) =
      new CSVWriter(this, new OutputStreamWriter(out, encoding))
  //----------------------------------------------------------------
  def setDelimiter(delimiter: String) =
    new CSVFactory(quotes, delimiter, newLine,encoding)
  def setDelimiter(delimiter: Array[Char]) =
    new CSVFactory(quotes, new String(delimiter), newLine,encoding)

  def setQuote(quotes: String) =
    new CSVFactory(quotes, delimiter, newLine,encoding)
  def setQuote(quotes: Array[Char]) =
    new CSVFactory(new String(quotes), delimiter, newLine,encoding)
  def setQuote(quotes: Char) =
    new CSVFactory(Character.toString(quotes), delimiter, newLine,encoding)

  def setNewLine(newLine: String) =
    new CSVFactory(quotes, delimiter, newLine,encoding)
  def setNewLine(newLine: Array[Char]) =
    new CSVFactory(quotes, delimiter, new String(newLine), encoding)
  def setNewLine(newLine: Char) =
    new CSVFactory(quotes, delimiter, Character.toString(newLine), encoding)

  def setEncoding(encoding: Charset) =
    new CSVFactory(quotes, delimiter, newLine,encoding)
  def setEncoding(encoding: String) =
    new CSVFactory(quotes, delimiter, newLine, Charset.forName(encoding))

}

