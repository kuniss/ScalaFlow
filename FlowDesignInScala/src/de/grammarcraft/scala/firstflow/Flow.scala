package de.grammarcraft.scala.firstflow

object Flow {
  def main(args: Array[String]) {
	  // instantiate
	  println("instantiate flow units...")
	  val op1 = new AsyncCrossChanger("crossing1")
	  val op2 = new CrossChanger("crossing2")
	  
	  // bind
	  println("bind them...")
	  op1.output1 = op2.input1
	  op1.output2 = op2.input2
	  op2.output1 = msg => {
		  println("received " + msg + " over output1 of " + op2)
		  op1.stop()
		  op2.stop()
	  }
	  op2.output2 = msg => println("received " + msg + " over output2 of " + op2)

	  // start
	  println("run them...")
	  op1.start()
	  op2.start()
	  println("send first message")
	  op1.input1("first message")
	  println("send second message")
	  op1.input2("second message")

	  println("finished.")
  }

}