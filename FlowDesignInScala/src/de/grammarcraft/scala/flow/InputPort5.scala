package de.grammarcraft.scala.flow

/**
 * Represents the fifth input port for a function unit with multiple input ports 
 * at flow design implementations in Scala.
 * Use this trait for function units with 5 or more ports. 
 * This is an restriction by convention and not checked.
 * 
 * @author kuniss@grammarcraft.de
 */
trait InputPort5[T] {

  /**
   * Implements how input messages received at port <i>input5</i> are processed.
   * Must be implemented at the function unit applying this trait.
   */
  protected def processInput5(msg: T): Unit

  /**
   * The function unit's fifth input port.
   */  
  def input5(msg: T) {
	  processInput5(msg)
  }

}