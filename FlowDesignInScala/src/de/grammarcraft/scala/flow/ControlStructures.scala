package de.grammarcraft.scala.flow

object ControlStructures {

  /**
   * Flow DSl control structure allowing to specify in a code block what should be done with 
   * output data of the given function unit's output port. 
   * Typically used to implement side effects like logging or displaying results.<br>
   * E.g., <code>
   * on(<i>fu.output</i>) { 
   *  	msg => println("result: " + msg)
   * }
   * </code>
   */
  def on[T](outputPort: dsl.OutputPort[T])(operation: T => Unit) { outputPort -> operation }
  
  /**
   * Flow DSl control structure allowing to specify in a code block what should be done for
   * a set of given function units on an intergation error.<br>
   * E.g., <code>
   * onIntegrationError(<i>fu1, fu2, fu3</i>) { 
   *  	msg => println("integration error happened: " + msg)
   * }
   * </code>
   */
  def onIntegrationErrorAt (functionUnitList: FunctionUnit*) (integrationErrorOperation: String => Unit) {
	  functionUnitList.foreach( functionUnit => functionUnit.onIntegrationError(integrationErrorOperation) )
  }
  
  /**
   * Flow DSl control structure allowing to specify in a code block what should be done for
   * a set of given error ports if an error message is arriving through on of these error ports.<br>
   * E.g., <code>
   * onError(<i>fu1, fu2, fu3</i>) { 
   *  	msg => println("error happened: " + msg)
   * }
   * </code>
   */
  def onErrorAt[ErrorType] (functionUnitWithErrorPortList: ErrorPort[ErrorType]*) (errorOperation: ErrorType => Unit) {
	  functionUnitWithErrorPortList.foreach { functionUnit => 
	    on(functionUnit.error) {  
	      errorOperation 
	    } 
	  }
  }

}