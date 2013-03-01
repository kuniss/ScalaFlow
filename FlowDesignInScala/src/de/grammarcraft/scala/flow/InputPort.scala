package de.grammarcraft.scala.flow

/**
 * Represents the one and only input port for a function unit at flow design implementations in Scala.
 * Use this trait for function units with only one input port.
 * This is an restriction by convention an not checked.
 * 
 * @author kuniss@grammarcraft.de
 */
trait InputPort[T] {

  /**
   * Implements how input messages received at port <i>input</i> are processed.
   * Must be implemented at the function unit applying this trait.
   */
  protected def processInput(msg: T): Unit
  
  /**
   * The (by convention) one only one function unit's input port.
   */
  def input(msg: T) {
	  processInput(msg)
  }

}