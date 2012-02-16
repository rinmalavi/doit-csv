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


  scenario("Simple withHeader Test.") {
      val outFileName = "/home/marin/doit/csvs/test1.csv"
      val startCase = 517
      val endCase   = 567
      val strSize = 120
      val numOfQuotes = (i:Int) => i/70
      val header = Array("first", "second", "third")
      val rowMod  = (i: Int) => (i)
      val colMod  = header.length  //(i:Int )  =>(i / 60)


    for (i <- startCase to endCase) {
      val t= new Random( i - 1)
      val factory = CSVFactory().
          setDelimiter(t.nextString(i / 50)).
          setNewLine(t.nextString(i / 60)).
          setQuote(t.nextString(i / 70))
      //info("deli: " + factory.delimiter + ", nL" + factory.newLine + ", q " + factory.quotes)
      if (valid(factory)) {
        val rand = new Random(i)
        val writer = factory.getWriter(new FileOutputStream(outFileName))
        writer.write(header)
        for (j <- 1 to rowMod(i)) { // redovi
          val str = for (k <- 1 to colMod)
            yield getRandStr(rand, numOfQuotes(i) , factory.quotes)
          val arr = str.toArray
          writer.write(arr)
        }
        writer.close()
        val time = System.currentTimeMillis()
        val r = new Random(i)
        val f = new File(outFileName)
        info("Starting test :" + i+ ", File size: " + f.length())
        val reader = factory.getReader(new FileInputStream(f), WithHeaders )
        reader.foreach(
          (_).foreach{ val str = getRandStr(rand, numOfQuotes(i), factory.quotes)

              _ should equal(str)
          })
          }
      else {}
      }
    }
        def getRandStr(r: Random, i: Int, quotes: String) = {
          val str = new StringBuilder(
              r.nextString(r.nextInt().abs % 120 + 4))
          val l = str.length
          for (l <- 1 to i) {
             str.insert((r.nextInt().abs % l + 3), quotes)
          }
          str.result
        }

      def valid(fac: CSVFactory) = {
        if(fac.delimiter.contains(fac.quotes) ||

        fac.delimiter.contains(fac.newLine) ||
        fac.quotes.contains(fac.newLine) ||
        fac.quotes.contains(fac.delimiter) ||
        fac.newLine.contains(fac.quotes) ||
        fac.newLine.contains(fac.delimiter))
          false else true
      }
}
