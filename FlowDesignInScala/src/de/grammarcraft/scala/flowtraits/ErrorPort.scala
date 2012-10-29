package de.grammarcraft.scala.flowtraits

/**
 * @author denis.kuniss
 *
 */
trait ErrorPort[T] {
  
  private[this] var errorOperations: List[T => Unit] = List()
  
  def bindErrorTo(operation: T => Unit) {
	  errorOperations = operation :: errorOperations
  }
  
  protected def error(exception: T) {
	  if (!errorOperations.isEmpty) {
		  errorOperations.foreach(forward => forward(exception))
	  }
	  else {
		  println("no binding defined for error of " + this)
	  }
  }

}