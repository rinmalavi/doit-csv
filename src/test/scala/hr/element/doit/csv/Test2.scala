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
import scala.util.Random

class ExampleSuite2 extends GivenWhenThen
  with ShouldMatchers
  with FeatureSpec {

  scenario("Easy Test"){
    val outFileName = "/home/marin/doit/csvs/test2.csv"
    val factory = CSVFactory().
        setDelimiter("1234").
        setNewLine("56").
        setQuote("asdfgh")

    val oS= new FileOutputStream(outFileName)
    val writer = factory.getWriter( oS)
    //val header = IndexedSeq("first", "second", "third")
    val line = IndexedSeq("1\"25asdfgh435426546;","2656543542" ,  "gfsgfsgfsgfsd3" )

    //writer.write(header.toArray)
    writer.write(line.toArray)
    //writer.close()
    oS.close()


    val reader = factory.getReader(
        new FileInputStream(outFileName))

    //val rea = reader.readLn()

    reader.foreach(_.foreach(println(_)))
  }
//
  scenario("Simple withHeader Test.") {
    val outFileName = "/home/marin/doit/csvs/test1.csv"
    val startCase = 6066
    val endCase = 6067
    val strSize = 50
    val numOfQuotes = (i: Int) => i / 70
    val header = IndexedSeq("first", "second", "third")
    val rowMod = (i: Int) => (i)
    val colMod = header.length //(i:Int )  =>(i / 60)

    for (i <- startCase to endCase) {
      val t = new Random(i - 1)
      val factory = CSVFactory()/*.
        setDelimiter(t.nextString(i / 70)).
        setNewLine(t.nextString(i / 60)).
        setQuote(t.nextString(i / 20))*/


   //    val factory = CSVFactory().
//        setDelimiter("1234").
//        setNewLine("56").
//        setQuote("asdfgh")
      if (valid(factory)) {
        val oS= new FileOutputStream(outFileName)
        val writer = factory.getWriter(oS)
        writer.write(header.toArray)
        val rand = new Random(i)
        for (j <- 1 to rowMod(i)) {
          val str = for (k <- 1 to colMod)
            yield getRandStr(rand, numOfQuotes(i), factory.quotes, strSize)
          val arr = str.toArray
          writer.write(arr)
        }
        oS.close()
        val time = System.currentTimeMillis()
        val r = new Random(i)
        val f = new File(outFileName)
      info("Starting test :" + i + ", File size: " + f.length())
        val reader = factory.getReaderWithHeaders(new FileInputStream(f))
        reader.foreach(
            _.foreach {
                _ should equal(
                    getRandStr(r, numOfQuotes(i), factory.quotes, strSize))
          })
          info("time: "+(System.currentTimeMillis() - time))
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
