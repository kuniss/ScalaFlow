package de.grammarcraft.scala.flow

/**
 * Represents the one and only output port of function unit at flow design implementations in Scala.
 * Use this trait for function units with only one output port.
 * This is an restriction by convention an not checked.
 * 
 * @author kuniss@grammarcraft.de
 *
 */
trait OutputPort2[T] {
  
  private[this] var outputOperations: List[T => Unit] = List()
  
  /**
   * Lets the function unit output be processed by the given function closure.  
   */
  def output2IsProcessedBy(operation: T => Unit) {
	  outputOperations = operation :: outputOperations
  }

  /**
   * Helper class for syntactic sugar allowing to write connection down as
   * <i>fu.output2</i> -> <i>receiver</i>. See definition of value <i>output2</i>.
   */  
  class _OutputPort2(val outputPort: OutputPort2[T]) {
	  def -> (operation: T => Unit) = outputPort.output2IsProcessedBy(operation)
	  def isProcessedBy(operation: T => Unit) = outputPort.output2IsProcessedBy(operation)
  }
  val output2 = new _OutputPort2(this)

  /**
   * The (by convention) one only one function unit's output port.
   */
  def forwardOutput2(msg: T) {
	  if (!outputOperations.isEmpty) {
	    outputOperations.foreach(operation => operation(msg))
	  }
	  else
	    println("no output port defined for " + this + ": '" + 
	        msg + "' could not be delivered") 
  }

}