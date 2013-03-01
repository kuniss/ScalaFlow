package de.grammarcraft.scala.flowtraits


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
	  reverse -> toLower
	  reverse -> toUpper
	  toLower -> collector.lower
	  toUpper -> collector.upper
	  collector bindOutputTo(msg => {
		  println("received '" + msg + "' from " + collector)
	  })
	  // error handling
	  toLower bindErrorTo(error => {
	    println("exception happended at " + toLower + 
	        ": " + error)
	  })

	  // run
	  println("run them...")
	  val palindrom = "Trug Tim eine so helle Hose nie mit Gurt?"
	  println("send message: " + palindrom)
	  reverse.input(palindrom)

	  println("finished.")
	  
  }

}