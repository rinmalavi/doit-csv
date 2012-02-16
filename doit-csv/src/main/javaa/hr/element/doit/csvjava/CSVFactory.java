package hr.element.doit.csvjava;


import java.io.InputStream;
import java.nio.charset.Charset;

public class CSVFactory {
  public final String delimiter;
  public final String newLine;
  public final String quotes;
  public final Charset encoding;

  public static CSVFactory factory() {
    return new CSVFactory(";", "\n", "\"", Charset.forName("UTF-8"));
  }

  private CSVFactory(final String delimiter, final String newLine,
      final String quotes, final Charset encoding) {
    this.delimiter = delimiter;
    this.newLine = newLine;
    this.quotes = quotes;
    this.encoding = encoding;
  }

  public CSVFactory setDelimiter(final String delimiter) {
    return new CSVFactory(delimiter, newLine, quotes, encoding);
  }

  public CSVFactory setDelimiter(final char delimiter) {
    return setDelimiter(String.valueOf(delimiter));
  }

  public CSVFactory setNewLine(final String newLine) {
    return new CSVFactory(delimiter, newLine, quotes, encoding);
  }

  public CSVFactory setNewLine(final char newLine) {
    return setNewLine(String.valueOf(newLine));
  }

  public CSVFactory setEncoding(final String encoding) {
    return setEncoding(Charset.forName(encoding));
  }

  public CSVFactory setEncoding(final Charset encoding) {
    return new CSVFactory(delimiter, newLine, quotes, encoding);
  }

  public CSVFactory setQuotes(final String quotes) {
    return new CSVFactory(delimiter, newLine, quotes, encoding);
  }

  public CSVFactory setQuotes(final char quotes) {
    return setQuotes(String.valueOf(quotes));
  }

  // ----------------------------------------------------
/*
  public CSVWriter getWriter(final OutputStream oS) {
    Writer w = new OutputStreamWriter(oS, encoding);
    return new CSVWriter(this, w);
  }
*/
  // -----------------------------------------------------
  public CSVReaderOld getReader(final InputStream iS) {
    return new CSVReaderOld(this, iS);
  }
}
