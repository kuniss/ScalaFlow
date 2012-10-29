package de.grammarcraft.scala.flowtraits

/**
 * @author denis.kuniss
 *
 */
trait InputPort2[T] {

  protected def processInput2(msg: T): Unit
  
  def input2(msg: T) {
	  processInput2(msg)
  }

}