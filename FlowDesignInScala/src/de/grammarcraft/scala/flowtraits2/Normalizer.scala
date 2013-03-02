package de.grammarcraft.scala.flowtraits2

import de.grammarcraft.scala.flow.FunctionUnit
import de.grammarcraft.scala.flow.InputPort
import de.grammarcraft.scala.flow.OutputPort1
import de.grammarcraft.scala.flow.OutputPort2

/**
 * @author kuniss
 *
 */
final class Normalizer extends FunctionUnit("Normalizer") 
	with InputPort[String]
    with OutputPort1[String]
    with OutputPort2[String]
{
 
	val toLower = new ToLower
	val toUpper = new ToUpper

	// bind 
	toLower -> forwardOutput1 _
	toUpper -> forwardOutput2 _

	// for meaningful names on binding to context
	val lower = output1
	val upper = output2

	protected def processInput(msg: String) {
		toLower.input(msg)
		toUpper.input(msg)
	}
  
}