package de.grammarcraft.scala.cyclicflow

import de.grammarcraft.scala.flow.FunctionUnit
import de.grammarcraft.scala.flow.InputPort
import de.grammarcraft.scala.flow.OutputPort1
import de.grammarcraft.scala.flow.OutputPort2
import de.grammarcraft.scala.flow.ErrorPort

/**
 * @author kuniss
 *
 */
final class Cyclic extends FunctionUnit("Cyclic") 
	with InputPort[Integer]
    with OutputPort1[Integer]
    with OutputPort2[Integer]
    with ErrorPort[String]
{
  // for meaningful names on binding to context and msg forwarding
  val run = input _

  val continue = OutputPort1("continue")
  val continueWith = continue.fromInside

  val stopped = OutputPort2("stopped")
  val stopWith = stopped.fromInside

  // function unit semantic
  override protected def processInput(currentCycle: Integer) {
    try {
      if (currentCycle < 1000)
        continueWith(currentCycle + 1)
      else
        stopWith(currentCycle)
    } catch {
      case t: Throwable => 
        error <= 
          "error on Cyclic.processInput caught on cycle " + currentCycle +
          " : " + t.toString()
    }
  }
}