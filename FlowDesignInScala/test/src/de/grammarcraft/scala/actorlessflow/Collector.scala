package de.grammarcraft.scala.actorlessflow

class Collector(val separator: String) {
  
  def input1(msg: String) {
	  accumulateInput(msg)
  }
  
  def input2(msg: String) {
	  accumulateInput(msg)
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

  private[this] var accumulation: List[String] = List()
		  
  private def accumulateInput(msg: String) {
	  accumulation = msg :: accumulation
	  if (accumulation.length == 2) output(accumulation mkString(separator))
  }
  
  
  override def toString:String = "Collector"
  
}
