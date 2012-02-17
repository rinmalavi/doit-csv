package hr.element.doit

package hr.element.doit.csv

import java.security.SecureRandom
import java.math.BigInteger
import org.scalatest.FunSuite
import scala.collection.mutable.Stack
import org.scalatest.matchers._
import hr.element.doit.csv._
import org.scalatest.GivenWhenThen
import org.scalatest.FeatureSpec
import java.io.InputStreamReader
import java.io.Reader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import _root_.hr.element.doit.csv.CSVReader
import _root_.hr.element.doit.csv.CSVFactory
import _root_.hr.element.doit.csv.WithHeaders
import scala.util.Random

class ExampleSuite2 extends GivenWhenThen
  with ShouldMatchers
  with FeatureSpec {

  scenario("Easy Test"){
    val outFileName = "/home/marin/doit/csvs/test2.csv"
    val factory = CSVFactory()

  val writer = factory.getWriter(new FileOutputStream(outFileName))
  val header = IndexedSeq("first", "second", "third")
  val line = IndexedSeq("1", "2", "3")

  writer.write(header.toArray)
  writer.write(line.toArray)
  writer.close()

  val reader = factory.getReader(
      new FileInputStream(outFileName), WithHeaders)

  val rea = reader.readLn()

  rea.foreach(println)

  }

  scenario("Simple withHeader Test.") {
    val outFileName = "/home/marin/doit/csvs/test1.csv"
    val startCase = 217
    val endCase = 267
    val strSize = 50
    val numOfQuotes = (i: Int) => i / 70
    val header = IndexedSeq("first", "second", "third")
    val rowMod = (i: Int) => (i)
    val colMod = header.length //(i:Int )  =>(i / 60)

    for (i <- startCase to endCase) {
      val t = new Random(i - 1)
      val factory = CSVFactory().
        setDelimiter(t.nextString(i / 50)).
        setNewLine(t.nextString(i / 60)).
        setQuote(t.nextString(i / 70))
      //info("deli: " + factory.delimiter + ", nL" + factory.newLine + ", q " + factory.quotes)
      if (valid(factory)) {
        val rand = new Random(i)
        val writer = factory.getWriter(new FileOutputStream(outFileName))
        writer.write(header.toArray)
        for (j <- 1 to rowMod(i)) { // redovi
          val str = for (k <- 1 to colMod)
            yield getRandStr(rand, numOfQuotes(i), factory.quotes, strSize)
          val arr = str.toArray
          writer.write(arr)
        }
        writer.close()
        val time = System.currentTimeMillis()
        val r = new Random(i)
        val f = new File(outFileName)
        info("Starting test :" + i + ", File size: " + f.length())
        val reader = factory.getReader(new FileInputStream(f), WithHeaders)
        //reader.header.foreach(println)
        reader.foreach(
            y =>
            y.foreach {
          x =>
            val str = getRandStr(r, numOfQuotes(i), factory.quotes, strSize)
            print(" t>"+y.getMap().find(_._2==str) +  "<")
             x should equal(str)
          })
      } else {}
    }
  }

  def getRandStr(r: Random, i: Int, quotes: String, strSize: Int) = {
    val str = new StringBuilder(
      r.nextString(r.nextInt().abs % strSize + 4))
    val l = str.length
    for (l <- 1 to i) {
      str.insert((r.nextInt().abs % l + 3), quotes)
    }
    str.result
  }

  def valid(fac: CSVFactory) = {
    if (fac.delimiter.contains(fac.quotes) ||

      fac.delimiter.contains(fac.newLine) ||
      fac.quotes.contains(fac.newLine) ||
      fac.quotes.contains(fac.delimiter) ||
      fac.newLine.contains(fac.quotes) ||
      fac.newLine.contains(fac.delimiter))
      false else true
  }

}