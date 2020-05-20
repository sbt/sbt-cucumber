name := "cucumber-test"

organization := "com.waioeka.sbt"

version := "0.0.6"

scalaVersion := "2.12.11"

libraryDependencies ++= Seq (
  "io.cucumber" % "cucumber-core" % "4.7.1" % "test",
  "io.cucumber" %% "cucumber-scala" % "4.7.1" % "test",
  "io.cucumber" % "cucumber-jvm" % "4.7.1" % "test",
  "io.cucumber" % "cucumber-junit" % "4.7.1" % "test",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test")

enablePlugins(CucumberPlugin)


CucumberPlugin.envProperties := Map("K"->"2049")

CucumberPlugin.monochrome := false
CucumberPlugin.glues := List("com/waioeka/sbt/")

CucumberPlugin.beforeAll := { () =>
  println("beforeAll")
  sbt.IO.append( target.value / "beforeAll", "beforeAll")
}
CucumberPlugin.afterAll := { () =>
  println("afterAll")
  sbt.IO.append( target.value / "afterAll", "afterAll")
}
