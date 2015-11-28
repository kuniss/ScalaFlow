package de.grammarcraft.scala.flow.dsl

class OutputPort[T](register: (T => Unit) => Unit) {
  
  /**
   * Lets the function unit's output data flow to the given input port
   * connecting both function units.<br>
   * Flow DSL operator for connecting the function unit's output port represented by an instance of 
   * this type to an arbitrary function unit with explicitly specified input port. <br>
   * E.g., <code>sender.output -> receiver.input</code>
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
  def -> (functionUnitWithOnlyOneInputPort: de.grammarcraft.scala.flow.InputPort[T]) = register(functionUnitWithOnlyOneInputPort.input(_))  
}