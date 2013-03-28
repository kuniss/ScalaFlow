package de.grammarcraft.scala.flow

/**
 * Represents the one and only output port of function unit at flow design implementations in Scala.
 * Use this trait for function units with only one output port.
 * This is an restriction by convention an not checked.
 * 
 * @author kuniss@grammarcraft.de
 *
 */
trait OutputPort[T] extends FunctionUnit { port =>
  
  private[this] var outputOperations: List[T => Unit] = List()
  
  /**
   * Lets the function unit output flow to the given input port
   * connecting both function units.
   */
  def -> (operation: T => Unit) = outputIsProcessedBy(operation)
		  
  /**
   * Lets the function unit output be processed by the given function closure.  
   */
  private def outputIsProcessedBy(operation: T => Unit) {
	  outputOperations = operation :: outputOperations
  }

  /**
   * Represents the function unit's (by convention) one and only output port.
   * Helper object for syntactic sugar allowing to write connection down as
   * <i>fu.output</i> -> <i>receiver</i>. See definition of value <i>output</i>.
   */
  val output = new Object {
	  def -> (operation: T => Unit) = port.outputIsProcessedBy(operation)
	  def isProcessedBy(operation: T => Unit) = port.outputIsProcessedBy(operation)
  }
  
  // to void port name specification at binding
  /**
   * Lets the function unit output flow to the given one and only input port of the given
   * function unit connecting both function units.
   */
  def -> (functionUnit: InputPort[T]) {
	  outputOperations = functionUnit.input _ :: outputOperations
	  //                                    ^ partially applied function
  }
    
  /**
   * The human readable name of this trait output port.
   * May be overridden by the function unit mixing in this trait. Default is "output"
   */
  protected val OutputPortName = "output"
  
  /**
   * Forwards the given message over the function units (by convention) one and only 
   * output port to the function units connected to this port.
   */
  protected def forwardOutput(msg: T) {
	  if (!outputOperations.isEmpty) {
	    outputOperations.foreach(operation => operation(msg))
	  }
	  else
	    forwardIntegrationError("nothing connected to port " + OutputPortName + " for " + this + ": '" + 
	        msg + "' could not be delivered") 
  }

  

}