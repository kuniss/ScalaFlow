package de.grammarcraft.scala.flowtraits

class Reverse extends FunctionUnit("Reverse") 
	with InputPort[String] 
	with OutputPort[String] 
{
  
  protected def process(msg: String) {
    output(msg.reverse)
  }

}

