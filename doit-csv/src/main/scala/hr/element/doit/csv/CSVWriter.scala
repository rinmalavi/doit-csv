package hr.element.doit.csv
import java.io.Writer
import scala.annotation.tailrec

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

    def parse2(l: String) ={
      var quoteCount = 0
      var valuePivot = config.quotes.length()
      @tailrec
      def count(lastPivot: Int){
        val valuePivot = l.indexOf(config.quotes,
            lastPivot + config.quotes.length());
        if(valuePivot == -1) {}
        else{
          quoteCount += 1
          count(valuePivot)
          }
        }
      count(-config.quotes.length)
      if( Seq(config.newLine,
          config.delimiter)
          .exists(l.contains)||(quoteCount != 0)){
        val quoteLen = config.quotes.length
        val newValueLength = l.length +(quoteCount + 2) * quoteLen
        val newValue = new StringBuilder()
        newValue append config.quotes
        def buildStringFrom(i: Int) {
          val oldValHead =  if (i < 0) 0 else i
          val oldValTail =  l.indexOf(config.quotes,
              i + config.quotes.length)
          if (oldValTail == -1){
            newValue append l.drop(oldValHead)
          } else {
            newValue append l.take(oldValTail).drop(oldValHead)
            newValue append config.quotes
            buildStringFrom(oldValTail)
            }
        }
        buildStringFrom(-config.quotes.length)
        newValue append config.quotes
        newValue result
      } else l
    }



    def parse(l: String) = {
      if( Seq(config.quotes,
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