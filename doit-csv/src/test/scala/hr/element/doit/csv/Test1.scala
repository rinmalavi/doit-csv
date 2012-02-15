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

class ExampleSuite extends GivenWhenThen
  with ShouldMatchers
  with FeatureSpec {

  //  scenario("SlidingMatch test"){
  //    info("")
  //    val ssm = SlidingMatcher("q");
  //
  //    ssm.consume('q') should be (Delimiter)
  //  }

  scenario("simple Test") {
    val outFileName = "/home/marin/doit/csvs/test1.csv"
      val startCase = 517
      val endCase   = 557
      val rowMod  = (i: Int) => (i)
      val colMod  = (i:Int )  =>(i / 60)
      val strSize = 120
      val numOfQuotes = (i:Int) => i/70


    for (i <- startCase to endCase) {
      val t= new Random( i - 1)
//      val delimiter = t.nextString(i / 50)
//      val newLine = t.nextString(i / 60)
//      val quotes = t.nextString(i / 70)
      val delimiter = (  50).toString
      val newLine = (  60).toString
      val quotes = ( 170).toString
      //info("deli: " + delimiter + ", nL" + newLine + ", q " + quotes)
      if (delimiter.contains(quotes) ||
        delimiter.contains(newLine) ||
        quotes.contains(newLine) ||
        quotes.contains(delimiter) ||
        newLine.contains(quotes) ||
        newLine.contains(delimiter)) {
      }
      else {
        val factory = CSVFactory.factory().
          setDelimiter(delimiter).
          setNewLine(newLine).
          setQuotes(quotes)

        val rand = new Random(i)
        val writer = factory.getWriter(new FileOutputStream(outFileName))
        for (j <- 1 to rowMod(i)) { // redovi


          val str =
            for (k <- 1 to colMod(i)) yield {
              val str = new StringBuilder(
                rand.nextString(rand.nextInt().abs % strSize + 4))
              val l = str.length
              for (l <- 1 to numOfQuotes(i)) {
                str.insert((rand.nextInt().abs % l + 3), quotes)
              }
              str.result()

            }
          val arr = str.toArray
          //arr.foreach(println)
          writer.write(arr)

        }

        writer.close()
        val time = System.currentTimeMillis()
        val r = new Random(i)
        val f = new File(outFileName)
        info("Starting test :" + i+ "File size: " + f.length())
        val reader = factory.getReader(new FileInputStream(f))
        while (reader.hasNext()) {
          val lr = reader.next()
          lr.foreach{
            x =>
              val str = new StringBuilder(
                r.nextString(r.nextInt().abs % strSize + 4))
              val l = str.length
              for (l <- 1 to numOfQuotes(i)) {
                str.insert((r.nextInt().abs % l + 3), quotes)
              }

              //println("      >         |" + x + "|        <>        |" +str+"|        <       ")
              /* if (!(x sameElements str.result()))  {println("miss")
            }*/
              //println(x)
              //println(str.result)
              //if (x.length==31) x.foreach(y=> println("|>"+y+"<|"+ y.toInt))
              x should equal(str.result())
              /*for (z <- 0 to x.length() - 1) {
                try {
                  x(z) should equal(str.result()(z))
                } catch {
                  case e: StringIndexOutOfBoundsException =>
                    print("|"+x+"|"+x.length())
                    println(",|"+str.result+"|"+str.length())
                }

              }
            })*/
        }}

        info("                        time: " + (System.currentTimeMillis() - time))
      }
    }
  }
}
//  scenario("Inputing CSV into a line reading stream") {
//    info("")
//
//    val outFileName = "/home/marin/doit/csvs/test1.csv"
//
//val delimiter = 1234.toString()
//    val newLine = (5678 ).toString()
//    val quotes = (90 ).toString()
//    val factory = CSVFactory.factory().
//      setDelimiter(delimiter).
//      setNewLine(newLine).
//      setQuotes(quotes)
//    for( i <- 1 to 2){
//      val rand = new Random(33)
//      for (j <- 1 to (i)) { // redovi
//        val writer = factory.getWriter(new FileOutputStream(outFileName))
//        val str =
//          for (k <- 1 to i)
//            yield rand.nextString(Random.nextInt() % 15)
//            val arr = str.toArray
//            writer.write(arr)
//      writer.close()
//      }
//      val r = new Random(33)
//      val reader = factory.getReader(new FileInputStream(outFileName))
//      while(reader.hasNext()){
//        val lr = reader.next()
//        lr.foreach(x =>
//          x should equal (rand.nextString(Random.nextInt() % 15)))
//      println
//      }
//      }
//  }
//}


/*

package hr.element.doit

package test

import org.scalatest.FunSuite
import scala.collection.mutable.Stack
import org.scalatest.matchers._
import hr.element.doit.csv
import hr.element.doit.csv.SlidingMatcher._
import hr.element.doit.csv.SlidingMatcher
import org.scalatest.GivenWhenThen
import hr.element.doit.csv.CyclicCharacterMatcher
import org.scalatest.FeatureSpec

class ExampleSuite extends GivenWhenThen
                            with ShouldMatchers
                            with FeatureSpec{

//  scenario("Delimiter constraints") {
//        info("Delimiter length cannot be zero")
//
//            val matcher = SlidingMatcher("2")
//     //       matcher should be a ('SingleCharacterMatcher)
//            matcher.consume(' ') should equal (Right(Some(' ')))
//            matcher.consume('2') should equal (Left())
//        }
//  scenario(" TwoCharacterMatcher result test"){
//
//    val matcher = SlidingMatcher("2 ")
//   // matcher should be a ('TwoCharacterMatcher)
//    matcher.consume(' ') should equal (Right(None))
//    matcher.consume('1') should equal (Right(Some(' ')))
//
//    matcher.consume('2') should equal (Right(Some('1')))
//    matcher.consume(' ') should equal (Left())
//
//    matcher.consume(' ') should equal (Right(None))
//  }
//  scenario(" CyclicCharacterMatcher result test"){
////       val matcher = SlidingMatcher("123")
////
////           matcher.consume(' ') should equal (Right(None))
////           matcher.consume(' ') should equal (Right(None))
////           matcher.consume('1') should equal (Right(Some(' ')))
////           matcher.consume('2') should equal (Right(Some(' ')))
////
////
////           matcher.consume('3') should equal (Left())
////           matcher.consume(' ') should equal (Right(None))
////           matcher.consume(' ') should equal (Right(None))
////           matcher.consume('1') should equal (Right(Some(' ')))
////           matcher.consume('2') should equal (Right(Some(' ')))
//
//  }
//
//}

//import org.scalatest._
//import org.scalatest.matchers._
//
//class SlidingWindowSpec extends
//                        with GivenWhenThen
//                        with ShouldMatchers {
//
//  feature("Sliding Window Matcher") {
//
//
//      when("A CSVConfig delimiter is set")
//      then("it should thow an exception")
//
//      test("SlidingMatcher"){
//
//      }
//
//      "1" should be ("2")
//    }
//
//    scenario("pop is invoked on an empty stack") {
//
//      given("an empty stack")
//      when("when pop is invoked on the stack")
//      then("NoSuchElementException should be thrown")
//      and("the stack should still be empty")
//      pending
//    }
//  }
//}

*/