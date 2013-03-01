package de.grammarcraft.scala.flowtraits

/**
 * @author denis.kuniss
 *
 */
trait InputPort[T] {

  protected def processInput(msg: T): Unit
  
  def input(msg: T) {
	  processInput(msg)
  }

}