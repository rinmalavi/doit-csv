package hr.element.doit.csv
import java.io.Writer

class CSVWriter(config: CSVFactory, w: Writer) {

  def write(line: Array[String]) {
    if ((line == null)||(line.length == 0)){
      this
    } else {
      for(i <- line.indices) {
        w.write(parse(line(i)))
        if(i != line.length-1) w.write(config.delimiter)
      }
      w.write(config.newLine)
    }

//    def parse2(l: String){
//      val quoteNum =
//      while()
//    }


    def parse(l: String) = {
      if( List(config.quotes,
          config.newLine,
          config.delimiter)
          .exists(l.contains)){
        val dubleQuote = config.quotes + config.quotes
        config.quotes +
        l.replaceAll(config.quotes, dubleQuote) +
        config.quotes
      } else {
        l
      }
    }

  }
  def close() { w.close()}

}