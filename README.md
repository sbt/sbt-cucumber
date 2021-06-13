# SBT Cucumber

<p align="left">
<a href="https://travis-ci.org/sbt/sbt-cucumber">
<img src="https://travis-ci.org/sbt/sbt-cucumber.svg?branch=master"/>
</a>
<a href="https://search.maven.org/#search%7Cga%7C1%7Ca%3A%22cucumber-plugin%22">
<img src="https://maven-badges.herokuapp.com/maven-central/com.waioeka.sbt/cucumber-plugin/badge.svg"/>
</a>
<a href="https://waffle.io/lewismj/cucumber">
<img src="https://badge.waffle.io/lewismj/cucumber.svg?columns=In%20Progress,Done&style=flat-square">
</a>
<a href="https://gitter.im/lewismj/cucumber">
<img src="https://badges.gitter.im/Join%20Chat.svg">
</a>
</p>

The Cucumber plugin provides a new sbt command, allowing you to run just your Cucumber tests using `sbt cucumber`.

The plugin can be used if you want a separate command to run Cucumber tests and have your normal test framework ignore Cucumber tests.

If you want to run Cucumber tests within a ScalaTest unit test framework see [here](https://github.com/lewismj/cucumber).

## Dependency Information

```scala
 addSbtPlugin("com.waioeka.sbt" % "cucumber-plugin" % "0.3.1")
```

> **Note** The latest version of the plugin is built using SBT 1.2.8.

### Cucumber Plugin Example

The project _cucumber-plugin-example_ highlights how to use the plugin. You will need to ensure that your `build.sbt` file defines both the dependencies and the 'glue' setting (i.e. where to find the step definitions).

```scala
name := "cucumber-test"
organization := "com.waioeka.sbt"
version := "0.3.0"

libraryDependencies ++= Seq (
  "io.cucumber" % "cucumber-core" % "6.10.3" % "test",
  "io.cucumber" %% "cucumber-scala" % "6.10.3" % "test",
  "io.cucumber" % "cucumber-jvm" % "6.10.3" % "test",
  "io.cucumber" % "cucumber-junit" % "6.10.3" % "test",
  "org.scalatest" %% "scalatest" % "3.2.8" % "test")

enablePlugins(CucumberPlugin)

CucumberPlugin.glues := List("com.waioeka.sbt")

// Any environment properties you want to override/set.
CucumberPlugin.envProperties := Map("K"->"2049")
```

Remember to set the `CucumberPlugin.glue` parameter to the sub directory in *test* that contains your Scala step definitions.

In your *resources* directory, put in your feature files.

```feature
@my-tag
Feature: Multiplication
  In order to avoid making mistakes
  As a dummy
  I want to multiply numbers

Scenario: Multiply two variables
Given a variable x with value 2
And a variable y with value 3
When I multiply x * y
Then I get 6
```

If you need to generate the stubs, just run `sbt cucumber` and you will get an error complaining about missing stubs. You can copy and paste the stub functions into your step implementation.

You can now put in your stub implementation:

```scala
package com.waioeka.sbt

import io.cucumber.scala.{EN, ScalaDsl}
import org.scalatest.Matchers

/** AddAndMultiplySteps*/
class MultiplicationSteps extends ScalaDsl with EN with Matchers {
  var x : Int = 0
  var y : Int = 0
  var z : Int = 0

  Given("""^a variable x with value (\d+)$""") { (arg0: Int) => x = arg0 }

  Given("""^a variable y with value (\d+)$""") { (arg0: Int) => y = arg0 }

  When("""^I multiply x \* y$""") { () => z = x * y }

  Then("""^I get (\d+)$""") { (arg0: Int) => z should be (arg0) }
}
```

To run the tests in standalone mode:

```shell
sbt compile
sbt cucumber
```

You should see some output as follows:

```shell
paeroa:cucumber-test lewismj$ sbt cucumber
[info] Loading project definition from /Users/lewismj/waioeka/upa-plugins/cucumber-test/project
[info] Set current project to cucumber-test (in build file:/Users/lewismj/waioeka/upa-plugins/cucumber-test/)
[info] Feature: Multiplication
[info]   In order to avoid making mistakes
[info]   As a dummy
[info]   I want to multiply numbers
[info]
[info]   Scenario: Multiply two variables  # Multiplication.feature:6
[info]     Given a variable x with value 2 # MultiplicationSteps.scala:17
[info]     And a variable y with value 3   # MultiplicationSteps.scala:21
[info]     When I multiply x * y           # MultiplicationSteps.scala:25
[info]     Then I get 6                    # MultiplicationSteps.scala:29
[info]
[info] 1 Scenarios (1 passed)
[info] 4 Steps (4 passed)
[info] 0m0.106s
[info]
[success] Total time: 1 s, completed 28-Dec-2015 22:16:19
 ```

The results will be output in the following formats:

- cucumber-html, standard Cucumber html output.
- cucumber.json, standard Cucumber json output.
- cucumber-junit-report.xml, a Junit style rest report.

## Cucumber Plugin Arguments

Cucumber arguments may be supplied. For example, `sbt "cucumber --tags ~@my-tag"` will filter tagged feature files.

```shell
[success] Total time: 1 s, completed 15-Jun-2016 09:23:22
paeroa:cucumber-plugin-example lewismj$ sbt "cucumber --tags ~@my-tag"
[info] Loading global plugins from /Users/lewismj/.sbt/0.13/plugins
[info] Loading project definition from /Users/lewismj/github/cucumber/cucumber-plugin-example/project
[info] Set current project to cucumber-test (in build file:/Users/lewismj/github/cucumber/cucumber-plugin-example/)
** hello **
[info] None of the features at [classpath:] matched the filters: [~@my-tag]
[info]
[info] 0 Scenarios
[info] 0 Steps
[info] 0m0.000s
[info]
** goodbye **
[success] Total time: 1 s, completed 15-Jun-2016 09:23:41
```

## Properties

In your `build.sbt` file you can now specify environment variables as follows:

```scala
CucumberPlugin.envProperties := Map("K"->"2049")
```

The setting expects a type of `Map[String,String]`.

## Changelog

| Version | Summary                                                                                                                                           |
|---------|---------------------------------------------------------------------------------------------------------------------------------------------------|
| 0.3.0   | The plugin now uses Cucumber 6.10.3                                                                                                               |
| 0.2.0   | Please note this [change](https://github.com/sbt/sbt-cucumber/pull/4/files) was merged in, to make the glue parameter a List, rather than String. |
