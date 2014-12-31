package com.timgroup
package sbtjavaversion

import sbt._
import Keys._

/**
 * Based on suggestions to this question:
 *   http://stackoverflow.com/q/19208942
 */
class EnforceJava(version: String) extends Plugin {
  def shortVersion: String = version.split("\\.").last
  val manifestAttributes: Map[String, String] = Map("X-Java-Version" -> shortVersion)

  override lazy val globalSettings = Seq(
    packageOptions in (Compile, packageBin) +=
       Package.ManifestAttributes(manifestAttributes.toSeq:_*),
    scalacOptions += s"-target:jvm-${version}",
    javacOptions ++= Seq("-target", version, "-source", version),
    initialize := {
      val _ = initialize.value // run the previous initialization
      val required = VersionNumber(version)
      val curr = VersionNumber(sys.props("java.specification.version"))
      assert(CompatibleJavaVersion(curr, required), s"Java $required or above required, found $curr instead")
    }
  )

  object CompatibleJavaVersion extends VersionNumberCompatibility {
    def name = "Java specification compatibility"
    def isCompatible(current: VersionNumber, required: VersionNumber) =
      current.numbers.zip(required.numbers).forall(n => n._1 >= n._2)
    def apply(current: VersionNumber, required: VersionNumber) = isCompatible(current, required)
  }
}
