package de.grammarcraft.scala.cyclicflow

import de.grammarcraft.scala.flow.ControlStructures._

object RunFlow {
  def main(args: Array[String]) {
	  // build
	  println("instantiate flow units...")
	  val cyclic = new Cyclic
	  
	  // bind
	  println("bind them...")
	  cyclic.continue -> cyclic.run
	  cyclic.stopped -> (
	      cyclicCounter => {
	    	  println(cyclic + " stopped on " + cyclicCounter)
	      }
	  )

	  onErrorAt(cyclic)  {
		  errMsg => println("error received: " + errMsg)
	  }
	  
	  onIntegrationErrorAt(cyclic) {
		  errMsg => System.err.println("integration error happened: " + errMsg)
	  }
	  
	  // run
	  println("run them...")
	  cyclic.run <= 1
	  
	  println("finished.")
	  
  }

}