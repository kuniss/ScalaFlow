package de.grammarcraft.scala.flowtraits2

import de.grammarcraft.scala.flow.InputPort
import de.grammarcraft.scala.flow.OutputPort
import de.grammarcraft.scala.flow.FunctionUnit

final class Reverse extends FunctionUnit("Reverse") 
	with InputPort[String] 
	with OutputPort[String] 
{
  val reversedString = OutputPort("reversedString")
  
  protected def processInput(msg: String) {
    reversedString <= msg.reverse
  }

}

