package de.grammarcraft.scala.flow.dsl

class OutputPort[T](register: (T => Unit) => Unit) {
	  def -> (operation: T => Unit) = register(operation)
	  def isProcessedBy(operation: T => Unit) = register(operation)
	  def -> (functionUnitWithOnlyOneInputPort: de.grammarcraft.scala.flow.InputPort[T]) = register(functionUnitWithOnlyOneInputPort.input(_))  
}