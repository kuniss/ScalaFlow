package de.grammarcraft.scala.flowtraits2

import de.grammarcraft.scala.flow.InputPort
import de.grammarcraft.scala.flow.OutputPort
import de.grammarcraft.scala.flow.FunctionUnit

class ToUpper extends FunctionUnit("ToUpper") 
	with InputPort[String] 
	with OutputPort[String]
{

  protected def processInput(msg: String) {
    output <= msg.toUpperCase()
  }

}