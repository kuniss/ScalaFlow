/*
 * Copyright 2013 Denis Kuniss kuniss@grammarcraft.de
 * 
 * This file is part of the Flow-Design Scala Library.
 * The Flow-Design Scala Library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * The Flow-Design Scala Library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with the Flow-Design Scala Library.  If not, see <http://www.gnu.org/licenses/>.
 */

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