package hr.element.doit.csv
package test

import org.scalatest._
import org.scalatest.matchers._
import scala.util.Random
import java.security.SecureRandom
import java.math.BigInteger
import java.io._
import java.nio.charset.Charset

class ExampleSuite2 extends GivenWhenThen
  with ShouldMatchers
  with FeatureSpec {

  scenario("Easy Test"){
    val outFileName = "test2.csv"
    val factory = CSVConfig.
        setDelimiter(",").
        setNewLine("\n").
        setQuotes("\"").
        setEncoding(Charset.forName("utf8"))

//    val oS= new FileOutputStream(outFileName)
//    val writer = factory.getWriter( oS)
//    val header = IndexedSeq("first", "second", "third")
//    val line = IndexedSeq("")

    val s = """ime,prezime,dob
omot,ageref,82
pero,marko,"""



//    writer.write(header.toArray)
//    writer.write(line.toArray)
//
//    oS.close()
//
//
    val reader = factory.getReaderWithHeaders(new ByteArrayInputStream(s.getBytes()))
      //  new FileInputStream(s))

    reader.foreach{t =>
      println(" TEST",t("ime"), t("prezime"), t("dob"))
    }

    //reader.header.map(_) should equal (Array(1,2,3))
  }

//  scenario("Easy Test"){
//    val outFileName = "test2.csv"
//    val factory = CSVConfig.
//        setDelimiter("1234").
//        setNewLine("56").
//        setQuotes("asdfgh").
//        setEncoding(Charset.forName("utf8"))
//
//    val oS= new FileOutputStream(outFileName)
//    val writer = factory.getWriter( oS)
//    val header = IndexedSeq("first", "second", "third")
//    val line = IndexedSeq("1\"25asdfgh435426546;","2656543542" ,  "gfsgfsgfsgfsd3" )
//
//
//    writer.write(header.toArray)
//    writer.write(line.toArray)
//
//    oS.close()
//
//
//    val reader = factory.getReaderWithHeaders(
//        new FileInputStream(outFileName))
//
//
//
//    //reader.header.map(_) should equal (Array(1,2,3))
//  }
//
//  scenario("Simple withHeader Test, TwoCharMatcher") {
//    val outFileName = "test1.csv"
//    val testRange = 1166 to 1167
//    val strSize = 20
//    val numOfQuotes = (i: Int) => i / 70
//    val header = IndexedSeq("first", "second", "third")
//    val rowMod = (i: Int) => (i)
//    val colMod = header.length //(i:Int )  =>(i / 60)
//    val quotes ="23"
//    val newLine="7"
//    val delimiter ="9"
//    for (i <- testRange) {
//      val t = new Random(i - 1)
//      val factory = CSVConfig.
//        setDelimiter(t.nextString(i % 2)).
//        setNewLine(t.nextString(i % 2)).
//        setQuotes(t.nextString(i % 2)).
//        setEncoding(Charset.forName("utf8"))
//
//        val oS= new FileOutputStream(outFileName)
//        val writer = factory.getWriter(oS)
//      if (valid(writer.config)) {
//
//        writer.write(header.toArray)
//        val rand = new Random(i)
//        for (j <- 1 to rowMod(i)) {
//          val str = for (k <- 1 to colMod)
//            yield getRandStr(rand, numOfQuotes(i), quotes, strSize)
//          val arr = str.toArray
//          writer.write(arr)
//        }
//        oS.close()
//        val time = System.currentTimeMillis()
//
//        val r = new Random(i)
//        val f = new File(outFileName)
//      info("Starting test :" + i + ", File size: " + f.length)
//        val reader = factory.getReaderWithHeaders(new FileInputStream(f))
//        reader.foreach{x =>
//            x.foreach {
//                _ should equal(
//                    getRandStr(r, numOfQuotes(i), quotes, strSize))
//          }
//        }
//          info("time: "+(System.currentTimeMillis() - time))
//      } else {}
//    }
//  }

  scenario("Simple Test with Headers and CyclicMatcher"){
        val outFileName = "test1.csv"
    val testRange = 566 to 567
    val strSize = 20
    val numOfQuotes = (i: Int) => i / 70
    val header = IndexedSeq("first", "second", "third")
    val rowMod = (i: Int) => (i)
    val colMod = header.length// (i:Int )  =>(i / 60)

    for (i <- testRange) {
      val t = new Random(i - 1)
      val factory = CSVConfig.
      setDelimiter(t.nextString(i / 70)).
        setNewLine(t.nextString(i / 60)).
        setQuotes(t.nextString(i / 20)).
        setEncoding(Charset.forName("utf8"))

        val oS= new FileOutputStream(outFileName)
        val writer = factory.getWriter(oS)
      if (valid(writer.config)) {

        writer.write(header.toArray)
        val rand = new Random(i)
        for (j <- 1 to rowMod(i)) {
          val str = for (k <- 1 to colMod)
            yield getRandStr(rand, numOfQuotes(i), writer.config.quotes, strSize)
          val arr = str.toArray
          writer.write(arr)
        }
        oS.close()
        val time = System.currentTimeMillis()

        val r = new Random(i)
        val f = new File(outFileName)
      info("Starting test :" + i + ", File size: " + f.length)
        val reader = factory.getReaderWithHeaders(new FileInputStream(f))
        reader.foreach{x =>
            x.foreach { chineze => 
            info(chineze)
                chineze should equal(
                    getRandStr(r, numOfQuotes(i), writer.config.quotes, strSize))
          }
        }
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

  def valid(fac: CSVConfig) = {
    if (fac.delimiter.contains(fac.quotes) ||
      fac.delimiter.contains(fac.newLine) ||
      fac.quotes.contains(fac.newLine) ||
      fac.quotes.contains(fac.delimiter) ||
      fac.newLine.contains(fac.quotes) ||
      fac.newLine.contains(fac.delimiter))
      false else true
  }
}
