package de.grammarcraft.scala.sharedflow

import scala.actors.Actor

class ToUpper extends Actor {
  
  case class Input(msg: String)
  
  def input(msg: String) {
	  this!Input(msg)
  }
  
  def act() {
    loop {
      react {
        case Input(msg) => {
        	println("'" + msg + "' received as input for " + this)
        	output(process(msg))
        }
        case "STOP" => exit()
      }
    }
  }
  

  private[this] var outputTargets: List[String => Unit] = List()
  
  def bindOutputTo(operation: String => Unit) {
	  outputTargets = operation :: outputTargets
  }
  
  def output(msg: String) {
	  if (!outputTargets.isEmpty) {
	    outputTargets.foreach(operation => operation(msg))
	  }
	  else {
	    println("no binding defined for output of " + this)
	  }
  }

  
  def process(msg: String): String = msg.toUpperCase()
  
  
  def stop() {
    this!"STOP"
  }

  override def toString:String = "ToUpper"

}
