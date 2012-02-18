package hr.element.doit.csv {

import java.io._
import java.nio.charset.Charset

class CSVFactory protected(
    val delimiter: String,
    val newLine: String,
    val quotes: String) {

  def getReader(reader: Reader) =
    reader

  def getWriter(writer: Writer) =
    writer
}

class CSVFactoryWithEncoding protected(
    delimiter: String,
    encoding: Charset,
    newLine: String,
    quotes: String) extends CSVFactory(delimiter, newLine, quotes) {

  def getReader(iS: InputStream): Reader =
    getReader(new InputStreamReader(iS, encoding))

  def getWriter(oS: OutputStream): Writer =
    getWriter(new OutputStreamWriter(oS, encoding))
}

object CSVFactory {
  val default = new CSVFactoryWithEncoding(";", Charset.forName("UTF8"), "\\n", "\"")

  def setDelimiter(delimiter: CharSequence) = new WithDelimiter(delimiter.toString)
  def setEncoding(encoding: Charset) = new WithEncoding(encoding)
  def setNewLine(newLine: CharSequence) = new WithNewLine(newLine.toString)
  def setQuotes(quotes: CharSequence) = new WithQuotes(quotes.toString)

  protected class WithDelimiter(val delimiter: String) {
    def setEncoding(encoding: Charset) = new WithDelimiterAndEncoding(delimiter, encoding)
    def setNewLine(newLine: CharSequence) = new WithDelimiterAndNewLine(delimiter, newLine.toString)
    def setQuotes(quotes: CharSequence) = new WithDelimiterAndQuotes(delimiter, quotes.toString)
  }

  protected class WithEncoding(val encoding: Charset) {
    def setDelimiter(delimiter: CharSequence) = new WithDelimiterAndEncoding(delimiter.toString, encoding)
    def setNewLine(newLine: CharSequence) = new WithEncodingAndNewLine(encoding, newLine.toString)
    def setQuotes(quotes: CharSequence) = new WithEncodingAndQuotes(encoding, quotes.toString)
  }

  protected class WithNewLine(val newLine: String) {
    def setDelimiter(delimiter: CharSequence) = new WithDelimiterAndNewLine(delimiter.toString, newLine)
    def setEncoding(encoding: Charset) = new WithEncodingAndNewLine(encoding, newLine)
    def setQuotes(quotes: CharSequence) = new WithNewLineAndQuotes(newLine, quotes.toString)
  }

  protected class WithQuotes(val quotes: String) {
    def setDelimiter(delimiter: CharSequence) = new WithDelimiterAndQuotes(delimiter.toString, quotes)
    def setEncoding(encoding: Charset) = new WithEncodingAndQuotes(encoding, quotes)
    def setNewLine(newLine: CharSequence) = new WithNewLineAndQuotes(newLine.toString, quotes)
  }

  protected class WithDelimiterAndEncoding(val delimiter: String, val encoding: Charset) {
    def setNewLine(newLine: CharSequence) = new WithDelimiterAndEncodingAndNewLine(delimiter, encoding, newLine.toString)
    def setQuotes(quotes: CharSequence) = new WithDelimiterAndEncodingAndQuotes(delimiter, encoding, quotes.toString)
  }

  protected class WithDelimiterAndNewLine(val delimiter: String, val newLine: String) {
    def setEncoding(encoding: Charset) = new WithDelimiterAndEncodingAndNewLine(delimiter, encoding, newLine)
    def setQuotes(quotes: CharSequence) = new WithDelimiterAndNewLineAndQuotes(delimiter, newLine, quotes.toString)
  }

  protected class WithDelimiterAndQuotes(val delimiter: String, val quotes: String) {
    def setEncoding(encoding: Charset) = new WithDelimiterAndEncodingAndQuotes(delimiter, encoding, quotes)
    def setNewLine(newLine: CharSequence) = new WithDelimiterAndNewLineAndQuotes(delimiter, newLine.toString, quotes)
  }

  protected class WithEncodingAndNewLine(val encoding: Charset, val newLine: String) {
    def setDelimiter(delimiter: CharSequence) = new WithDelimiterAndEncodingAndNewLine(delimiter.toString, encoding, newLine)
    def setQuotes(quotes: CharSequence) = new WithEncodingAndNewLineAndQuotes(encoding, newLine, quotes.toString)
  }

  protected class WithEncodingAndQuotes(val encoding: Charset, val quotes: String) {
    def setDelimiter(delimiter: CharSequence) = new WithDelimiterAndEncodingAndQuotes(delimiter.toString, encoding, quotes)
    def setNewLine(newLine: CharSequence) = new WithEncodingAndNewLineAndQuotes(encoding, newLine.toString, quotes)
  }

  protected class WithNewLineAndQuotes(val newLine: String, val quotes: String) {
    def setDelimiter(delimiter: CharSequence) = new WithDelimiterAndNewLineAndQuotes(delimiter.toString, newLine, quotes)
    def setEncoding(encoding: Charset) = new WithEncodingAndNewLineAndQuotes(encoding, newLine, quotes)
  }

  protected class WithDelimiterAndEncodingAndNewLine(val delimiter: String, val encoding: Charset, val newLine: String) {
    def setQuotes(quotes: CharSequence) = new WithDelimiterAndEncodingAndNewLineAndQuotes(delimiter, encoding, newLine, quotes.toString)
  }

  protected class WithDelimiterAndEncodingAndQuotes(val delimiter: String, val encoding: Charset, val quotes: String) {
    def setNewLine(newLine: CharSequence) = new WithDelimiterAndEncodingAndNewLineAndQuotes(delimiter, encoding, newLine.toString, quotes)
  }

  protected class WithDelimiterAndNewLineAndQuotes(val delimiter: String, val newLine: String, val quotes: String) {
    def setEncoding(encoding: Charset) = new WithDelimiterAndEncodingAndNewLineAndQuotes(delimiter, encoding, newLine, quotes)
  }

  protected class WithEncodingAndNewLineAndQuotes(val encoding: Charset, val newLine: String, val quotes: String) {
    def setDelimiter(delimiter: CharSequence) = new WithDelimiterAndEncodingAndNewLineAndQuotes(delimiter.toString, encoding, newLine, quotes)
  }

  protected class WithDelimiterAndEncodingAndNewLineAndQuotes(val delimiter: String, val encoding: Charset, val newLine: String, val quotes: String) {
  }
}

/*
object CSVFactoryIAmSaneEnoughNotToWriteThatByHandThankYouBuilder extends App {
  case class Setter(val name: String, val clazz: Class[_]) {
    val argument  = name.head.toLower + name.tail
    val clazzString = clazz.getSimpleName

    val finalCall = argument + (if (clazz eq classOf[CharSequence]) ".toString" else "")
    val finalClazz = if (clazz eq classOf[CharSequence]) "String" else clazzString

    def createDef(others: Set[Setter]): String = {
      val all = (others + this).toList.sortBy(_.name)

      val ands = all.map(_.name).mkString("And")
      val args = all.map(x => if (x eq this) x.finalCall else x.argument).mkString(", ")

      "%sdef set%s(%s: %s) = new With%s(%s)%s".
        format(padding, name, argument, clazzString, ands, args, newLine)
    }
  }

  val setters = Set(
    Setter("Delimiter", classOf[CharSequence])
  , Setter("Encoding",  classOf[Charset])
  , Setter("NewLine",   classOf[CharSequence])
  , Setter("Quotes",    classOf[CharSequence])
  )

  val padding = "  "
  val newLine = "\n"

  val sB = new StringBuilder

  setters.subsets.foreach{s =>
    val withs = s.map(_.name).mkString("And")
    val args = s.map(x => "val %s: %s".format(x.argument, x.finalClazz)).mkString(", ")
    sB ++= "%sprotected class With%s(%s) {%s".format(padding, withs, args, newLine)
    (setters -- s).foreach(o => sB ++= "%s%s".format(padding * 2, o.createDef(s)))
    sB ++= "%s}%s%2$s".format(padding, newLine)
  }
}
*/

}

package tete {

object A extends App {
  import hr.element.doit.csv._

  CSVFactory.setNewLine("aoeu").setDelimiter(",").setEncoding(null).setQuotes("'")
}

}