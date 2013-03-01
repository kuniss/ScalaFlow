package de.grammarcraft.scala.flow

/**
 * Represents a flow design function unit.
 * Each function unit should derive from this class
 * 
 * @author kuniss@grammarcraft.de
 *
 */
abstract class FunctionUnit(val name: String) {
  
  override def toString:String = name

}