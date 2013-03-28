package de.grammarcraft.scala.flow

/**
 * Represents the programmatic error port at flow design implementations in Scala.
 * Use this trait for function units with an programmatic error port.
 * 
 * @author kuniss@grammarcraft.de
 *
 */
trait ErrorPort[T] extends FunctionUnit { port =>
  
  private[this] var errorOperations: List[T => Unit] = List()
 
  /**
   * Lets the function unit output be processed by the given function closure.  
   */
  private def errorIsProcessedBy(operation: T => Unit) {
	  errorOperations = operation :: errorOperations
  }
  
  /**
   * Helper object for syntactic sugar allowing to write connection down as
   * <i>fu.error</i> -> <i>receiver</i>.
   */
  val error = new Object {
	  def -> (operation: T => Unit) = port.errorIsProcessedBy(operation)
	  def isProcessedBy(operation: T => Unit) = port.errorIsProcessedBy(operation)
  }  

  /**
   * The function unit's error port.
   */
  protected def forwardError(exception: T) {
	  if (!errorOperations.isEmpty) {
		  errorOperations.foreach(forward => forward(exception))
	  }
	  else {
		  forwardIntegrationError("no binding defined for error port of " + this)
	  }
  }

}