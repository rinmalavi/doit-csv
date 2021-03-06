package hr.element.doit.csv

sealed trait Smr
case object Delimiter extends Smr
case object Quote extends Smr
case object NewLine extends Smr
case object Cooldown extends Smr
case class  ReadCh(c: Char) extends Smr

sealed trait ModeCase
case object Ignore extends ModeCase
case object Unexpected extends ModeCase
case object Watch extends ModeCase
