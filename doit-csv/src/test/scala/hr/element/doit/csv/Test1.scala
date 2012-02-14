package hr.element.doit

package hr.element.doit.csv

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
import _root_.hr.element.doit.csv.CSVReader
import _root_.hr.element.doit.csv.CSVFactory

class ExampleSuite extends GivenWhenThen
                            with ShouldMatchers
                            with FeatureSpec{

//  scenario("SlidingMatch test"){
//    info("")
//    val ssm = SlidingMatcher("q");
//
//    ssm.consume('q') should be (Delimiter)
//  }


  scenario("Inputing CSV into a line reading stream"){
    info("")

    val fileName = "/home/marin/doit/csvs/test1.csv"
    val rder: CSVReader = CSVFactory.factory().setNewLine("sss").getReader(new FileInputStream(fileName));



    val it = rder.iterator()

    while(it.hasNext()){
            println
            println("TEST>")
      val lr = it.next()
         lr.foreach(x => print(">"+ x + "<"))

    }
    println


    }

}


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