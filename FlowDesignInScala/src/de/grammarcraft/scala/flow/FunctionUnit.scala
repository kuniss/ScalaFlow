package de.grammarcraft.scala.flow

/**
 * Represents a flow design function unit.
 * Each function unit should derive from this class
 * 
 * @author kuniss@grammarcraft.de
 *
 */
abstract class FunctionUnit(val name: String) {
  
  override def toString:String = name

  private[this] var errorOperations: List[String => Unit] = List()

  /**
   * The function unit's integration error port.
   * Used in case a message cannot be forwarded in case no receiving function 
   * unit has been registered.
   */
  protected def forwardIntegrationError(errorMsg: String) {
	  if (!errorOperations.isEmpty) {
		  errorOperations.foreach(forward => forward(errorMsg))
	  }
	  else {
		  System.err.println(this + " has an integration error: " + errorMsg)
	  }
  }

}