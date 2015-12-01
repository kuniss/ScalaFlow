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
 * Represents the fourth input port for a function unit with multiple input ports 
 * at flow design implementations in Scala.
 * Use this trait for function units with 4 or more ports. 
 * This is an restriction by convention and not checked.
 * 
 * @author kuniss@grammarcraft.de
 */
trait InputPort4[T] {

  /**
   * Implements how input messages received at port <i>input4</i> are processed.
   * Must be implemented at the function unit applying this trait.
   */
  protected def processInput4(msg: T): Unit

  /**
   * The function unit's second input port.<br>
   * Allows to forward input data to the function unit this port belongs to. E.g.,<br>
   * <code><i>receiver.input4</i> <= 13</code>, or
   * <code><i>receiver.input4</i> <= { compute() }</code>, or
   */
  val input4 = new dsl.InputPort[T](processInput4(_))
  
  /**
   * Flow DSL element to define user named input port which may be used instead 
   * of [[de.grammarcraft.scala.flow.InputPort4[T].input4]] when forwarding input data function unit input ports.<br>
   * Typically the definition is done as follows:<br>
   * <code>val <i>myPortName</i> = InputPort4</code>.<br>
   * "InputPort4" literally corresponds  to the "with InputPort4" clause at the class
   * definition header of this function unit.
   */
  val InputPort4 = input4

}