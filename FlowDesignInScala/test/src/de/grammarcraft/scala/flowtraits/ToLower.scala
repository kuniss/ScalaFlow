package de.grammarcraft.scala.flowtraits

class ToLower extends FunctionUnit("ToLower") 
	with InputPort[String] 
	with OutputPort[String]
	with ErrorPort[String]
{

  protected def processInput(msg: String) {
    output(msg.toLowerCase())
//    error("artifical error for testing purposes; '" + 
//        msg + "' will not be processed")
  }

}
