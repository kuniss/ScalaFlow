package de.grammarcraft.scala.sharedflow

import scala.actors.Actor

class Collector(val separator: String) extends Actor {
  
  case class Input1(msg: String)
  case class Input2(msg: String)
  
  def input1(msg: String) {
	  this!Input1(msg)
  }
  
  def input2(msg: String) {
	  this!Input2(msg)
  }

  def act() {
    loop {
      react {
        case Input1(msg) => {
        	println("'" + msg + "' received as input1 for " + this)
        	try {
        		accumulateInput(msg)
        	}
        	catch {
        	  case ex:Exception => error(ex);
			}
        }
        case Input2(msg) => {
        	println("'" + msg + "' received as input2 for " + this)
        	try {
        		accumulateInput(msg)
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

  private[this] var accumulation: List[String] = List()
		  
  def accumulateInput(msg: String) {
	  accumulation = msg :: accumulation
	  if (accumulation.length == 2) output(accumulation mkString(separator))
  }
  
  
  override def toString:String = "Collector"
  
  def stop() {
    this!"STOP"
  }

}
