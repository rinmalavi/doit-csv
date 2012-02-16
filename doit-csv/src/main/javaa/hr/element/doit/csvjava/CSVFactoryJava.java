package hr.element.doit.csvjava;


import hr.element.doit.csv.CSVReader;
import hr.element.doit.csv.CSVWriter;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

public class CSVFactoryJava {
  public final String delimiter;
  public final String newLine;
  public final String quotes;
  public final Charset encoding;

  public static CSVFactoryJava factory() {
    return new CSVFactoryJava(";", "\n", "\"", Charset.forName("UTF-8"));
  }

  private CSVFactoryJava(final String delimiter, final String newLine,
      final String quotes, final Charset encoding) {
    this.delimiter = delimiter;
    this.newLine = newLine;
    this.quotes = quotes;
    this.encoding = encoding;
  }

  public CSVFactoryJava setDelimiter(final String delimiter) {
    return new CSVFactoryJava(delimiter, newLine, quotes, encoding);
  }

  public CSVFactoryJava setDelimiter(final char delimiter) {
    return setDelimiter(String.valueOf(delimiter));
  }

  public CSVFactoryJava setNewLine(final String newLine) {
    return new CSVFactoryJava(delimiter, newLine, quotes, encoding);
  }

  public CSVFactoryJava setNewLine(final char newLine) {
    return setNewLine(String.valueOf(newLine));
  }

  public CSVFactoryJava setEncoding(final String encoding) {
    return setEncoding(Charset.forName(encoding));
  }

  public CSVFactoryJava setEncoding(final Charset encoding) {
    return new CSVFactoryJava(delimiter, newLine, quotes, encoding);
  }

  public CSVFactoryJava setQuotes(final String quotes) {
    return new CSVFactoryJava(delimiter, newLine, quotes, encoding);
  }

  public CSVFactoryJava setQuotes(final char quotes) {
    return setQuotes(String.valueOf(quotes));
  }

  // ----------------------------------------------------

  public CSVWriter getWriter(final OutputStream oS) {
    Writer w = new OutputStreamWriter(oS, encoding);
    return new CSVWriter(this, w);
  }

  // -----------------------------------------------------
  public CSVReader getReader(final InputStream iS) {
    return new CSVReader(this, iS);
  }
}
