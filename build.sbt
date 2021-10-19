import sbt.{Credentials, ScmInfo}

val Scala10x = "2.10.7"
val Scala12x = "2.12.10"

ThisBuild / version := "0.3.1"
ThisBuild / scalaVersion := Scala12x
ThisBuild / organization := "com.waioeka.sbt"

// cucumber-scala 6.10.3 doesn't exist for Scala 2.10
ThisBuild / crossScalaVersions := Seq(Scala12x)

lazy val credentialSettings = Seq(
  credentials ++= (for {
    username <- Option(System.getenv().get("SONATYPE_USERNAME"))
    password <- Option(System.getenv().get("SONATYPE_PASSWORD"))
  } yield Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", username, password)).toSeq
)

lazy val publishSettings = Seq(
  publishMavenStyle := true,
  publishArtifact in Test := false,
  pomIncludeRepository := Function.const(false),
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("Snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("Releases" at nexus + "service/local/staging/deploy/maven2")
  },
  homepage := Some(url("https://github.com/lewismj/kea")),
  licenses := Seq("BSD-style" -> url("http://www.opensource.org/licenses/bsd-license.php")),
  scmInfo := Some(ScmInfo(url("https://github.com/lewismj/kea"), "scm:git:git@github.com:lewismj/kea.git")),
  autoAPIMappings := true,
  pomExtra := (
    <developers>
      <developer>
        <name>Michael Lewis</name>
        <url>@lewismj</url>
      </developer>
    </developers>
    )
) ++ credentialSettings

lazy val commonSettings = Seq(
  scalacOptions += "-target:jvm-1.8",
  libraryDependencies ++=  Seq (
    "io.cucumber" % "cucumber-core" % "6.10.3",
    "io.cucumber" %% "cucumber-scala" % "6.10.3",
    "io.cucumber" % "cucumber-jvm" % "6.10.3" pomOnly(),
    "io.cucumber" % "cucumber-junit" % "6.10.3",
    "org.apache.commons" % "commons-lang3" % "3.9",
    "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2",
    "org.scala-lang.modules" %% "scala-xml" % "1.2.0"),
    fork in test := true,
    // This cross builds on sbt using Scala version
    pluginCrossBuild / sbtVersion := {
      scalaBinaryVersion.value match {
        case "2.10" => "0.13.18"
        case "2.12" => "1.2.8"
      }
    },
)

lazy val pluginSettings = buildSettings ++ commonSettings

lazy val plugin = project.in(file("."))
  .enablePlugins(SbtPlugin)
  .settings(
    name := "cucumber-plugin",
    moduleName := "cucumber-plugin",
  )
  .settings(pluginSettings:_*)
  .settings(publishSettings:_*)
