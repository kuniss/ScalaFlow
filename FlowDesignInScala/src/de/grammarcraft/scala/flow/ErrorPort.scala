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
 * Represents the programmatic error port at flow design implementations in Scala.
 * Use this trait for function units with an programmatic error port.
 * 
 * @author kuniss@grammarcraft.de
 *
 */
trait ErrorPort[T] extends FunctionUnit { port =>
  
  private[this] var errorOperations: List[T => Unit] = List()
 
  /**
   * Lets the function unit output be processed by the given function closure.  
   */
  private[this] def errorIsProcessedBy(operation: T => Unit) {
	  errorOperations = operation :: errorOperations
  }
  
  /**
   * Helper object for syntactic sugar allowing to write connection down as
   * <i>fu.error</i> -> <i>receiver</i>.
   */
  val error = new de.grammarcraft.scala.flow.dsl.OutputPort[T](errorIsProcessedBy(_), forwardError(_))  

  /**
   * The function unit's error port.
   */
  private[this] def forwardError(exception: T) {
	  if (!errorOperations.isEmpty) {
		  errorOperations.foreach(forward => forward(exception))
	  }
	  else {
		  forwardIntegrationError("no binding defined for error port of " + this)
	  }
  }

}