package de.grammarcraft.scala.flow

/**
 * Represents the fourth input port for a function unit with multiple input ports 
 * at flow design implementations in Scala.
 * Use this trait for function units with 4 or more ports. 
 * This is an restriction by convention and not checked.
 * 
 * @author kuniss@grammarcraft.de
 */
trait InputPort4[T] {

  /**
   * Implements how input messages received at port <i>input4</i> are processed.
   * Must be implemented at the function unit applying this trait.
   */
  protected def processInput4(msg: T): Unit

  /**
   * The function unit's fourth input port.
   */  
  def input4(msg: T) {
	  processInput4(msg)
  }

}