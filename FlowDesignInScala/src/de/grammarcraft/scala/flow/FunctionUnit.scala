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

  private[this] var integrationErrorOperations: List[String => Unit] = List()

  /**
   * Method for registration of integration error continuations at the companion object;
   * see below.
   */
  private def onIntegrationError(integrationErrorOperation: String => Unit) {
	  integrationErrorOperations = integrationErrorOperation :: integrationErrorOperations
  }
  
  /**
   * The function unit's integration error port.
   * Used in case a message cannot be forwarded in case no receiving function 
   * unit has been registered.
   */
  protected def forwardIntegrationError(errorMsg: String) {
	  if (!integrationErrorOperations.isEmpty) {
		  integrationErrorOperations.foreach(forward => forward(errorMsg))
	  }
	  else {
		  System.err.println(this + " has an integration error: " + errorMsg)
	  }
  }

}

final object FunctionUnit {
  
  /**
   * Currying enabled method for binding the same integration error handling to 
   * several function units at one
   */
  def onIntegrationErrorAt (functionUnitList: FunctionUnit*) (integrationErrorOperation: String => Unit) {
	  functionUnitList.foreach( functionUnit => functionUnit.onIntegrationError(integrationErrorOperation) )
  }
  
  /**
   * Currying enabled method for binding at once the same error handling to 
   * several function units with the same typed error port
   */
  def onErrorAt[ErrorType] (functionUnitWithErrorPortList: ErrorPort[ErrorType]*) (errorOperation: ErrorType => Unit) {
	  functionUnitWithErrorPortList.foreach( functionUnit => functionUnit.error isProcessedBy(errorOperation) )
  }
  
}