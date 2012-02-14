package hr.element.doit.csv.test

import org.scalatest._
import org.scalatest.matchers._

import java.io._

class BufferedReaderSmr(reader: Reader) extends BufferedReader(reader) {
}