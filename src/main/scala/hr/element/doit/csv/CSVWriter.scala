package hr.element.doit.csv

import scala.annotation.tailrec

import java.io._

class CSVWriter(config: CSVFactory, oS: OutputStream) {
  val w: Writer = new OutputStreamWriter(oS, config.encoding)

  val quoteLen = config.quotes.length

  val escapes =
    Seq(config.delimiter, config.quotes, config.newLine)

  def quoteIfNecessary(l: String) {
    if (escapes.exists(l.contains)) {
      w.write(config.quotes)
      quote(l)
      w.write(config.quotes)
    } else {
      w.write(l)
    }
  }

  def quote(l: String, i: Int = -quoteLen) {
    val oldValHead =
      math.max(i, 0)

    l.indexOf(config.quotes, i + quoteLen) match {
      case oldValTail if oldValTail == -1 =>
        w.write(l substring oldValHead)
      case oldValTai =>
        w.write(l substring(oldValHead, oldValTai))
        w.write(config.quotes)
        quote(l, oldValTai)
    }
  }

  def write(line: Array[String]) {

    if ((line == null) || line.isEmpty) {
      this
    }
    else {
      for (i <- line.indices) {
        if (i != 0){
          w.write(config.delimiter)
        }
        quoteIfNecessary(line(i))
      }
      w.write(config.newLine)
      w.flush()
      this
    }
  }
  def close() { w.close() }
}
