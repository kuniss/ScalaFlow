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
 * Represents the first output port of a function unit at flow design implementations in Scala.
 * Use this trait for function units with more than one output port.
 * This is a restriction by convention and not checked.
 * 
 * @author kuniss@grammarcraft.de
 *
 */
trait OutputPort1[T] extends FunctionUnit {
  
  private[this] var outputOperations: List[T => Unit] = List()
  
  /**
   * Lets the function unit output be processed by the given function closure.  
   */
  private def output1IsProcessedBy(operation: T => Unit) {
	  outputOperations = operation :: outputOperations
  }
  
  /**
   * Represents the function unit's first output port.
   * Helper object for syntactic sugar allowing to write connection down as
   * <i>fu.output1</i> -> <i>receiver</i>. See definition of value <i>output1</i>.
   */  
  val output1 = new de.grammarcraft.scala.flow.dsl.OutputPort[T](output1IsProcessedBy(_))
   
  /**
   * The human readable name of this trait output port.
   * May be overridden by the function unit mixing in this trait. Default is "output1"
   */
  protected val OutputPort1Name = "output1"

  /**
   * Forwards the given message over the function units first output port to 
   * the function units connected to this port.
   */
  protected def forwardOutput1(msg: T) {
	  if (!outputOperations.isEmpty) {
	    outputOperations.foreach(operation => operation(msg))
	  }
	  else
	    forwardIntegrationError("nothing connected to port " + OutputPort1Name + " for " + this + ": '" + 
	        msg + "' could not be delivered") 
  }

}