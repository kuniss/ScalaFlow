package de.grammarcraft.scala.actorlessflow

class ToUpper {
  
  def input(msg: String) {
    process(msg)
  }

  private[this] var outputOperations: List[String => Unit] = List()
  
  def bindOutputTo(operation: String => Unit) {
	  outputOperations = operation :: outputOperations
  }
  
  private def output(msg: String) {
	  if (!outputOperations.isEmpty) {
	    outputOperations.foreach(operation => operation(msg))
	  }
	  else {
	    error(new RuntimeException("no binding defined for output of " + this + ": '" + 
	        msg + "' could not be delivered"))
	  }
  }

  
  private[this] var errorOperations: List[Exception => Unit] = List()
  
  def bindErrorTo(operation: Exception => Unit) {
	  errorOperations = operation :: errorOperations
  }
  
  private def error(exception: Exception) {
	  if (!errorOperations.isEmpty) {
		  errorOperations.foreach(forward => forward(exception))
	  }
	  else {
		  println("no binding defined for error of " + this)
	  }
  }

  
  private def process(msg: String) {
    output(msg.toUpperCase())
  }
  
  override def toString:String = "ToUpper"

}
