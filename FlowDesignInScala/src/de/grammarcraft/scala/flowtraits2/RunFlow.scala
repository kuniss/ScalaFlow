package de.grammarcraft.scala.flowtraits2


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
	  collector.letOutputBeProcessedBy(msg => {
		  println("received '" + msg + "' from " + collector)
	  })

	  // run
	  println("run them...")
	  val palindrom = "Trug Tim eine so helle Hose nie mit Gurt?"
	  println("send message: " + palindrom)
	  reverse.input(palindrom)

	  println("finished.")
	  
  }

}