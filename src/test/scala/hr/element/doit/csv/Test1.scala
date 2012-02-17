//package hr.element.doit
//
//package hr.element.doit.csv
//
//import java.security.SecureRandom
//import java.math.BigInteger
//import org.scalatest.FunSuite
//import scala.collection.mutable.Stack
//import org.scalatest.matchers._
//import hr.element.doit.csv._
//import org.scalatest.GivenWhenThen
//import org.scalatest.FeatureSpec
//import java.io.InputStreamReader
//import java.io.Reader
//import java.io.File
//import java.io.FileInputStream
//import java.io.FileOutputStream
//import _root_.hr.element.doit.csv.CSVReader
//import _root_.hr.element.doit.csv.CSVFactory
//import scala.util.Random
//
//class ExampleSuite extends GivenWhenThen
//  with ShouldMatchers
//  with FeatureSpec {
//
////    scenario("Complex test"){
////      info("")
////      val f = "/home/marin/doit/csvs/test2.csv"
////      val factory = CSVFactory()
////      val reader = factory.getReader(new FileInputStream(f))
////
////      reader.foreach(x =>{
////          (x).foreach(y => print(y+"v"))
////            println()
////      })
////    }
////}
////
//  scenario("simple Test") {
//    val outFileName = "/home/marin/doit/csvs/test1.csv"
//      val startCase = 517
//      val endCase   = 567
//      val rowMod  = (i: Int) => (i)
//      val colMod  = (i:Int )  =>(i / 60)
//      val strSize = 120
//      val numOfQuotes = (i:Int) => i/70
//
//
//    for (i <- startCase to endCase) {
//      val t= new Random( i - 1)
////      val delimiter = t.nextString(i / 50)
////      val newLine = t.nextString(i / 60)
////      val quotes = t.nextString(i / 70)
//      val delimiter = (  50).toString
//      val newLine = (  60).toString
//      val quotes = ( 170).toString
//      //info("deli: " + delimiter + ", nL" + newLine + ", q " + quotes)
//      if (delimiter.contains(quotes) ||
//        delimiter.contains(newLine) ||
//        quotes.contains(newLine) ||
//        quotes.contains(delimiter) ||
//        newLine.contains(quotes) ||
//        newLine.contains(delimiter)) {
//      }
//      else {
//        val factory = CSVFactory().
//          setDelimiter(delimiter).
//          setNewLine(newLine).
//          setQuote(quotes)
//
//        val rand = new Random(i)
//        val writer = factory.getWriter(new FileOutputStream(outFileName))
//        for (j <- 1 to rowMod(i)) { // redovi
//
//
//          val str =
//            for (k <- 1 to colMod(i)) yield {
//              val str = new StringBuilder(
//                rand.nextString(rand.nextInt().abs % strSize + 4))
//              val l = str.length
//              for (l <- 1 to numOfQuotes(i)) {
//                str.insert((rand.nextInt().abs % l + 3), quotes)
//              }
//              str.result()
//
//            }
//          val arr = str.toArray
//          //arr.foreach(println)
//          writer.write(arr)
//
//        }
//
//        writer.close()
//        val time = System.currentTimeMillis()
//        val r = new Random(i)
//        val f = new File(outFileName)
//        info("Starting test :" + i+ ", File size: " + f.length())
//        val reader = factory.getReader(new FileInputStream(f))
//        reader.foreach(
//          (_).foreach{
//            x =>
//              val str = new StringBuilder(
//                r.nextString(r.nextInt().abs % strSize + 4))
//              val l = str.length
//              for (l <- 1 to numOfQuotes(i)) {
//                str.insert((r.nextInt().abs % l + 3), quotes)
//              }
//              x should equal(str.result())
//
//          })
//
//        info("                        time: " + (System.currentTimeMillis() - time))
//      }
//    }
//  }
//}
