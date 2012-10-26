package de.grammarcraft.scala.firstflow

import scala.actors.Actor

class AsyncCrossChanger(val name: String) extends Actor {
  
  case class Input1(msg: Any)
  case class Input2(msg: Any)
  
  var output1: Any => Unit = null
  var output2: Any => Unit = null

  val asynchronizer1 = new Actor() {
    def act() {
      while (true) {
        receive {
          case Input1(msg) => {
            println(msg + " asynchronously received as input1 for " + AsyncCrossChanger.this)
            processInput1(msg)
          }
          case "STOP" => exit()
        }
      }
    }
  }

  val asynchronizer2 = new Actor() {
    def act() {
      while (true) {
        receive {
          case Input2(msg) => {
            println(msg + " asynchronously received as input2 for " + AsyncCrossChanger.this)
            processInput2(msg)
          }
          case "STOP" => exit()
        }
      }
    }
  }
  
  def act() {
    loop {
      react {
        case i @ Input1(msg) => {
            println("asynchonously process " + msg + " as input1 for " + this)
        	asynchronizer1 ! i
        }
        case i @ Input2(msg) => {
            println("asynchonously process " + msg + " as input2 for " + this)
        	asynchronizer2 ! i
        }
        case stop @ "STOP" => {
        	asynchronizer1!stop
        	asynchronizer2!stop
        	exit()
        }
        case msg => println("Unknown msg received: " + msg)
      }
    }
  }
  
  def processInput1(msg: Any) {
    println("process " + msg + " as input1 for " + this + " takes 3 seconds...")
    Thread.sleep(3000) // even we are sleeping here
    if (output2 == null) {
    	println("no binding defined for output2")
    	return
    }
    output2(msg)
  }

  def processInput2(msg: Any) {
    println("process " + msg + " as input2 for " + this)
    if (output1 == null) {
    	println("no binding defined for output2")
    	return
    }
    output1(msg)
  }

  def input1(msg: Any) {
    this!Input1(msg)
  }

  def input2(msg: Any) {
    this!Input2(msg)
  }
  
  override def toString:String = name
  
  def stop() {
    this!"STOP"
  }
  
  // class initialization - start internal actors
  asynchronizer1.start()
  asynchronizer2.start()

}
