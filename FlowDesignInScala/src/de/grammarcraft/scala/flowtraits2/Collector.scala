package de.grammarcraft.scala.flowtraits2

import de.grammarcraft.scala.flow.FunctionUnit
import de.grammarcraft.scala.flow.InputPort1
import de.grammarcraft.scala.flow.InputPort2
import de.grammarcraft.scala.flow.OutputPort

final class Collector(val separator: String) extends FunctionUnit("Collector") 
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
  
  // give ports meaningful names
  val lower = input1 _
  val upper = input2 _
  
  private[this] var accumulation: List[String] = List()
		  
  private def accumulateInput(msg: String) {
	  accumulation = msg :: accumulation
	  if (accumulation.length == 2) forwardOutput(accumulation mkString(separator))
  }

}
