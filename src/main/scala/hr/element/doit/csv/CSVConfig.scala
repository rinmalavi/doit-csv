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

  def getReaderWithHeaders(reader: Reader) =
    new CSVReaderWithHeaders(this, reader)

  def getWriter(writer: Writer) =
    new CSVWriter(this, writer)

  class withEncoding(val encoding: Charset) {
    def getReader(iS: InputStream): CSVReader =
      new CSVReader(self, new InputStreamReader(iS, encoding))

    def getReaderWithHeaders(iS: InputStream): CSVReaderWithHeaders =
      new CSVReaderWithHeaders(self, new InputStreamReader(iS, encoding))

    def getWriter(oS: OutputStream): CSVWriter =
      new CSVWriter(self, new OutputStreamWriter(oS, encoding))
  }
}

object CSVConfig {
  val default =
    new CSVConfig(";", "\\n", "\"")
      .setEncoding(Charset.forName("UTF8"))

  def setDelimiter(delimiter: CharSequence) =
    new WithDelimiter(delimiter.toString)

  def setDelimiter(delimiter: Char): WithDelimiter =
    setDelimiter(delimiter.toString)

  def setEncoding(encoding: Charset) =
    new WithEncoding(encoding)

  def setEncoding(encoding: String): WithEncoding =
    setEncoding(Charset.forName(encoding))

  def setNewLine(newLine: CharSequence) =
    new WithNewLine(newLine.toString)

  def setNewLine(newLine: Char): WithNewLine =
    setNewLine(newLine.toString)

  def setQuotes(quotes: CharSequence) =
    new WithQuotes(quotes.toString)

  def setQuotes(quotes: Char): WithQuotes =
    setQuotes(quotes.toString)

  protected class WithDelimiter(val delimiter: String) {
    def setEncoding(encoding: Charset) =
      new WithDelimiterAndEncoding(delimiter, encoding)

    def setEncoding(encoding: String): WithDelimiterAndEncoding =
      setEncoding(Charset.forName(encoding))

    def setNewLine(newLine: CharSequence) =
      new WithDelimiterAndNewLine(delimiter, newLine.toString)

    def setNewLine(newLine: Char): WithDelimiterAndNewLine =
      setNewLine(newLine.toString)

    def setQuotes(quotes: CharSequence) =
      new WithDelimiterAndQuotes(delimiter, quotes.toString)

    def setQuotes(quotes: Char): WithDelimiterAndQuotes =
      setQuotes(quotes.toString)
  }

  protected class WithEncoding(val encoding: Charset) {
    def setDelimiter(delimiter: CharSequence) =
      new WithDelimiterAndEncoding(delimiter.toString, encoding)

    def setDelimiter(delimiter: Char): WithDelimiterAndEncoding =
      setDelimiter(delimiter.toString)

    def setNewLine(newLine: CharSequence) =
      new WithEncodingAndNewLine(encoding, newLine.toString)

    def setNewLine(newLine: Char): WithEncodingAndNewLine =
      setNewLine(newLine.toString)

    def setQuotes(quotes: CharSequence) =
      new WithEncodingAndQuotes(encoding, quotes.toString)

    def setQuotes(quotes: Char): WithEncodingAndQuotes =
      setQuotes(quotes.toString)
  }

  protected class WithNewLine(val newLine: String) {
    def setDelimiter(delimiter: CharSequence) =
      new WithDelimiterAndNewLine(delimiter.toString, newLine)

    def setDelimiter(delimiter: Char): WithDelimiterAndNewLine =
      setDelimiter(delimiter.toString)

    def setEncoding(encoding: Charset) =
      new WithEncodingAndNewLine(encoding, newLine)

    def setEncoding(encoding: String): WithEncodingAndNewLine =
      setEncoding(Charset.forName(encoding))

    def setQuotes(quotes: CharSequence) =
      new WithNewLineAndQuotes(newLine, quotes.toString)

    def setQuotes(quotes: Char): WithNewLineAndQuotes =
      setQuotes(quotes.toString)
  }

  protected class WithQuotes(val quotes: String) {
    def setDelimiter(delimiter: CharSequence) =
      new WithDelimiterAndQuotes(delimiter.toString, quotes)

    def setDelimiter(delimiter: Char): WithDelimiterAndQuotes =
      setDelimiter(delimiter.toString)

    def setEncoding(encoding: Charset) =
      new WithEncodingAndQuotes(encoding, quotes)

    def setEncoding(encoding: String): WithEncodingAndQuotes =
      setEncoding(Charset.forName(encoding))

    def setNewLine(newLine: CharSequence) =
      new WithNewLineAndQuotes(newLine.toString, quotes)

    def setNewLine(newLine: Char): WithNewLineAndQuotes =
      setNewLine(newLine.toString)
  }

