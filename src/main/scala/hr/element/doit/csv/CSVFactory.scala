package hr.element.doit.csv

import java.io.{ InputStream, Reader, OutputStream, Writer }
import java.nio.charset.Charset

object CSVFactory extends CSVFactory(";", "\"", "\n")

class CSVFactory private (
    val delimiter: String,
    val quotes: String,
    val newLine: String) {

  // /===========================================================================

  def getReader(reader: Reader) =
    new CSVReader(this, reader)

  def getReaderWithHeaders(reader: Reader) =
    new CSVReaderWithHeaders(this, reader)

  def getWriter(writer: Writer) =
    new CSVWriter(this, writer)

  // ===========================================================================

  def setDelimiter(delimiter: CharSequence): CSVFactory =
    new CSVFactory(quotes, delimiter.toString, newLine)

  def setDelimiter(delimiter: Char): CSVFactory =
    setDelimiter(delimiter.toString)

  // ---------------------------------------------------------------------------

  def setQuotes(quotes: CharSequence): CSVFactory =
    new CSVFactory(quotes.toString, delimiter, newLine)

  def setQuotes(quotes: Char): CSVFactory =
    setQuotes(quotes.toString)

  // ---------------------------------------------------------------------------

  def setNewLine(newLine: CharSequence): CSVFactory =
    new CSVFactory(quotes, delimiter, newLine.toString)

  def setNewLine(newLine: Char): CSVFactory =
    setNewLine(newLine.toString)
}
