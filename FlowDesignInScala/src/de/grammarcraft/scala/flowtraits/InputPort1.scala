package de.grammarcraft.scala.flowtraits

/**
 * @author denis.kuniss
 *
 */
trait InputPort1[T] {

  protected def processInput1(msg: T): Unit
  
  def input1(msg: T) {
	  processInput1(msg)
  }

}