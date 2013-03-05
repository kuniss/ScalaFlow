package de.grammarcraft.scala.flow

/**
 * Represents the one and only output port of function unit at flow design implementations in Scala.
 * Use this trait for function units with only one output port.
 * This is an restriction by convention an not checked.
 * 
 * @author kuniss@grammarcraft.de
 *
 */
trait OutputPort1[T] {
  
  private[this] var outputOperations: List[T => Unit] = List()
  
  /**
   * Lets the function unit output be processed by the given function closure.  
   */
  def output1IsProcessedBy(operation: T => Unit) {
	  outputOperations = operation :: outputOperations
  }
  
  /**
   * Helper class for syntactic sugar allowing to write connection down as
   * <i>fu.output1</i> -> <i>receiver</i>. See definition of value <i>output1</i>.
   */  
  class _OutputPort1(val outputPort: OutputPort1[T]) {
	  def -> (operation: T => Unit) = outputPort.output1IsProcessedBy(operation)
	  def isProcessedBy(operation: T => Unit) = outputPort.output1IsProcessedBy(operation)
  }
  val output1 = new _OutputPort1(this)
    
  /**
   * The (by convention) one only one function unit's output port.
   */
  protected def forwardOutput1(msg: T) {
	  if (!outputOperations.isEmpty) {
	    outputOperations.foreach(operation => operation(msg))
	  }
	  else
	    println("no output port defined for " + this + ": '" + 
	        msg + "' could not be delivered") 
  }

}