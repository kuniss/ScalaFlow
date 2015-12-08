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
    
    /**
     * The human readable name of this trait output port.
     * May be overridden by the function unit mixing in this trait. Default is "output"
     */
    private[this] var portName = "output"
    
    private[this] var outputOperations: List[T => Unit] = List()
    
    /**
     * Lets the function unit output be processed by the given function closure.  
     */
    private[this] def outputIsProcessedBy(operation: T => Unit) {
  	  outputOperations = operation :: outputOperations
    }
  
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
    
    // Flow DSL related constructs and operators

    /**
     * Represents the function unit's (by convention) one and only output port.
     * Helper object for syntactic sugar allowing to specify connections as<br>
     * <code><i>sender.output</i> -> <i>receiver</i></code>, or<br>
     * <code><i>sender.output</i> -> <i>receiver.input</i></code>, or<br>
     * <code><i>sender.output</i> -> <i>receiver</i></code>.
     */
    val output = new dsl.OutputPort[T](outputIsProcessedBy(_), forwardOutput(_))
    
    /**
     * Flow DSL element to define user named output port which may be used instead 
     * of [[de.grammarcraft.scala.flow.OutputPort[T].output]] when connecting function unit ports.<br>
     * Typically the definition is done as follows:<br>
     * <code>val <i>myPortName</i> = OutputPort("<i>myPortName</i>")</code>.<br>
     * "OutputPort" literally corresponds to the "with OutputPort" clause at the class
     * definition header of this function unit.
     * 
     * @param userPortName the name of this port used in integration error messages; 
     * by convention the name of variable this object is assigned to should be used
     */  
    def OutputPort(userPortName: String): dsl.OutputPort[T] = {
      this.portName = userPortName
      return output
    }

    /**
     * Lets the function unit's output data flow to the given input port
     * connecting both function units.<br>
     * Flow DSL operator for connecting a function unit with one and only one output port to an 
     * arbitrary function unit with explicitly specified input port. <br>
     * E.g., <code>fu1 -> fu2.input</code>
     */
    def -> (operation: T => Unit) = outputIsProcessedBy(operation)
  		  
    /**
     * Lets the function unit's output data flow to a function unit with one and only one input port 
     * connecting both function units.<br>
     * Flow DSL operator for connecting a function unit with one and only one output port to an 
     * arbitrary function unit allowing to omit input port when specifying the flow connection. <br>
     * E.g., <code><i>sender</i> -> <i>receiver.input</i><code>
     */
    def -> (functionUnit: InputPort[T]) {
  	  outputOperations = functionUnit.input.processInputOperation :: outputOperations
    }

    /**
     * Lets output data of an integrated function unit with one and only one port flow to the 
     * integrating function unit's own output port.<br>
     * Flow DSL operator for connecting the output port of an integrated function unit
     * to the output port of the integrating function unit. E.g.
     * {{{
     * integratedFU -> output
     * }}}
     */
    def -> (ownOutputPort: dsl.OutputPort[T]) = outputIsProcessedBy(ownOutputPort.fromInside)
 
  }
  
  // Flow DSL specific operators
  package dsl {

    private[flow] class OutputPort[T](register: (T => Unit) => Unit, forward: T => Unit) {
      
      /**
       * Lets the function unit's output data flow to the given input port
       * connecting both function units.<br>
       * Flow DSL operator for connecting the function unit's output port represented by an instance of 
       * this type to an arbitrary function unit with explicitly specified input port.<br>
       * E.g., <code>sender.output -> receiver.input</code>
       */
      def -> (operation: T => Unit) = register(operation)
      
      /**
       * Lets output data of an integrated function unit flow to the integrating function
       * unit's own output port.<br>
       * Flow DSL operator for connecting the output port of an integrated function unit
       * to the output port of the integrating function unit. E.g.
       * {{{
       * integratedFU.output -> output
       * }}}
       */
      def -> (ownOutputPort: dsl.OutputPort[T]) = register(ownOutputPort.fromInside)

      private[flow] def fromInside: T => Unit = forward
      
      /**
       * Lets the function unit's output data flow to a function unit with one and only one input port 
       * connecting both function units.<br>
       * Flow DSL operator for connecting a function unit's output port to an 
       * arbitrary function unit with one and only one input port allowing to omit the receivers input 
       * ports when specifying the flow connection. <br>
       * E.g., <code>sender.output -> receiver</code>
       */
      def -> (functionUnitWithOnlyOneInputPort: de.grammarcraft.scala.flow.InputPort[T]) = register(functionUnitWithOnlyOneInputPort.input.processInputOperation(_))  
      
      /**
       * Lets the function unit's output data flow to a given function unit's input port 
       * connecting both function units.<br>
       * Flow DSL operator for connecting a function unit's output port to an 
       * arbitrary function unit's input port when specifying the flow connection. <br>
       * E.g., <code>sender.output -> receiver.input</code>
       */
      def -> (inputPort: de.grammarcraft.scala.flow.dsl.InputPort[T]) = register(inputPort.processInputOperation(_))  

      /**
       * Forwards the right hand side value to the function unit's output port given on the 
       * left side.<br>
       * Flow DSL operator for forwarding computed data to a particular output port. E.g.,<br>
       * <code><i>output</i> <= 12</code>
       */
      def <= (msg: T) = { forward(msg) }

      /**
       * Forwards the result of the application of the right hand side code block to the 
       * function unit's output port given on the left side.<br>
       * Flow DSL operator for forwarding computed data to a particular output port. E.g.,<br>
       * {{{
       * output <= {
       * 	if (stateReached) 12 else 13
       * }
       * }}}
       */      
      def <= (closure: Unit => T) = { forward(closure(())) }
      
    } 
    
  }
  
}