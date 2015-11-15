package de.grammarcraft.scala.flowtraits

class ToUpper extends FunctionUnit("ToUpper") 
	with InputPort[String] 
	with OutputPort[String]
{

  protected def processInput(msg: String) {
    output(msg.toUpperCase())
  }

}