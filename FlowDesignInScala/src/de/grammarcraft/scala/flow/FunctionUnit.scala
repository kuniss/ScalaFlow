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
 * Represents a flow design function unit.
 * Each function unit should derive from this class
 * 
 * @author kuniss@grammarcraft.de
 *
 */
abstract class FunctionUnit(val name: String) {
  
  override def toString:String = name

  private[this] var integrationErrorOperations: List[String => Unit] = List()

  /**
   * Method for registration of integration error continuations at the companion object;
   * see below.
   */
  private[flow] def onIntegrationError(integrationErrorOperation: String => Unit) {
	  integrationErrorOperations = integrationErrorOperation :: integrationErrorOperations
  }
  
  /**
   * The function unit's integration error port.
   * Used in case a message cannot be forwarded as no receiving function 
   * unit has been registered.
   */
  protected def forwardIntegrationError(errorMsg: String) {
	  if (!integrationErrorOperations.isEmpty) {
		  integrationErrorOperations.foreach(forward => forward(errorMsg))
	  }
	  else {
		  System.err.println(this + " has an integration error: " + errorMsg) // at default print to stderr if no continuation is registered
	  }
  }

}
