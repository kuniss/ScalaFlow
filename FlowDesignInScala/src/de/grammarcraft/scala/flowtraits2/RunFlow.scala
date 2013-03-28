package de.grammarcraft.scala.flowtraits2


object RunFlow {
  def main(args: Array[String]) {
	  // build
	  println("instantiate flow units...")
	  val reverse = new Reverse
	  val normalizer = new Normalize
	  val collector = new Collector(", ")
	  
	  // bind
	  println("bind them...")
	  reverse -> normalizer
	  normalizer.lower -> collector.lower
	  normalizer.upper -> collector.upper
	  collector.output isProcessedBy(msg => {
		  println("received '" + msg + "' from " + collector)
	  })
	  collector.error isProcessedBy(msg => {
		  println("error received from " + collector + ": " + msg)
	  })

	  // run
	  println("run them...")
	  val palindrom = "Trug Tim eine so helle Hose nie mit Gurt?"
	  println("send message: " + palindrom)
	  reverse.input(palindrom)

	  reverse.input(palindrom) // second call should raise an error message on the Collectors error port
	  
	  println("finished.")
	  
  }

}