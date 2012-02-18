package hr.element.doit.csv

import java.io._
import java.nio.charset.Charset

class CSVFactory private(
    val quotes: String,
    val delimiter: String,
    val newLine: String,
    val encoding: Charset){

  def getReader(iS: InputStream) =
    new CSVReader(this, iS)

  def getReaderWithHeaders(iS: InputStream) =
    new CSVReaderWithHeaders(this, iS)

  def getWriter(out: OutputStream) =
    new CSVWriter(this, out)
}

trait DelimiterSetter { _: CSVFactory =>
  def setDelimiter(delimiter: String): CSVFactory

  def setDelimiter(delimiter: CharSequence): CSVFactory =
    setDelimiter(delimiter.toString)

  def setDelimiter(delimiter: Array[Char]): CSVFactory =
    setDelimiter(new String(delimiter))

  def setDelimiter(delimiter: Char): CSVFactory =
    setDelimiter(delimiter.toString)
}

trait QuotesSetter { _: CSVFactory =>
  def setQuotes(quotes: String): CSVFactory

  def setQuotes(quotes: CharSequence): CSVFactory =
    setQuotes(quotes)

  def setQuotes(quotes: Array[Char]): CSVFactory =
    setQuotes(new String(quotes))

  def setQuotes(quotes: Char): CSVFactory =
    setQuotes(quotes.toString)
}


trait NewLineSetter { _: CSVFactory =>
  def setNewLine(newLine: String): CSVFactory

  def setNewLine(newLine: CharSequence): CSVFactory =
    setNewLine(newLine.toString)

  def setNewLine(newLine: Array[Char]): CSVFactory =
    setNewLine(new String(newLine))

  def setNewLine(newLine: Char): CSVFactory =
    setNewLine(newLine.toString)
}

trait EncodingSetter { _: CSVFactory =>
  def setEncoding(encoding: Charset): CSVFactory

  def setEncoding(encoding: String): CSVFactory =
    setEncoding(Charset.forName(encoding))
}

