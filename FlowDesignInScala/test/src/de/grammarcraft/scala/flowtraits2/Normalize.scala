package de.grammarcraft.scala.flowtraits2

import de.grammarcraft.scala.flow.FunctionUnit
import de.grammarcraft.scala.flow.InputPort
import de.grammarcraft.scala.flow.OutputPort1
import de.grammarcraft.scala.flow.OutputPort2

/**
 * @author kuniss
 *
 */
final class Normalize extends FunctionUnit("Normalize") 
	with InputPort[String]
    with OutputPort1[String]
    with OutputPort2[String]
{
 
	val toLower = new ToLower
	val toUpper = new ToUpper

	// bind 
	toLower.output -> _output1
	toUpper.output -> _output2

	// for meaningful names on binding to context
	val lower = OutputPort1("lower")
	val upper = OutputPort2("upper")

	protected def processInput(msg: String) {
		toLower.input(msg)
		toUpper.input(msg)
	}
  
}