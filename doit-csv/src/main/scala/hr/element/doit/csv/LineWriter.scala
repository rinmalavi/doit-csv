package hr.element.doit.csv
import java.io.Writer



class LineWriter(writer: Writer) {
  val delimiter = ";"
  val newLine = "\n"
  def write(lines : Array[String]) {
    lines.foreach(line => writer.write(getFormatedLine(line) + delimiter ))
    writer.write(newLine)

    def getFormatedLine(line: String) = {
      val sb = new StringBuilder

    }

  }

}