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
        	try {
        		output(process(msg))
        	}
        	catch {
        	  case ex:Exception => error(ex);
			}
        }
        case "STOP" => exit()
        case msg => error(new RuntimeException("unknown message '" + msg + "' received; will not be processed"))
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
	    error(new RuntimeException("no binding defined for output of " + this + ": '" + 
	        msg + "' could not be delivered"))
	  }
  }

  private[this] var errorTargets: List[Exception => Unit] = List()
  
  def bindErrorTo(operation: Exception => Unit) {
	  errorTargets = operation :: errorTargets
  }
  
  def error(exception: Exception) {
	  if (!errorTargets.isEmpty) {
		  errorTargets.foreach(forward => forward(exception))
	  }
	  else {
		  println("no binding defined for error of " + this)
	  }
  }

  
  def process(msg: String): String = {
    throw new RuntimeException("artifical exception for testing purposes; '" + 
        msg + "' will not be processed")
//    msg.toUpperCase()
  }
  
  
  def stop() {
    this!"STOP"
  }

  override def toString:String = "ToUpper"

}
