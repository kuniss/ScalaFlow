package de.grammarcraft.scala.flowtraits

/**
 * @author denis.kuniss
 *
 */
trait OutputPort[T] {
  
  private[this] var outputOperations: List[T => Unit] = List()
  
  def bindOutputTo(operation: T => Unit) {
	  outputOperations = operation :: outputOperations
  }

  def -> (operation: T => Unit) = bindOutputTo(operation)
  
  // to void port name specification at binding
  def bindOutputTo(functionUnit: InputPort[T]) {
	  outputOperations = functionUnit.input _ :: outputOperations
	  //                                    ^ partially applied function
  }
  
  def -> (functionUnit: InputPort[T]) = bindOutputTo(functionUnit)
    
  protected def output(msg: T) {
	  if (!outputOperations.isEmpty) {
	    outputOperations.foreach(operation => operation(msg))
	  }
	  else
	    println("no output port defined for " + this + ": '" + 
	        msg + "' could not be delivered") 
  }

  

}