package de.grammarcraft.scala.flow

/**
 * Represents the one and only output port of function unit at flow design implementations in Scala.
 * Use this trait for function units with only one output port.
 * This is an restriction by convention an not checked.
 * 
 * @author kuniss@grammarcraft.de
 *
 */
trait OutputPort1[T] { port =>
  
  private[this] var outputOperations: List[T => Unit] = List()
  
  /**
   * Lets the function unit output be processed by the given function closure.  
   */
  private def output1IsProcessedBy(operation: T => Unit) {
	  outputOperations = operation :: outputOperations
  }
  
  /**
   * Helper object for syntactic sugar allowing to write connection down as
   * <i>fu.output1</i> -> <i>receiver</i>. See definition of value <i>output1</i>.
   */  
  val output1 = new Object {
	  def -> (operation: T => Unit) = port.output1IsProcessedBy(operation)
	  def isProcessedBy(operation: T => Unit) = port.output1IsProcessedBy(operation)
  }
    
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