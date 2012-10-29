package de.grammarcraft.scala.actorlessflow


object RunFlow {
  def main(args: Array[String]) {
	  // build
	  println("instantiate flow units...")
	  val reverse = new Reverse
	  val toLower = new ToLower
	  val toUpper = new ToUpper
	  val collector = new Collector(", ")
	  
	  // bind
	  println("bind them...")
	  reverse bindOutputTo toLower.input
	  reverse bindOutputTo toUpper.input
	  toLower bindOutputTo collector.input1
	  toUpper bindOutputTo collector.input2
	  collector bindOutputTo(msg => {
		  println("received '" + msg + "' from " + collector)
	  })
	  // error handling
	  toUpper bindErrorTo(exception => {
	    println("exception happended at " + toUpper + 
	        ": " + exception.getMessage())
	  })
	  toLower bindErrorTo(exception => {
	    println("exception happended at " + toLower + 
	        ": " + exception.getMessage())
	  })
	  reverse bindErrorTo(exception => {
	    println("exception happended at " + reverse + 
	        ": " + exception.getMessage())
	  })
	  collector bindErrorTo(exception => {
	    println("exception happended at " + collector + 
	        ": " + exception.getMessage())
	  })

	  // run
	  println("run them...")
	  val palindrom = "Trug Tim eine so helle Hose nie mit Gurt?"
	  println("send message: " + palindrom)
	  reverse.input(palindrom)

	  println("finished.")
	  
  }

}