package de.grammarcraft.scala.flow

/**
 * Represents the second input port for a function unit with multiple input ports 
 * at flow design implementations in Scala.
 * Use this trait for function units with two or more ports. 
 * This is an restriction by convention and not checked.
 * 
 * @author kuniss@grammarcraft.de
 */
trait InputPort2[T] {

  /**
   * Implements how input messages received at port <i>input2</i> are processed.
   * Must be implemented at the function unit applying this trait.
   */
  protected def processInput2(msg: T): Unit

  /**
   * The function unit's second input port.
   */  
  def input2(msg: T) {
	  processInput2(msg)
  }

}