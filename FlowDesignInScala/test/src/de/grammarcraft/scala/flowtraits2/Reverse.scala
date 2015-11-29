package de.grammarcraft.scala.flowtraits2

import de.grammarcraft.scala.flow.InputPort
import de.grammarcraft.scala.flow.OutputPort
import de.grammarcraft.scala.flow.FunctionUnit

final class Reverse extends FunctionUnit("Reverse") 
	with InputPort[String] 
	with OutputPort[String] 
{
  override protected val OutputPortName = "reversedString"
  
  protected def processInput(msg: String) {
    output <= msg.reverse
  }

}

