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
   * Represents the one and only input port for a function unit at flow design implementations in Scala.
   * Use this trait for function units with only one input port.
   * This is an restriction by convention an not checked.
   * 
   * @author kuniss@grammarcraft.de
   */
  trait InputPort[T] {
  
    /**
     * Implements how input messages received at port <i>input</i> are processed.
     * Must be implemented at the function unit applying this trait.
     */
    protected def processInput(msg: T): Unit

    /**
     * Helper method with different visibility than {@link #processInput(T)}, used 
     * in {@link OutputPort#->(InputPort[T])} for registering it as output operation.
     */
    private[flow] def _processInput(msg: T) {
  	  processInput(msg)
    }

    // Flow DSL related constructs and operators

    /**
     * The (by convention) function unit's one and only input port.<br>
     * Allows to forward input data to the function unit this port belongs to. E.g.,<br>
     * <code><i>receiver.input</i> <= 13</code>, or
     * <code><i>sender.output</i> <= { compute() }</code>, or
     */
    val input = new dsl.InputPort[T](processInput(_))
    
    /**
     * Flow DSL construct to define user named input port which may be used instead 
     * of {@link #input1} when forwarding input data function unit input ports.<br>
     * Typically the definition is done as follows:<br>
     * <code>val <i>myPortName</i> = InputPort</code>.<br>
     * "InputPort" literally corresponds  to the "with InputPort" clause at the class
     * definition header of this function unit.
     */
    val InputPort = input
    
  }

  // Flow DSL specific operators
  package dsl {
    
    private[flow] class InputPort[T](processInput: T => Unit) {
      /**
       * Forwards the given value to the input port on the left hand side.<br>
       * Flow DSL operator for forwarding a value into an input port to be processed
       * by the function unit the input port belongs to.<br>
       * E.g., <code>receiver.input <= 13</code>
       */
      def <= (msg: T) = processInput(msg)
      
      /**
       * Forwards the given value computed by the given code block to the input port 
       * on the left hand side.<br>
       * Flow DSL operator for forwarding a value into an input port to be processed
       * by the function unit the input port belongs to.<br>
       * E.g., <pre>receiver.input <= {
       *   if (stateReached) 13 else 12
       * }
       * </pre>
       */
      def <= (closure: Unit => T) = processInput(closure())
    }
    
  }
  
}