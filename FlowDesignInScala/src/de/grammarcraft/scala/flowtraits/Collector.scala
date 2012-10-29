package de.grammarcraft.scala.flowtraits

class Collector(val separator: String) extends FunctionUnit("Collector") 
	with InputPort1[String] 
	with InputPort2[String] 
	with OutputPort[String]
{
  
  protected def processInput1(msg: String) {
    accumulateInput(msg)
  }
  
  protected def processInput2(msg: String) {
    accumulateInput(msg)
  }
  
  private[this] var accumulation: List[String] = List()
		  
  private def accumulateInput(msg: String) {
	  accumulation = msg :: accumulation
	  if (accumulation.length == 2) output(accumulation mkString(separator))
  }

}
