/*
 * Copyright 2013 Denis Kuniss kuniss@grammarcraft.de
 * 
 * This file is part of the Flow-Design Scala Library.
 * The Flow-Design Scala Library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * The Flow-Design Scala Library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with the Flow-Design Scala Library.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.grammarcraft.scala.flow

/**
 * Represents the second output port of a function unit at flow design implementations in Scala.
 * Use this trait for function units with two or more output port.
 * This is a restriction by convention and not checked.
 * 
 * @author kuniss@grammarcraft.de
 *
 */
trait OutputPort2[T] extends FunctionUnit {
  
  private[this] var outputOperations: List[T => Unit] = List()
  
  /**
   * Lets the function unit output be processed by the given function closure.  
   */
  private[this] def output2IsProcessedBy(operation: T => Unit) {
	  outputOperations = operation :: outputOperations
  }

  /**
   * Represents the function unit's second output port.
   * Helper object for syntactic sugar allowing to write connection down as
   * <i>fu.output2</i> -> <i>receiver</i>. See definition of value <i>output2</i>.
   */  
  val output2 = new de.grammarcraft.scala.flow.dsl.OutputPort[T](output2IsProcessedBy(_), forwardOutput2(_))

  /**
   * Flow DSL construct to define user named output port which may be used instead 
   * of {@link #output1} when connecting function unit ports.<br>
   * Typically the definition is done as follows:<br>
   * <code>val <i>myPortName</i> = OutputPort1("<i>myPortName</i>")</code>.
   * 
   * @param userPortName the name of this port used in integration error messages; 
   * by convention the name of variable this object is assigned to should be used
   */  
  def OutputPort2(userPortName: String): dsl.OutputPort[T] = {
    this.portName = userPortName
    return output2
  }

  /**
   * The human readable name of this trait output port.
   * May be overridden by the function unit mixing in this trait. Default is "output2"
   */
  private[this] var portName = "output2"

  /**
   * Forwards the given message over the function units second output port to 
   * the function units connected to this port.
   */
  private[this] def forwardOutput2(msg: T) {
	  if (!outputOperations.isEmpty) {
	    outputOperations.foreach(operation => operation(msg))
	  }
	  else
	    forwardIntegrationError("nothing connected to port " + portName + " of function unit " + this + ": '" + 
	        msg + "' could not be delivered") 
  }

    /**
     * Represents the function unit's (by convention) second inner side output port.<br>
     * This Flow DSL element is intended to be used for integrating function units to forward computation
     * results from integrated function unit output ports to the integrating function unit
     * output port. It's the so speaking inside visible outside port. The outside visible 
     * output port {link #output2} can not be used here.<br>
     * This will allow to specify output data forwarding as<br> 
     * <i>integratedFU.output</i> -> <i>_output2</i>, or<br>
     * <i>intergatedFU.output</i> -> <i>_output2</i>.
     */
    protected val _output2 = forwardOutput2(_)
}