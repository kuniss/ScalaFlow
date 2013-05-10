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