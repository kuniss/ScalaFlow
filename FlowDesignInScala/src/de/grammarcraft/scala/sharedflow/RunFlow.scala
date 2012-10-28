package de.grammarcraft.scala.sharedflow


object RunFlow {
  def main(args: Array[String]) {
	  // build
	  println("instantiate flow units...")
	  val reverse = new Reverse
	  val toLower = new ToLower
	  val toUpper = new ToUpper
	  val collector = new Collector(", ")

	  def stopAll() {
		  reverse.stop()
		  toLower.stop()
		  toUpper.stop()
		  collector.stop()
	  }
	  
	  // bind
	  println("bind them...")
	  reverse bindOutputTo toLower.input
	  reverse bindOutputTo toUpper.input
	  toLower bindOutputTo collector.input1
	  toUpper bindOutputTo collector.input2
	  collector bindOutputTo(msg => {
		  println("received '" + msg + "' from " + collector)
		  stopAll()
	  })
	  // error handling
	  toUpper bindErrorTo(exception => {
	    println("exception happended at " + toUpper + 
	        ": " + exception.getMessage())
	        stopAll()
	  })
	  toLower bindErrorTo(exception => {
	    println("exception happended at " + toLower + 
	        ": " + exception.getMessage())
	        stopAll()
	  })
	  reverse bindErrorTo(exception => {
	    println("exception happended at " + reverse + 
	        ": " + exception.getMessage())
	        stopAll()
	  })
	  collector bindErrorTo(exception => {
	    println("exception happended at " + collector + 
	        ": " + exception.getMessage())
	        stopAll()
	  })

	  // run
	  println("run them...")
	  reverse.start()
	  toLower.start()
	  toUpper.start()
	  collector.start()
	  val palindrom = "Trug Tim eine so helle Hose nie mit Gurt?"
	  println("send message: " + palindrom)
	  reverse.input(palindrom)

	  println("finished.")
	  
  }

}