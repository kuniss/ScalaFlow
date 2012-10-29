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
  
  protected def output(msg: T) {
	  if (!outputOperations.isEmpty) {
	    outputOperations.foreach(operation => operation(msg))
	  }
	  else
	    println("no output port defined for " + this + ": '" + 
	        msg + "' could not be delivered") 
  }

  

}