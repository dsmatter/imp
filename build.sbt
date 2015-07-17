import ReleaseTransformations._

lazy val root = project.in(file("."))
  .aggregate(impJS, impJVM)
  .settings(publish := {}, publishLocal := {}, publishArtifact := false)

lazy val imp = crossProject.in(file("."))
  .settings(
    name := "imp",
    organization := "org.spire-math",
    scalaVersion := "2.11.7",
    crossScalaVersions := Seq("2.10.5", "2.11.7"),
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % scalaVersion.value % "provided",
      "org.scalatest" %%% "scalatest" % "3.0.0-M7" % "test"
    ),
    scalacOptions ++= Seq(
      "-deprecation",
      "-unchecked",
      "-feature"
    ),
    licenses += ("MIT", url("http://opensource.org/licenses/MIT")),
    homepage := Some(url("http://github.com/non/imp")),

    // release stuff
    releaseCrossBuild := true,
    releasePublishArtifactsAction := PgpKeys.publishSigned.value,
    publishMavenStyle := true,
    publishArtifact in Test := false,
    pomIncludeRepository := Function.const(false),
    publishTo <<= (version).apply { v =>
      val nexus = "https://oss.sonatype.org/"
      if (v.trim.endsWith("SNAPSHOT"))
        Some("Snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("Releases" at nexus + "service/local/staging/deploy/maven2")
    },
    pomExtra := (
    <scm>
      <url>git@github.com:non/imp.git</url>
      <connection>scm:git:git@github.com:non/imp.git</connection>
      </scm>
      <developers>
      <developer>
      <id>d_m</id>
      <name>Erik Osheim</name>
      <url>http://github.com/non/</url>
        </developer>
      </developers>
    ),
    releaseProcess := Seq[ReleaseStep](
      checkSnapshotDependencies,
      inquireVersions,
      setReleaseVersion,
      commitReleaseVersion,
      tagRelease,
      publishArtifacts,
      setNextVersion,
      commitNextVersion,
      pushChanges))

lazy val impJVM = imp.jvm
lazy val impJS = imp.js