object CSVFactory
    extends CSVFactory("\"", ";", "\n", Charset.forName("UTF-8"))
    with QuotesSetter
    with DelimiterSetter
    with NewLineSetter
    with EncodingSetter {
  def setQuotes(quotes: String) =
    new CSVFactory(quotes, delimiter, newLine, encoding)
      with DelimiterSetter
      with NewLineSetter
      with EncodingSetter {
        def setDelimiter(delimiter: String) =
          new CSVFactory(quotes, delimiter, newLine, encoding)
            with NewLineSetter
            with EncodingSetter {
              def setNewLine(newLine: String) =
                new CSVFactory(quotes, delimiter, newLine, encoding)
                  with EncodingSetter {
                    def setEncoding(encoding: Charset) =
                      new CSVFactory(quotes, delimiter, newLine, encoding)
                  }
              def setEncoding(encoding: Charset) =
                new CSVFactory(quotes, delimiter, newLine, encoding)
                  with NewLineSetter {
                    def setNewLine(newLine: String) =
                      new CSVFactory(quotes, delimiter, newLine, encoding)
                  }
            }
        def setNewLine(newLine: String) =
          new CSVFactory(quotes, delimiter, newLine, encoding)
            with DelimiterSetter
            with EncodingSetter {
              def setDelimiter(delimiter: String) =
                new CSVFactory(quotes, delimiter, newLine, encoding)
                  with EncodingSetter {
                    def setEncoding(encoding: Charset) =
                      new CSVFactory(quotes, delimiter, newLine, encoding)
                  }
              def setEncoding(encoding: Charset) =
                new CSVFactory(quotes, delimiter, newLine, encoding)
                  with DelimiterSetter {
                    def setDelimiter(delimiter: String) =
                      new CSVFactory(quotes, delimiter, newLine, encoding)
                  }
            }
        def setEncoding(encoding: Charset) =
          new CSVFactory(quotes, delimiter, newLine, encoding)
            with DelimiterSetter
            with NewLineSetter {
              def setDelimiter(delimiter: String) =
                new CSVFactory(quotes, delimiter, newLine, encoding)
                  with NewLineSetter {
                    def setNewLine(newLine: String) =
                      new CSVFactory(quotes, delimiter, newLine, encoding)
                  }
              def setNewLine(newLine: String) =
                new CSVFactory(quotes, delimiter, newLine, encoding)
                  with DelimiterSetter {
                    def setDelimiter(delimiter: String) =
                      new CSVFactory(quotes, delimiter, newLine, encoding)
                  }
            }
      }
  def setDelimiter(delimiter: String) =
    new CSVFactory(quotes, delimiter, newLine, encoding)
      with QuotesSetter
      with NewLineSetter
      with EncodingSetter {
        def setQuotes(quotes: String) =
          new CSVFactory(quotes, delimiter, newLine, encoding)
            with NewLineSetter
            with EncodingSetter {
              def setNewLine(newLine: String) =
                new CSVFactory(quotes, delimiter, newLine, encoding)
                  with EncodingSetter {
                    def setEncoding(encoding: Charset) =
                      new CSVFactory(quotes, delimiter, newLine, encoding)
                  }
              def setEncoding(encoding: Charset) =
                new CSVFactory(quotes, delimiter, newLine, encoding)
                  with NewLineSetter {
                    def setNewLine(newLine: String) =
                      new CSVFactory(quotes, delimiter, newLine, encoding)
                  }
            }
        def setNewLine(newLine: String) =
          new CSVFactory(quotes, delimiter, newLine, encoding)
            with QuotesSetter
            with EncodingSetter {
              def setQuotes(quotes: String) =
                new CSVFactory(quotes, delimiter, newLine, encoding)
                  with EncodingSetter {
                    def setEncoding(encoding: Charset) =
                      new CSVFactory(quotes, delimiter, newLine, encoding)
                  }
              def setEncoding(encoding: Charset) =
                new CSVFactory(quotes, delimiter, newLine, encoding)
                  with QuotesSetter {
                    def setQuotes(quotes: String) =
                      new CSVFactory(quotes, delimiter, newLine, encoding)
                  }
            }
        def setEncoding(encoding: Charset) =
          new CSVFactory(quotes, delimiter, newLine, encoding)
            with QuotesSetter
            with NewLineSetter {
              def setQuotes(quotes: String) =
                new CSVFactory(quotes, delimiter, newLine, encoding)
                  with NewLineSetter {
                    def setNewLine(newLine: String) =
                      new CSVFactory(quotes, delimiter, newLine, encoding)
                  }
              def setNewLine(newLine: String) =
                new CSVFactory(quotes, delimiter, newLine, encoding)
                  with QuotesSetter {
                    def setQuotes(quotes: String) =
                      new CSVFactory(quotes, delimiter, newLine, encoding)
                  }
            }
      }
  def setNewLine(newLine: String) =
    new CSVFactory(quotes, delimiter, newLine, encoding)
      with QuotesSetter
      with DelimiterSetter
      with EncodingSetter {
        def setQuotes(quotes: String) =
          new CSVFactory(quotes, delimiter, newLine, encoding)
            with DelimiterSetter
            with EncodingSetter {
              def setDelimiter(delimiter: String) =
                new CSVFactory(quotes, delimiter, newLine, encoding)
                  with EncodingSetter {
                    def setEncoding(encoding: Charset) =
                      new CSVFactory(quotes, delimiter, newLine, encoding)
                  }
              def setEncoding(encoding: Charset) =
                new CSVFactory(quotes, delimiter, newLine, encoding)
                  with DelimiterSetter {
                    def setDelimiter(delimiter: String) =
                      new CSVFactory(quotes, delimiter, newLine, encoding)
                  }
            }
        def setDelimiter(delimiter: String) =
          new CSVFactory(quotes, delimiter, newLine, encoding)
            with QuotesSetter
            with EncodingSetter {
              def setQuotes(quotes: String) =
                new CSVFactory(quotes, delimiter, newLine, encoding)
                  with EncodingSetter {
                    def setEncoding(encoding: Charset) =
                      new CSVFactory(quotes, delimiter, newLine, encoding)
                  }
              def setEncoding(encoding: Charset) =
                new CSVFactory(quotes, delimiter, newLine, encoding)
                  with QuotesSetter {
                    def setQuotes(quotes: String) =
                      new CSVFactory(quotes, delimiter, newLine, encoding)
                  }
            }
        def setEncoding(encoding: Charset) =
          new CSVFactory(quotes, delimiter, newLine, encoding)
            with QuotesSetter
            with DelimiterSetter {
              def setQuotes(quotes: String) =
                new CSVFactory(quotes, delimiter, newLine, encoding)
                  with DelimiterSetter {
                    def setDelimiter(delimiter: String) =
                      new CSVFactory(quotes, delimiter, newLine, encoding)
                  }
              def setDelimiter(delimiter: String) =
                new CSVFactory(quotes, delimiter, newLine, encoding)
                  with QuotesSetter {
                    def setQuotes(quotes: String) =
                      new CSVFactory(quotes, delimiter, newLine, encoding)
                  }
            }
      }
  def setEncoding(encoding: Charset) =
    new CSVFactory(quotes, delimiter, newLine, encoding)
      with QuotesSetter
      with DelimiterSetter
      with NewLineSetter {
        def setQuotes(quotes: String) =
          new CSVFactory(quotes, delimiter, newLine, encoding)
            with DelimiterSetter
            with NewLineSetter {
              def setDelimiter(delimiter: String) =
                new CSVFactory(quotes, delimiter, newLine, encoding)
                  with NewLineSetter {
                    def setNewLine(newLine: String) =
                      new CSVFactory(quotes, delimiter, newLine, encoding)
                  }
              def setNewLine(newLine: String) =
                new CSVFactory(quotes, delimiter, newLine, encoding)
                  with DelimiterSetter {
                    def setDelimiter(delimiter: String) =
                      new CSVFactory(quotes, delimiter, newLine, encoding)
                  }
            }
        def setDelimiter(delimiter: String) =
          new CSVFactory(quotes, delimiter, newLine, encoding)
            with QuotesSetter
            with NewLineSetter {
              def setQuotes(quotes: String) =
                new CSVFactory(quotes, delimiter, newLine, encoding)
                  with NewLineSetter {
                    def setNewLine(newLine: String) =
                      new CSVFactory(quotes, delimiter, newLine, encoding)
                  }
              def setNewLine(newLine: String) =
                new CSVFactory(quotes, delimiter, newLine, encoding)
                  with QuotesSetter {
                    def setQuotes(quotes: String) =
                      new CSVFactory(quotes, delimiter, newLine, encoding)
                  }
            }
        def setNewLine(newLine: String) =
          new CSVFactory(quotes, delimiter, newLine, encoding)
            with QuotesSetter
            with DelimiterSetter {
              def setQuotes(quotes: String) =
                new CSVFactory(quotes, delimiter, newLine, encoding)
                  with DelimiterSetter {
                    def setDelimiter(delimiter: String) =
                      new CSVFactory(quotes, delimiter, newLine, encoding)
                  }
              def setDelimiter(delimiter: String) =
                new CSVFactory(quotes, delimiter, newLine, encoding)
                  with QuotesSetter {
                    def setQuotes(quotes: String) =
                      new CSVFactory(quotes, delimiter, newLine, encoding)
                  }
            }
      }
}

