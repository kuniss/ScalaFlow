ScalaFlow - Flow Design in Scala [![Build Status](https://travis-ci.org/kuniss/ScalaFlow.svg?branch=master)](https://travis-ci.org/kuniss/ScalaFlow) [![Download](https://api.bintray.com/packages/kuniss/maven/de.grammarcraft.scala.flow/images/download.svg) ](https://bintray.com/kuniss/maven/de.grammarcraft.scala.flow/_latestVersion) [![Get automatic notifications about new "de.grammarcraft.xtend.scala" versions](https://www.bintray.com/docs/images/bintray_badge_color.png) ](https://bintray.com/kuniss/maven/de.grammarcraft.scala.flow/view?source=watch)
=====================================================================================================================

This library enables [Scala](http://www.scala-lang.org/) for Flow Design based programming, the natural approach for [IODA Architectures](http://geekswithblogs.net/theArchitectsNapkin/archive/2015/04/29/the-ioda-architecture.aspx).

The notation tries to follow the intention of the inventor of Flow Design [Ralf Westphal](http://blog.ralfw.de/) as close as possible. 
This programming paradigm and its notation was initially explained by him in English in his articles 
["Flow-Design Cheat Sheet – Part I, Notation"](http://www.geekswithblogs.net/theArchitectsNapkin/archive/2011/03/19/flow-design-cheat-sheet-ndash-part-i-notation.aspx) and
["Flow-Design Cheat Sheet – Part II, Notation"](http://www.geekswithblogs.net/theArchitectsNapkin/archive/2011/03/20/flow-design-cheat-sheet-ndash-part-ii-translation.aspx).
He introduces the underlying paradigm in detail in his article series ["The Incremental Architect´s Napkin"](http://geekswithblogs.net/theArchitectsNapkin/category/19718.aspx) and in his book ["Messaging as a Programming Model"](https://leanpub.com/messaging_as_a_programming_model).
The according architectural approach he describes in his article ["The IODA Architecture"](http://geekswithblogs.net/theArchitectsNapkin/archive/2015/04/29/the-ioda-architecture.aspx). However, this architectural approach is more general and is going beyond Flow Design and may be applicable to any programming platform.
All, absolutely worth to read!

Who understands German may read [my blog article series](http://blog.grammarcraft.de/2013/04/26/treppengeplatscher/)
on how this notation was initially mapped to Scala.

However, since version [0.1.0](https://github.com/kuniss/ScalaFlow/releases/tag/0.1.0) the Scala Flow library 
supports a notation which takes the advantages of Scala regarding operator overriding forming up a tiny DSL which is very concise and 
quite closer to the original flow notation than my first attempt.

## Scala Flow DSL

The Scala Flow DSL uses traits for declaring function units with their input and output ports, as well as of operators for wiring ports and for forwarding message to input and output ports.

### Function Unit With Ports

A class extended from class `FunctionUnit` enriched by the generic traits `InputPort` and `OutputPort` declares a function unit including threir input and output ports.
E.g., the function unit

![Simple Function Unit](http://blog.grammarcraft.de/wp-content/uploads/2013/03/Bild3-ToUpper.png)

would be declared in Scala as follows:
```scala
class ToUpper extends FunctionUnit("ToUpper") 
	with InputPort[String] 
	with OutputPort[String]
{
	val in = InputPort
	val out = OutputPort("out")
...
}
```
Here the port names `in` and `out` for the input and output port respectively have been defined and may be used later in flow declarations. Alternatively, the predefined ports `input` adn `output` may be used. `InputPort` and `OutputPort` are intended tob used for function units with only one input respectively output port. If more ports wants to be specified, there are up to 5 traits defined: `InputPort1`, `InputPort2`,.. and `OutputPort1`, `OutputPort2`... 

Organizing software systems in function units is one of the main design keys of Flow Design. It follows closely the [Principle of Mutual Oblivion (PoMO)](http://geekswithblogs.net/theArchitectsNapkin/archive/2014/08/24/the-incremental-architectrsquos-napkin---5---design-functions-for.aspx). There is no dependency between the implementation of different function units. Function units does not know each other. There are only global dependencies to message data types flowing through the ports.


### Processing Input Data

How the string messages arriving over the port `input` are processed is implemented by the method `processInput` (or `processInput1`, `processInput2` respectively):
```scala
class ToUpper {
  protected def processInput(msg: String) {
    ...
  }
```
Having added the according input port traits to a Scala class, the implementation of the right named method for processing the input arriving over the `input` port is also requested by compiler and supported by quick fix proposals in IDEs like Eclipse.


### Message Forwarding Operator `<=`

The computed messages inside the input pocessing method may be forwarded to the declared `output` port using the message forwarding operator `<=`:

```scala
output <= msg.toUpperCase()
``` 

The message forwarding operator also accepts closures computing the message to be forwarded:

```scala
output <= {
    if (msg.startsWith("_"))
        msg.toFirstUpper
    else
        msg.toUpperCase
}
``` 

## Operation Unit

The IODA architecture approach defines an operation as a function unit that contains logic and may have control flow.
The entry point to the logic are the input data processing methods `processInputX` defined for each input port of a particular operation unit.
The result of the computation logic is forwarded to the appropriate output port at the end of the logic processing using the frward operator `<=`. Here is an example with quite easy logic and no control flow:

```scala
class ToUpper extends FunctionUnit("ToUpper") 
	with InputPort[String] 
	with OutputPort[String]
{

  protected def processInput(msg: String) {
    output <= msg.toUpperCase()
  }
```


## Integration Unit

While operation units are used for implementing the pieces of logic a system is composed of, the only reason of integration units following the IODA architecture approach is the integration of other function units, operation or integration units. Integration units are used to compose systems and parts of systems. 
Nevertheless they are treated as function units as well, having input and output ports. Therefore input ports and output ports are declared in the same way.

```scala
final class Normalize extends FunctionUnit("Normalize") 
    with InputPort[String]
    with OutputPort1[String]
    with OutputPort2[String]
{
  ...
}
```

Separating operation units - for implementing functionality - from integration units for integration purposes is one of the main goals of the IODA architecture and within Flow Design. It allows to structure software systems in several levels of abstraction having implementation details only on the lowest level of abstraction. On upper level of abstraction the implementation consist of integration functionality only, represented by integration units.
This is the second main principle of Flow Design and follows closely the [Integration Operation Segregation Principle (IOSP)](http://geekswithblogs.net/theArchitectsNapkin/archive/2014/09/13/the-incremental-architectacutes-napkin---7---nest-flows-to.aspx). 

### Port Wiring Operator `->`

The function units are either instantiated by the board or passed via constructor injection. Any [Dependency Injection](https://en.wikipedia.org/wiki/Dependency_injection) approach could be applied here.

As integrating is the only reason for an integration unit, it normally has only a constructor method, nothing else.
This constructor connects output to input ports of the integrated function units, as well as the integration unit's own
input ports to the integrated function unit's input ports, and the integrated function unit's output ports to the integration unit's own output ports. 
For this the wiring operator `->` is used. Here comes an example:

```scala
final class Normalize extends FunctionUnit("Normalize") 
    with InputPort[String]
    with OutputPort1[String]
    with OutputPort2[String]
{ 
	// for meaningful names on binding to context
	val lower = OutputPort1("lower")
	val upper = OutputPort2("upper")

	val toLower = new ToLower
	val toUpper = new ToUpper

	// bind 
	toLower -> lower
	toUpper -> upper

        ...
}
```  
In this example the output from the `ToLower` and `ToUpper` function units are flowing to the output ports `lower` and `upper` of the integrating function unit `Normalize`.
The input data flying in through the input port `input` defined by trait `InputPort` is processed by the according `processInput` method and forwarded to the integrated function units `ToLower` and `ToUpper`:
```scala
	protected def processInput(msg: String) {
		toLower <= msg
		toUpper <= msg
	}
```

The wiring follows the data flow implemented by the system to be designed. The Xtend class above integrates the two instances of function units `ToLower` and `ToUpper` as shown in the following figure. In fact, it does not matter whether the integrated function units are integration or operation function units!

![Integrating Function Unit](http://blog.grammarcraft.de/wp-content/uploads/2013/07/Bild3-Normalize-reingezoomt1.png)

The wiring operator works for full qualified port names, like
```
fu1.output -> fu2.input
```
as well as for function units with only one input or output port, like
```
fu1 -> fu2
```
as well as for mixed combinations, like
```
fu1.output -> fu2
fu1 -> fu2.output
```

### Implementing Side Effects

Beside the normal port wiring some time a side effect wants to be specified (e.g. explicit logging). To make this explicit, the special DSL construct `on` has been defined which accepts a code block like closure specifcation:
```scala
	  on(collector.output) { msg => 
		  println("received '" + msg + "' from " + collector)
	  }
```
Here the output data leaving the `collector` function unit trough the `output` port are printed on the standard out.

## Error Handling


### Integration Error Handling

An integration error is given if a declared output port is not connected to any input port nor any closure processing message from it. This is a meta-model error and is treated by the library by the special port `intergationError` of type `String` predefined for each function unit. By default this port is always connected to a closure printing a fatal error message on the error console.

However, this connection may be overridden by the library user, explicitly forwarding the exception to an user defined closure or input port.

```
on(fu.integrationError) { log.fatal("integration error happened: {0}", exception.message) } 
```

as this may become a hassle if for all instantiated function units the same closure has to be connected, there is an helper expression implemented where a bunch of function units may be connected at once to one closure:
```
 	onIntegrationErrorAt(reverse, normalizer) {
		  errMsg => System.err.println("integration error happened: " + errMsg)
	}
```

Here, the variables `reverse` and `collector` are referencing function unit instances.


### Model Error Handling

The more interesting part for system designers is, how to handle errors which are inherent part of the system to be modeled. 
In fact, this is quite easy: You may use the predefined trait `ErrorPort`. This will allow you to subsume all error ports at one integrating function unit to forward to the same output port or be processed by teh same closure using the predefined DSL construct `onErrorAt` as shown below.

```scala
  onErrorAt(collector)  {
    errMsg => println("error received: " + errMsg)
  }
```

Or, you may design an error port as ordinary port. As errors are still messages flowing through the system and they be handled explicitly by system's design. 

The only adavantage of using `ErrorPort` is its explicit specifcation as error port and the possibility to handle several error ports at once by the `onErrorAt` construct.

## Example

A simple example applying this notation may be found [in the test sources of this project](https://github.com/kuniss/ScalaFlow/tree/master/FlowDesignInScala/test/src/de/grammarcraft/scala/flowtraits2).

A non-trivial but simple example may be found [in this respository](https://github.com/kuniss/ScalaFlow-Examples/tree/master/ScalaFlow-ConvertRoman).


## Runtime Cost Considerations

In fact, wiring up ports results in method call implementations which are not very expensive and are almost well optimized by the Hotspot JVM.

E.g., the wiring of the function units from the example above into a chain, throwing an exception at the end, like
```
  reverse -> toUpper
  toUpper -> toLower
  toLower -> { msg => throw new RuntimeException(msg) }
         
  try {
    reverse <= "some input"
  }
  catch {
    case re: RuntimeException => re.printStackTrace
  }
```
will result in an exception with the following stack trace (shorten to show the important calls):
```
java.lang.RuntimeException: tupni emos
	at de.grammarcraft.scala.flowtraits2.CallStackExample$$anonfun$main$1.apply(CallStackExample.scala:14)
	...
	at de.grammarcraft.scala.flow.OutputPort$class.de$grammarcraft$scala$flow$OutputPort$$forwardOutput(OutputPort.scala:51)
	...
	at de.grammarcraft.scala.flowtraits2.ToLower.processInput(ToLower.scala:13)
	...
	at de.grammarcraft.scala.flow.OutputPort$class.de$grammarcraft$scala$flow$OutputPort$$forwardOutput(OutputPort.scala:51)
	...
	at de.grammarcraft.scala.flowtraits2.ToUpper.processInput(ToUpper.scala:13)
	...
	at de.grammarcraft.scala.flow.OutputPort$class.de$grammarcraft$scala$flow$OutputPort$$forwardOutput(OutputPort.scala:51)
	...
	at de.grammarcraft.scala.flowtraits2.Reverse.processInput(Reverse.scala:14)
	...
	at de.grammarcraft.scala.flowtraits2.CallStackExample.main(CallStackExample.scala)
```

## Concurrency Considerations

The message forwarding mechanisms of this Flow Design implementation are not thread safe at all. 
So, if messages may be forwarded to an input port of a function unit instance in concurrent situations, means, the particular `processInputX` method of  that input port may be called concurrently from different threads, the implementation of this method must handle such concurrent calls properly by a thread safe internal implementation according to the Scala synchronization rules.

Detecting concurrent situations in the system to be designed is an inherent mission of the system designer. Flow design does not help him in that challenge. However, support may be added in later versions of the library, when the actors concept is integrated letting function units becomes actors for concurrent designs. An example for combining the Flow Design approach with the Actors Design may be found in Ralf Westphal's article ["Actors in a IODA Architecture by Example"](http://geekswithblogs.net/theArchitectsNapkin/archive/2015/05/12/actors-in-a-ioda-architecture-by-example.aspx)

## Flow Cycle Considerations

The designer has strictly to avoid specifying direct or indirect cycles in the message flow! Cycles cannot be detected by the compiler as the library and its notation has been realized as an internal DSL utilizing Scala's opoerator overriding capabilities. For detecting cycles, the analysis of the global context would be needed. This may only be done by a compiler of an external DSL.

Who understands German may read my [blog article](http://blog.grammarcraft.de/2014/02/08/kreisfluss-rueckgekoppelte-systeme-mit-flow-design/) about that issue.

## Build Integration

The library is provided for arbitrary build systems via
[Bintray](https://bintray.com/kuniss/maven/de.grammarcraft.scala.flow/view) 
and via [Maven Central](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22de.grammarcraft.scala.flow%22) thanks to [Stefan Öhme's article](http://mnmlst-dvlpr.blogspot.de/2014/12/my-lightweight-release-process.html).

E.g., the integration via Gradle is as follows:
```
dependencies {
    compile 'de.grammarcraft:de.grammarcraft.scala.flow:0.1.+'
}
```


## How It Gets Released

The building is done applying Gradle plugins 
for [Scala compiling](https://docs.gradle.org/current/userguide/scala_plugin.html) 
and [for releasing to Bintray and Maven Central](http://plugins.gradle.org/plugin/com.github.oehme.sobula.bintray-release).

The build is running on [Travis CI](https://travis-ci.org/kuniss/ScalaFlow).
