package hr.element.doit.csv

import java.io._
import java.nio.charset.Charset

class CSVConfig protected(
    val delimiter: String,
    val newLine: String,
    val quotes: String) { self =>
  def setEncoding(encoding: Charset) =
    new withEncoding(encoding)

  def getReader(reader: Reader) =
    new CSVReader(this, reader)

  def getWriter(writer: Writer) =
    new CSVWriter(this, writer)

  class withEncoding(val encoding: Charset) {
    def getReader(iS: InputStream): CSVReader =
      new CSVReader(self, new InputStreamReader(iS, encoding))

    def getWriter(oS: OutputStream): CSVWriter =
      new CSVWriter(self, new OutputStreamWriter(oS, encoding))
  }
}

object CSVConfig {
  val default =
    new CSVConfig(";", "\\n", "\"").
      setEncoding(Charset.forName("UTF8"))

  def setDelimiter(delimiter: CharSequence) =
    new WithDelimiter(delimiter.toString)

  def setEncoding(encoding: Charset) =
    new WithEncoding(encoding)

  def setNewLine(newLine: CharSequence) =
    new WithNewLine(newLine.toString)

  def setQuotes(quotes: CharSequence) =
    new WithQuotes(quotes.toString)

  protected class WithDelimiter(val delimiter: String) {
    def setEncoding(encoding: Charset) =
      new WithDelimiterAndEncoding(delimiter, encoding)

    def setNewLine(newLine: CharSequence) =
      new WithDelimiterAndNewLine(delimiter, newLine.toString)

    def setQuotes(quotes: CharSequence) =
      new WithDelimiterAndQuotes(delimiter, quotes.toString)
  }

  protected class WithEncoding(val encoding: Charset) {
    def setDelimiter(delimiter: CharSequence) =
      new WithDelimiterAndEncoding(delimiter.toString, encoding)

    def setNewLine(newLine: CharSequence) =
      new WithEncodingAndNewLine(encoding, newLine.toString)

    def setQuotes(quotes: CharSequence) =
      new WithEncodingAndQuotes(encoding, quotes.toString)
  }

  protected class WithNewLine(val newLine: String) {
    def setDelimiter(delimiter: CharSequence) =
      new WithDelimiterAndNewLine(delimiter.toString, newLine)

    def setEncoding(encoding: Charset) =
      new WithEncodingAndNewLine(encoding, newLine)

    def setQuotes(quotes: CharSequence) =
      new WithNewLineAndQuotes(newLine, quotes.toString)
  }

  protected class WithQuotes(val quotes: String) {
    def setDelimiter(delimiter: CharSequence) =
      new WithDelimiterAndQuotes(delimiter.toString, quotes)

    def setEncoding(encoding: Charset) =
      new WithEncodingAndQuotes(encoding, quotes)

    def setNewLine(newLine: CharSequence) =
      new WithNewLineAndQuotes(newLine.toString, quotes)
  }

  protected class WithDelimiterAndEncoding(val delimiter: String, val encoding: Charset) {
    def setNewLine(newLine: CharSequence) =
      new WithDelimiterAndEncodingAndNewLine(delimiter, encoding, newLine.toString)

    def setQuotes(quotes: CharSequence) =
      new WithDelimiterAndEncodingAndQuotes(delimiter, encoding, quotes.toString)
  }

  protected class WithDelimiterAndNewLine(val delimiter: String, val newLine: String) {
    def setEncoding(encoding: Charset) =
      new WithDelimiterAndEncodingAndNewLine(delimiter, encoding, newLine)

    def setQuotes(quotes: CharSequence) =
      new CSVConfig(delimiter, newLine, quotes.toString)
  }

  protected class WithDelimiterAndQuotes(val delimiter: String, val quotes: String) {
    def setEncoding(encoding: Charset) =
      new WithDelimiterAndEncodingAndQuotes(delimiter, encoding, quotes)

    def setNewLine(newLine: CharSequence) =
      new CSVConfig(delimiter, newLine.toString, quotes)
  }

  protected class WithEncodingAndNewLine(val encoding: Charset, val newLine: String) {
    def setDelimiter(delimiter: CharSequence) =
      new WithDelimiterAndEncodingAndNewLine(delimiter.toString, encoding, newLine)

    def setQuotes(quotes: CharSequence) =
      new WithEncodingAndNewLineAndQuotes(encoding, newLine, quotes.toString)
  }

  protected class WithEncodingAndQuotes(val encoding: Charset, val quotes: String) {
    def setDelimiter(delimiter: CharSequence) =
      new WithDelimiterAndEncodingAndQuotes(delimiter.toString, encoding, quotes)

    def setNewLine(newLine: CharSequence) =
      new WithEncodingAndNewLineAndQuotes(encoding, newLine.toString, quotes)
  }

  protected class WithNewLineAndQuotes(val newLine: String, val quotes: String) {
    def setDelimiter(delimiter: CharSequence) =
      new CSVConfig(delimiter.toString, newLine, quotes)

    def setEncoding(encoding: Charset) =
      new WithEncodingAndNewLineAndQuotes(encoding, newLine, quotes)
  }

  protected class WithDelimiterAndEncodingAndNewLine(val delimiter: String, val encoding: Charset, val newLine: String) {
    def setQuotes(quotes: CharSequence) =
      new CSVConfig(delimiter, newLine, quotes.toString).
        setEncoding(encoding)
  }

  protected class WithDelimiterAndEncodingAndQuotes(val delimiter: String, val encoding: Charset, val quotes: String) {
    def setNewLine(newLine: CharSequence) =
      new CSVConfig(delimiter, newLine.toString, quotes).
        setEncoding(encoding)
  }

  protected class WithEncodingAndNewLineAndQuotes(val encoding: Charset, val newLine: String, val quotes: String) {
    def setDelimiter(delimiter: CharSequence) =
      new CSVConfig(delimiter.toString, newLine, quotes).
        setEncoding(encoding)
  }
}