/*
object CSVFactoryIAmSaneEnoughNotToWriteThatByHandThankYouBuilder extends App {
  case class Setter(val name: String, val clazz: Class[_]) {
    val clazzString = clazz.getSimpleName
    val argument  = name.head.toLower + name.tail
  }

  val setters = List(
    Setter("Quotes",    classOf[String])
  , Setter("Delimiter", classOf[String])
  , Setter("NewLine",   classOf[String])
  , Setter("Encoding",  classOf[Charset])
  )

  val padding = "  "
  val newLine = "\n"

  val constructorLine =
    "new CSVFactory(%s)".format(setters.map(_.argument).mkString(", "))

  def buildSetter(s: Setter, others: List[Setter], padCount: Int): String = {
    val defLine =
      "%sdef set%s(%s: %s) =%s".format(padding * padCount, s.name, s.argument, s.clazzString, newLine)

    val cLine =
      "%s%s%s".format(padding * (padCount + 1), constructorLine, newLine)

    val mixins =
      others.map(o =>
        "%swith %sSetter".format(padding * (padCount + 2), o.name)
      ).mkString(newLine)

    val body =
      others match {
        case Nil =>
          ""

        case _ =>
          " {%s%s%s}%s".format(newLine, buildSetters(others, padCount + 3), padding * (padCount + 2), newLine)
      }

    defLine + cLine + mixins + body
  }

  def buildSetters(setters: List[Setter], padCount: Int) =
    setters.map(s => buildSetter(s, setters.filterNot(s ==), padCount)).mkString("")

  println(buildSetters(setters, 1))
}
*/