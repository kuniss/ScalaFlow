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

