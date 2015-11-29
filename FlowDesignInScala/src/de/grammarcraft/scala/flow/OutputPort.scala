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

package de.grammarcraft.scala.flow {

  /**
   * Represents the one and only output port of function unit at flow design implementations in Scala.
   * Use this trait for function units with only one output port.
   * This is a restriction by convention and not checked.
   * 
   * @author kuniss@grammarcraft.de
   *
   */
  trait OutputPort[T] extends FunctionUnit {
    
    private[this] var outputOperations: List[T => Unit] = List()
    
    /**
     * Lets the function unit's output data flow to the given input port
     * connecting both function units.<br>
     * Flow DSL operator for connecting a function unit with one and only one output port to an 
     * arbitrary function unit with explicitly specified input port. <br>
     * E.g., <code>fu1 -> fu2.input</code>
     */
    def -> (operation: T => Unit) = outputIsProcessedBy(operation)
  		  
    /**
     * Lets the function unit output be processed by the given function closure.  
     */
    private[this] def outputIsProcessedBy(operation: T => Unit) {
  	  outputOperations = operation :: outputOperations
    }
  
    /**
     * Represents the function unit's (by convention) one and only output port.
     * Helper object for syntactic sugar allowing to specify connections as
     * <i>sender.output</i> -> <i>receiver</i>, or
     * <i>sender.output</i> -> <i>receiver.input</i>, or
     * <i>sender.output</i> -> <i>receiver</i>.
     * .
     */
    val output = new dsl.OutputPort[T](outputIsProcessedBy(_), forwardOutput(_))
    
    // to void port name specification at binding
    /**
     * Lets the function unit's output data flow to a function unit with one and only one input port 
     * connecting both function units.<br>
     * Flow DSL operator for connecting a function unit with one and only one output port to an 
     * arbitrary function unit allowing to omit input port when specifying the flow connection. <br>
     * E.g., <code><i>sender</i> -> <i>receiver.input</i><code>
     */
    def -> (functionUnit: InputPort[T]) {
  	  outputOperations = functionUnit.input _ :: outputOperations
  	  //                                    ^ partially applied function
    }
      
    /**
     * Flow DSL construct to define user named output port which may be used instead 
     * of {@link #output1} when connecting function unit ports.<br>
     * Typically the definition is done as follows:<br>
     * <code>val <i>myPortName</i> = OutputPort1("<i>myPortName</i>")</code>.
     * 
     * @param userPortName the name of this port used in integration error messages; 
     * by convention the name of variable this object is assigned to should be used
     */  
    def OutputPort(userPortName: String): dsl.OutputPort[T] = {
      this.portName = userPortName
      return output
    }

    /**
     * The human readable name of this trait output port.
     * May be overridden by the function unit mixing in this trait. Default is "output"
     */
    private[this] var portName = "output"
    
    /**
     * Forwards the given message over the function units (by convention) one and only 
     * output port to the function units connected to this port.
     */
    private[this] def forwardOutput(msg: T) {
  	  if (!outputOperations.isEmpty) {
  	    outputOperations.foreach(operation => operation(msg))
  	  }
  	  else
  	    forwardIntegrationError("nothing connected to port " + portName + " for " + this + ": '" + 
  	        msg + "' could not be delivered") 
    }
    
  }
  
  package dsl {

    private[flow] class OutputPort[T](register: (T => Unit) => Unit, forward: T => Unit) {
      
      /**
       * Lets the function unit's output data flow to the given input port
       * connecting both function units or to the integrating function unit's output port.<br>
       * Flow DSL operator for connecting the function unit's output port represented by an instance of 
       * this type to an arbitrary function unit with explicitly specified input port or output port 
       * of an integrating function unit. <br>
       * E.g., <code>sender.output -> receiver.input</code>, or<br>
       * <code>integratedFU.output -> output.fromInside</code>
       */
      def -> (operation: T => Unit) = register(operation)
      
      /**
       * Lets the function unit's output data being processed by the given closure.<br>
       * Flow DSL "control structure" for specifying how function unit's output has to be processed.
       * Typically used for implementing side effects in the middle (logging) or the end of a flow (outputting result).<br>
       * E.g., <code><i>sender.output</i> isProcessyBy { msg => println("result: " + msg)</code>
       */
      def isProcessedBy(operation: T => Unit) = register(operation)
    
      /**
       * Lets the function unit's output data flow to a function unit with one and only one input port 
       * connecting both function units.<br>
       * Flow DSL operator for connecting a function unit with one and only one output port to an 
       * arbitrary function unit with one and only one input port allowing to omit both input ports when 
       * specifying the flow connection. <br>
       * E.g., <code>sender -> receiver</code>
       */
      def -> (functionUnitWithOnlyOneInputPort: InputPort[T]) = register(functionUnitWithOnlyOneInputPort.input(_))  
      
      def <= (msg: T) = { forward(msg) }
      def <= (operation: Unit => T) = { forward(operation()) }
      
      /**
       * Represents the output port of an integrating function unit seen from inside.<br>
       * This is intended to be used for connecting an integrated function unit's output port
       * to the output port of the integrating function unit forwarding the output data of the 
       * integrated function unit as result of the integrating function unit.<br>
       * This allows to use Flow DSL operator '->' for connecting function unit's output port 
       * represented by an instance of this type to the output port of the function unit integrating 
       * the function unit on the left hand side.<br>
       * E.g., <code>integratedFU.output -> output.fromInside</code>
       */
      def fromInside: T => Unit = forward
    } 
    
  }
  
}