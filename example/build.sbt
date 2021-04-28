name := "cucumber-test"

organization := "com.waioeka.sbt"

version := "0.0.6"

scalaVersion := "2.12.13"

libraryDependencies ++= Seq (
        "io.cucumber" % "cucumber-core" % "6.10.3" % Test,
        "io.cucumber" %% "cucumber-scala" % "6.10.3" % Test,
        "io.cucumber" % "cucumber-jvm" % "6.10.3" % Test,
        "io.cucumber" % "cucumber-junit" % "6.10.3" % Test,
        "org.scalatest" %% "scalatest" % "3.2.8" % Test)

enablePlugins(CucumberPlugin)


CucumberPlugin.envProperties := Map("K"->"2049")

CucumberPlugin.monochrome := false
CucumberPlugin.glues := List("com.waioeka.sbt")

def before() : Unit = { println("beforeAll") }
def after() : Unit = { println("afterAll") }

CucumberPlugin.beforeAll := before
CucumberPlugin.afterAll := after
