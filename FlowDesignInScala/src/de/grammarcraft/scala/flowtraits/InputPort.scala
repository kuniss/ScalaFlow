package de.grammarcraft.scala.flowtraits

/**
 * @author denis.kuniss
 *
 */
trait InputPort[T] {

  protected def process(msg: T): Unit
  
  def input(msg: T) {
	  process(msg)
  }

}