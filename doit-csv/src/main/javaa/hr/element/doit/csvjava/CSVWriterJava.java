package hr.element.doit.csvjava;


import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class CSVWriterJava {
  public final CSVFactoryJava config;
  public final Writer writer;

  CSVWriterJava(final CSVFactoryJava config, final Writer w) {
    this.writer = w;
    this.config = config;
  }
  public CSVWriterJava write(String[] line) throws IOException{
    int i = 0;
    if ((line == null)||(line.length == 0)) return this;
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
      int valuePivot = -config.quotes.length();
      while(true){
        valuePivot = value.indexOf(config.quotes, valuePivot + config.quotes.length());
        if(valuePivot == -1) break ;
        quoteCount++;
      }

      if ( !hasNewLine && !hasDelimiter && (quoteCount == 0))
        return value;
    }
    final int quoteLength = config.quotes.length();
    final int newValueLength = value.length() + (quoteCount + 2) * quoteLength;

    final char[] newValue = new char[newValueLength];
    final char[] oldValue = value.toCharArray();
    final char[] quotesArray = config.quotes.toCharArray();

    System.arraycopy(quotesArray, 0, newValue, 0, quoteLength);
    System.arraycopy(quotesArray, 0, newValue, newValueLength - quoteLength, quoteLength);

    int newValuePivot = quoteLength;
    int oldValueTailPivot = -quoteLength;

    while(true) {
      final int oldValueHeadPivot = oldValueTailPivot < 0 ?  0 : oldValueTailPivot;

      oldValueTailPivot = value.indexOf(
          config.quotes,
          oldValueTailPivot + quoteLength);

      if (oldValueTailPivot == -1) {
        System.arraycopy(
            oldValue,
            oldValueHeadPivot,
            newValue,
            newValuePivot,
            oldValue.length - oldValueHeadPivot);
        break;
      }

      final int copyLength = oldValueTailPivot - oldValueHeadPivot;
      System.arraycopy(oldValue, oldValueHeadPivot, newValue, newValuePivot, copyLength);
      newValuePivot += copyLength;

      System.arraycopy(quotesArray, 0, newValue, newValuePivot, quoteLength);
      newValuePivot += quoteLength;
    }
    return new String(newValue);
  }

  public CSVWriterJava write(String[][] lines) throws IOException{
    for( String[] line : lines){
      write(line);
    }
    return this;
  }
  public CSVWriterJava write(List<String[]> lines) throws IOException{
    for( String[] line : lines){
      write(line);
    }
    return this;
  }
  public <T> CSVWriterJava writeAll(List<T> list){
    return this;
  }
  public CSVWriterJava writeHeader(){
    return this;
  }
  public void close() throws IOException{
    writer.close();
  }

}
