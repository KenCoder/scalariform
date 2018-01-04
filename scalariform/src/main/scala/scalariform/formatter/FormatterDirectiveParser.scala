package scalariform.formatter

import scala.util.parsing.combinator._

class FormatterDirectiveParser extends JavaTokenParsers {

  val plusOrMinus: Parser[Boolean] = "+" ^^^ true | "-" ^^^ false

  val toggle: Parser[ToggleOption] = plusOrMinus ~ ident ^^ { case onOrOff ~ optionName ⇒ ToggleOption(onOrOff, optionName) }

  val directives: Parser[List[FormatterDirective]] = "format:" ~>
    ("ON" ^^^ List(ToggleFormatting(true)) | "OFF" ^^^ List(ToggleFormatting(false)) | repsep(toggle, ","))

  def getDirectives(s: String): List[FormatterDirective] = parse(directives, s) getOrElse Nil
}

object FormatterDirectiveParser {
  def getDirectives(s: String): List[FormatterDirective] = {
    val index = s indexOf "format:"
    if (index == -1)
      Nil
    else
      new FormatterDirectiveParser().getDirectives(s.substring(index))
  }
}

sealed trait FormatterDirective

case class ToggleOption(onOrOff: Boolean, optionName: String) extends FormatterDirective
case class ToggleFormatting(onOrOff: Boolean) extends FormatterDirective
