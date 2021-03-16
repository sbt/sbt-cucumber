import sbt.{Credentials, ScmInfo}

val Scala11x = "2.11.12"
val Scala12x = "2.12.10"

lazy val buildSettings = Seq(
  name := "cucumber-plugin",
  organization in Global := "com.waioeka.sbt",
  scalaVersion := Scala12x,
  crossScalaVersions in ThisBuild := Seq(Scala11x, Scala12x),
  sbtPlugin := true,
  sbtVersion in Global := "1.2.8",
  version := "0.2.2"
)

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
    "io.cucumber" % "cucumber-core" % "4.3.0",
    "io.cucumber" %% "cucumber-scala" % "4.3.0",
    "io.cucumber" % "cucumber-jvm" % "4.3.0" pomOnly(),
    "io.cucumber" % "cucumber-junit" % "4.3.0",
    "org.apache.commons" % "commons-lang3" % "3.9",
    "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2",
    "org.scala-lang.modules" %% "scala-xml" % "1.2.0"),
    fork in test := true
)

lazy val pluginSettings = buildSettings ++ commonSettings

lazy val plugin = project.in(file("."))
  .settings(moduleName := "cucumber-plugin")
  .settings(pluginSettings:_*)
  .settings(publishSettings:_*)
