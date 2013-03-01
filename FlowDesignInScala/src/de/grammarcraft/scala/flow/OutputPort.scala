package de.grammarcraft.scala.flow

/**
 * Represents the one and only output port of function unit at flow design implementations in Scala.
 * Use this trait for function units with only one output port.
 * This is an restriction by convention an not checked.
 * 
 * @author kuniss@grammarcraft.de
 *
 */
trait OutputPort[T] {
  
  private[this] var outputOperations: List[T => Unit] = List()
  
  /**
   * Lets the function unit output flow to the given input port and
   * in that way connects both function units.
   */
  def -> (operation: T => Unit) = letOutputBeProcessedBy(operation)
		  
  /**
   * Lets the function unit output be processed by the given function closure.  
   */
  def letOutputBeProcessedBy(operation: T => Unit) {
	  outputOperations = operation :: outputOperations
  }

  // to void port name specification at binding
  /**
   * Lets the function unit output flow to the given one and only input port of the given
   * function unit and in that way connects both function units.
   */
  def -> (functionUnit: InputPort[T]) {
	  outputOperations = functionUnit.input _ :: outputOperations
	  //                                    ^ partially applied function
  }
    
  
  /**
   * The (by convention) one only one function unit's output port.
   */
  def output(msg: T) {
	  if (!outputOperations.isEmpty) {
	    outputOperations.foreach(operation => operation(msg))
	  }
	  else
	    println("no output port defined for " + this + ": '" + 
	        msg + "' could not be delivered") 
  }

  

}