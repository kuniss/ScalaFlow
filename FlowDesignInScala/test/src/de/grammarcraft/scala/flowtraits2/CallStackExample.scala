package de.grammarcraft.scala.flowtraits2

object CallStackExample {
    
    def main(args: Array[String]) {
        // build
        val reverse = new Reverse
        val toUpper = new ToUpper
        val toLower = new ToLower
        
        // bind
          reverse -> toUpper
          toUpper -> toLower
          toLower -> { msg => throw new RuntimeException(msg) }
            
          try {
              reverse <= "some input"
          }
          catch {
            case re: RuntimeException => re.printStackTrace
          }
    }
    
}