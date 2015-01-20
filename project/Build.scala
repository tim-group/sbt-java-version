import sbt._
import Keys._

object SbtJavaVersionBuild extends Build with BuildExtra {

  val sbtJavaVersion = Project("sbt-java-version", file("."))
    .settings(
      sbtPlugin    := true,
      organization := "com.timgroup",
      name         := "sbt-java-version",
      version      := "0.0." + System.getProperty("BUILD_NUMBER", sys.env.getOrElse("BUILD_NUMBER", "0-SNAPSHOT")),
      sbtVersion   in Global := "0.13.7",
      scalaVersion in Global := "2.10.4"
    )
    .settings(
      publishTo := Some("TIM Group Repo" at "http://repo.youdevise.com:8081/nexus/content/repositories/yd-release-candidates")
    )
    .settings(credentials += Credentials(new File("/etc/sbt/credentials")))
}
