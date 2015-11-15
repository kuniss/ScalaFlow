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

package de.grammarcraft.scala.flowtraits

/**
 * @author denis.kuniss
 *
 */
trait OutputPort[T] {
  
  private[this] var outputOperations: List[T => Unit] = List()
  
  def bindOutputTo(operation: T => Unit) {
	  outputOperations = operation :: outputOperations
  }

  def -> (operation: T => Unit) = bindOutputTo(operation)
  
  // to void port name specification at binding
  def bindOutputTo(functionUnit: InputPort[T]) {
	  outputOperations = functionUnit.input _ :: outputOperations
	  //                                    ^ partially applied function
  }
  
  def -> (functionUnit: InputPort[T]) = bindOutputTo(functionUnit)
    
  protected def output(msg: T) {
	  if (!outputOperations.isEmpty) {
	    outputOperations.foreach(operation => operation(msg))
	  }
	  else
	    println("no output port defined for " + this + ": '" + 
	        msg + "' could not be delivered") 
  }

  

}