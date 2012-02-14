package hr.element.doit.csv;



import hr.element.doit.csv.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import java.util.Iterator;


public class CSVReader implements Iterable<LineReader>, Iterator<LineReader> {

  private final CSVFactory config;
  private final Reader reader;
  private LineReader currentLine;

  CSVReader(final CSVFactory config, final InputStream iS) {
    this.config = config;
    reader = new InputStreamReader(iS, config.encoding);
  }

  @Override
  public Iterator<LineReader> iterator() {
    return this;
  }

  boolean hasMoreLines = true;

  @Override
  public boolean hasNext() {
    if (!hasMoreLines) {
      return false;
    }

    if (currentLine == null) {
      currentLine = new LineReader(config, reader);
      hasMoreLines = currentLine.nonEmpty();
    }

    return  hasMoreLines;
  }

  @Override
  public LineReader next(){
    if (!hasNext()) {
      return null;//throw new IOException("Called next on empty iterator!");
    }

    final LineReader cL = currentLine;
    currentLine = null;
    return cL;
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException("Could not remove element!");
  }
}
