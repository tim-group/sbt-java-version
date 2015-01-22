package com.timgroup
package sbtjavaversion

import sbt._
import Keys._

/**
 * Based on suggestions to this question:
 *   http://stackoverflow.com/q/19208942
 */
object SbtJavaVersion extends Plugin {
  import SbtJavaVersionKeys._
  private def shortVersion(version: String): String = version.split("\\.").last
  private def manifestAttributes(version: String): Map[String, String] = Map("X-Java-Version" -> shortVersion(version))

  private val targetVersion  = (javaVersion in target)     or (javaVersion in ThisBuild) or (javaVersion in Global)
  private val sourceVersion  = (javaVersion in javaSource) or targetVersion
  private val packageVersion = (javaVersion in packageBin) or targetVersion

  override lazy val projectSettings = {
    Seq(
      packageOptions in (Compile, packageBin) +=
        Package.ManifestAttributes(manifestAttributes(packageVersion.value).toSeq:_*),
      scalacOptions += s"-target:jvm-${targetVersion.value}",
      javacOptions ++= Seq("-target", targetVersion.value, "-source", sourceVersion.value)
    )
  }

  override lazy val globalSettings = Seq(
    javaVersion in Global := "1.6", // default, can be overridden in Build
    initialize := {
      val _ = initialize.value // run the previous initialization
      val required = VersionNumber((javaVersion in ThisBuild).value)
      val curr = VersionNumber(sys.props("java.specification.version"))
      println(s"Ensuring current java version ${required} >= ${curr}")
      assert(CompatibleJavaVersion(curr, required), s"Java $required or above required, found $curr instead")
    }
  )
}

object SbtJavaVersionKeys {
  val javaVersion = SettingKey[String]("java-version", "Set the version of java in target, javaSource, packageBin.")
}

private object CompatibleJavaVersion extends VersionNumberCompatibility {
  def name = "Java specification compatibility"
  def isCompatible(current: VersionNumber, required: VersionNumber) =
    current.numbers.zip(required.numbers).forall(n => n._1 >= n._2)

  def apply(current: VersionNumber, required: VersionNumber) = isCompatible(current, required)
}
