package de.grammarcraft.scala.flow

/**
 * Represents the third input port for a function unit with multiple input ports 
 * at flow design implementations in Scala.
 * Use this trait for function units with 3 or more ports. 
 * This is an restriction by convention and not checked.
 * 
 * @author kuniss@grammarcraft.de
 */
trait InputPort3[T] {

  /**
   * Implements how input messages received at port <i>input3</i> are processed.
   * Must be implemented at the function unit applying this trait.
   */
  protected def processInput3(msg: T): Unit

  /**
   * The function unit's third input port.
   */  
  def input3(msg: T) {
	  processInput3(msg)
  }

}