package com.timgroup
package sbtjavaversion

import sbt._
import Keys._

/**
 * Based on suggestions to this question:
 *   http://stackoverflow.com/q/19208942
 */
object SbtJavaVersion extends AutoPlugin {
  object autoImport {
    val javaVersion = SettingKey[String]("java-version", "Set the version of java in target, javaSource, packageBin.")
  }

  import SbtJavaVersionKeys._
  private def shortVersion(version: String): String = version.split("\\.").last
  private def manifestAttributes(version: String): Map[String, String] = Map("X-Java-Version" -> shortVersion(version))

  private val targetVersion  = (javaVersion in target)     or (javaVersion in ThisBuild)
  private val sourceVersion  = (javaVersion in javaSource) or targetVersion
  private val packageVersion = (javaVersion in packageBin) or targetVersion

  private def assertCompatibleJavaVersion(required: VersionNumber): Unit = {
    val curr = VersionNumber(sys.props("java.specification.version"))
    println(s"Ensuring current java version ${curr} >= ${required} required")
    assert(CompatibleJavaVersion(curr, required), s"Java $required or above required, found $curr instead")
  }

  override lazy val projectSettings = {
    Seq(
      initialize := {
        val _ = initialize.value
        assertCompatibleJavaVersion(VersionNumber((javaVersion).value))
      },
      packageOptions in (Compile, packageBin) +=
        Package.ManifestAttributes(manifestAttributes(packageVersion.value).toSeq:_*),
      scalacOptions += s"-target:jvm-${targetVersion.value}",
      javacOptions ++= Seq("-target", targetVersion.value, "-source", sourceVersion.value)
    )
  }

  override lazy val globalSettings = Seq(
    javaVersion in Global := "1.6", // default, can be overridden in Build
    initialize := {
      val _ = initialize.value
      assertCompatibleJavaVersion(VersionNumber((javaVersion).value))
    }
  )
}

object SbtJavaVersionKeys {
  val javaVersion = SbtJavaVersion.autoImport.javaVersion
}

private object CompatibleJavaVersion extends VersionNumberCompatibility {
  def name = "Java specification compatibility"
  def isCompatible(current: VersionNumber, required: VersionNumber) =
    current
      .numbers
      .zip(required.numbers)
      .foldRight(required.numbers.size<=current.numbers.size)((a,b) => (a._1 > a._2) || (a._1==a._2 && b))

  def apply(current: VersionNumber, required: VersionNumber) = isCompatible(current, required)
}
