sbt-java-version
=======
[![Build Status](https://travis-ci.org/tim-group/sbt-java-version.svg?branch=master)](https://travis-ci.org/tim-group/sbt-java-version)

Summary
-------
Ensures that code is compiled using targeted Java version, for example:

    object MyBuild extends Build {
       override val settings = super.settings ++ Seq(
          javaVersion in ThisBuild  := "1.6", // global setting, checks that java version is compatible on start-up
                                              //   also default for rest of settings
          javaVersion in packageBin := "1.7", // sets X-Java-Version in manifest
          javaVersion in target := "1.6"      // sets target version for Java and Scala compilation
          javaVersion in javaSources := "1.6" // sets source version for Java sources
       )
    }


Sets an MANIFEST.MF property `X-Java-Version` to a short version of `javaVersion` (e.g. "7" when `javaVersion` is set to "1.7")


Incrementally upgrade from Java 1.6 to Java 1.7
-----------------------------------------------------------------------

### First, run with Java 7 (assuming your production jar runner scripts recognizes `X-Java-Version`)

    javaVersion in ThisBuild := "1.6",
    javaVersion in packageBin := "1.7"
  
  
### Next, compile (and run) with Java 7

    javaVersion in ThisBuild := "1.7",
    javaVersion in target    := "1.6"
  
  
### Lastly, compile to Java 7

    javaVersion in ThisBuild := "1.7"
  
  
Usage
-----
Not yet published to a Maven or Ivy central repository---please open up an Issue if you find this pl
ugin useful and would like it published.

Workaround: compile locally and publish locally:

     sbt -DBUILD_VERSION=1 publishLocal
