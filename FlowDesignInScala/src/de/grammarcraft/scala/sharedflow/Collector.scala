package de.grammarcraft.scala.sharedflow

import scala.actors.Actor

class Collector(val separator: String) extends Actor {
  
  case class Input1(msg: String)
  case class Input2(msg: String)
  

  def act() {
    loop {
      react {
        case Input1(msg) => {
        	println("'" + msg + "' received as input1 for " + this)
        	accumulateInput(msg)
        }
        case Input2(msg) => {
        	println("'" + msg + "' received as input2 for " + this)
        	accumulateInput(msg)
        }
        case "STOP" => exit()
      }
    }
  }
  
  private[this] var accumulation: List[String] = List()
  
  def accumulateInput(msg: String) {
    accumulation = msg :: accumulation
    if (accumulation.length == 2) output(accumulation mkString(separator))
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
  
  def input1(msg: String) {
    this!Input1(msg)
  }

  def input2(msg: String) {
    this!Input2(msg)
  }
  
  override def toString:String = "Collector"
  
  def stop() {
    this!"STOP"
  }

}