  protected class WithDelimiterAndEncoding(val delimiter: String, val encoding: Charset) {
    def setNewLine(newLine: CharSequence) =
      new WithDelimiterAndEncodingAndNewLine(delimiter, encoding, newLine.toString)

    def setNewLine(newLine: Char): WithDelimiterAndEncodingAndNewLine =
      setNewLine(newLine.toString)

    def setQuotes(quotes: CharSequence) =
      new WithDelimiterAndEncodingAndQuotes(delimiter, encoding, quotes.toString)

    def setQuotes(quotes: Char): WithDelimiterAndEncodingAndQuotes =
      setQuotes(quotes.toString)
  }

  protected class WithDelimiterAndNewLine(val delimiter: String, val newLine: String) {
    def setEncoding(encoding: Charset) =
      new WithDelimiterAndEncodingAndNewLine(delimiter, encoding, newLine)

    def setEncoding(encoding: String): WithDelimiterAndEncodingAndNewLine =
      setEncoding(Charset.forName(encoding))

    def setQuotes(quotes: CharSequence) =
      new CSVConfig(delimiter, newLine, quotes.toString)

    def setQuotes(quotes: Char): CSVConfig =
      setQuotes(quotes.toString)
  }

  protected class WithDelimiterAndQuotes(val delimiter: String, val quotes: String) {
    def setEncoding(encoding: Charset) =
      new WithDelimiterAndEncodingAndQuotes(delimiter, encoding, quotes)

    def setEncoding(encoding: String): WithDelimiterAndEncodingAndQuotes =
      setEncoding(Charset.forName(encoding))

    def setNewLine(newLine: CharSequence) =
      new CSVConfig(delimiter, newLine.toString, quotes)

    def setNewLine(newLine: Char): CSVConfig =
      setNewLine(newLine.toString)
  }

  protected class WithEncodingAndNewLine(val encoding: Charset, val newLine: String) {
    def setDelimiter(delimiter: CharSequence) =
      new WithDelimiterAndEncodingAndNewLine(delimiter.toString, encoding, newLine)

    def setDelimiter(delimiter: Char): WithDelimiterAndEncodingAndNewLine =
      setDelimiter(delimiter.toString)

    def setQuotes(quotes: CharSequence) =
      new WithEncodingAndNewLineAndQuotes(encoding, newLine, quotes.toString)

    def setQuotes(quotes: Char): WithEncodingAndNewLineAndQuotes =
      setQuotes(quotes.toString)
  }

  protected class WithEncodingAndQuotes(val encoding: Charset, val quotes: String) {
    def setDelimiter(delimiter: CharSequence) =
      new WithDelimiterAndEncodingAndQuotes(delimiter.toString, encoding, quotes)

    def setDelimiter(delimiter: Char): WithDelimiterAndEncodingAndQuotes =
      setDelimiter(delimiter.toString)

    def setNewLine(newLine: CharSequence) =
      new WithEncodingAndNewLineAndQuotes(encoding, newLine.toString, quotes)

    def setNewLine(newLine: Char): WithEncodingAndNewLineAndQuotes =
      setNewLine(newLine.toString)
  }

  protected class WithNewLineAndQuotes(val newLine: String, val quotes: String) {
    def setDelimiter(delimiter: CharSequence) =
      new CSVConfig(delimiter.toString, newLine, quotes)

    def setDelimiter(delimiter: Char): CSVConfig =
      setDelimiter(delimiter.toString)

    def setEncoding(encoding: Charset) =
      new WithEncodingAndNewLineAndQuotes(encoding, newLine, quotes)

    def setEncoding(encoding: String): WithEncodingAndNewLineAndQuotes =
      setEncoding(Charset.forName(encoding))
  }

  protected class WithDelimiterAndEncodingAndNewLine(val delimiter: String, val encoding: Charset, val newLine: String) {
    def setQuotes(quotes: CharSequence) =
      new CSVConfig(delimiter, newLine, quotes.toString)
        .setEncoding(encoding)

    def setQuotes(quotes: Char): CSVConfig#withEncoding =
      setQuotes(quotes.toString)
  }

  protected class WithDelimiterAndEncodingAndQuotes(val delimiter: String, val encoding: Charset, val quotes: String) {
    def setNewLine(newLine: CharSequence) =
      new CSVConfig(delimiter, newLine.toString, quotes)
        .setEncoding(encoding)

    def setNewLine(newLine: Char): CSVConfig#withEncoding =
      setNewLine(newLine.toString)
  }

  protected class WithEncodingAndNewLineAndQuotes(val encoding: Charset, val newLine: String, val quotes: String) {
    def setDelimiter(delimiter: CharSequence) =
      new CSVConfig(delimiter.toString, newLine, quotes)
        .setEncoding(encoding)

    def setDelimiter(delimiter: Char): CSVConfig#withEncoding =
      setDelimiter(delimiter.toString)
  }
}
