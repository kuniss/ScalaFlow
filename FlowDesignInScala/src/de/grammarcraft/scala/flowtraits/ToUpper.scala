package de.grammarcraft.scala.flowtraits

class ToUpper extends FunctionUnit("ToUpper") 
	with InputPort[String] 
	with OutputPort[String]
{

  protected def process(msg: String) {
    output(msg.toUpperCase())
  }

}