package hr.element.doit.csv;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class CSVWriter {
  public final CSVFactory config;
  public final Writer writer;

  CSVWriter(final CSVFactory config, final Writer w) {
    this.writer = w;
    this.config = config;
  }
  public CSVWriter write(String[] line) throws IOException{
    int i = 0;
    while(true){
      final String van = getStringToWrite(line[i]);
      writer.write(van);

      if(++i == line.length) break;
      writer.write(config.delimiter);
    }
    writer.write(config.newLine);
    return this;
  }
  private String getStringToWrite(final String value) {

    int quoteCount = 0; {
      final boolean hasNewLine = -1 != value.indexOf(config.newLine);
      final boolean hasDelimiter = -1 != value.indexOf(config.delimiter);
      int valuePivot = -1;
      while(true){
        valuePivot = value.indexOf(config.quotes, valuePivot + 1);
        if(valuePivot == -1) break ;
        quoteCount++;
      }

      if ( !hasNewLine && !hasDelimiter && (quoteCount == 0))
        return value;
    }
    final int qouteLength = config.quotes.length();
    final int newValueLength = value.length() + (quoteCount + 2) * qouteLength;

    final char[] newValue = new char[newValueLength];
    final char[] oldValue = value.toCharArray();
    final char[] qoutesArray = config.quotes.toCharArray();

    System.arraycopy(qoutesArray, 0, newValue, 0, qouteLength);
    System.arraycopy(qoutesArray, 0, newValue, newValueLength - qouteLength, qouteLength);

    int newValuePivot = qouteLength;
    int oldValueTailPivot = -1;

    while(true) {
      final int oldValueHeadPivot = oldValueTailPivot < 0 ?  0 :oldValueTailPivot;

      oldValueTailPivot = value.indexOf(config.quotes, oldValueTailPivot+1);

      if (oldValueTailPivot == -1) {
        System.arraycopy(oldValue, oldValueHeadPivot, newValue, newValuePivot, oldValue.length - oldValueHeadPivot);
        break;
      }

      final int copyLength = oldValueTailPivot - oldValueHeadPivot;
      System.arraycopy(oldValue, oldValueHeadPivot, newValue, newValuePivot, copyLength);
      newValuePivot += copyLength;

      System.arraycopy(qoutesArray, 0, newValue, newValuePivot, qouteLength);
      newValuePivot += qouteLength;
    }
    return new String(newValue);
  }

  public CSVWriter write(String[][] lines) throws IOException{
    for( String[] line : lines){
      write(line);
    }
    return this;
  }
  public CSVWriter write(List<String[]> lines) throws IOException{
    for( String[] line : lines){
      write(line);
    }
    return this;
  }
  public <T> CSVWriter writeAll(List<T> list){
    return this;
  }
  public CSVWriter writeHeader(){
    return this;
  }
  public void close() throws IOException{
    writer.close();
  }

}
