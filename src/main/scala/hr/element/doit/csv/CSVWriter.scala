package hr.element.doit.csv

import scala.annotation.tailrec

import java.io._

class CSVWriter(config: CSVConfig, writer: Writer) {
  val quoteLen = config.quotes.length

  val escapes =
    Seq(config.delimiter, config.quotes, config.newLine)

  def quoteIfNecessary(l: String) {
    if (escapes.exists(l.contains)) {
      writer.write(config.quotes)
      quote(l)
      writer.write(config.quotes)
    } else {
      writer.write(l)
    }
  }

  def quote(l: String, i: Int = -quoteLen) {
    val oldValHead =
      math.max(i, 0)

    l.indexOf(config.quotes, i + quoteLen) match {
      case oldValTail if oldValTail == -1 =>
        writer.write(l substring oldValHead)
      case oldValTai =>
        writer.write(l substring(oldValHead, oldValTai))
        writer.write(config.quotes)
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
          writer.write(config.delimiter)
        }
        quoteIfNecessary(line(i))
      }
      writer.write(config.newLine)
      writer.flush()
      this
    }
  }
}
