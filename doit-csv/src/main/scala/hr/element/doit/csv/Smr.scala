package hr.element.doit.csv

sealed trait Smr

case object Delimiter extends Smr

case object Quote extends Smr

case object NewLine extends Smr

case object Cooldown extends Smr

case class  Ch3(c: Char) extends Smr


sealed trait ModeCase
case object Ignore extends ModeCase
case object Unexpected extends ModeCase
case object Watch extends ModeCase



//sealed trait SmrModet
//
//case object VerboseMode extends  SmrModet
//case object StartMode extends   SmrModet
//case object QuotedMode extends  SmrModet
//case object EscapeMode extends  SmrModet
//case object EndMode extends  SmrModet

//import LineReader._
//sealed trait SmrModet
//
//case object startMode extends SmrModet {
//  def apply(x: Smr): SmrMode = (_: Smr) => Watch
//}
//
//case object verboseMode extends  SmrModet {
//  def apply(x: Smr){
//    x match {
//      case Delimiter => Watch
//      case Quote => Unexpected
//      case NewLine => Watch
//      case Ch3(x) => Watch
//      case _ => Unexpected
//    }
//  }
//  val escapeMode: SmrMode ={
//      _ match {
//        case Delimiter => Watch
//        case Quote => Watch
//        case NewLine => Watch
//        case Ch3(x) => Unexpected
//        case _ => Unexpected
//      }
//  }
//  val quotedMode: SmrMode = {
//    _ match {
//      case Delimiter => Ignore
//      case Quote => Watch
//      case NewLine => Ignore
//      case Ch3(x) => Watch
//      case _ => Unexpected
//    }
//  }
//  val EndMode: SmrMode = (_: Smr) => Unexpected
