organization := "com.timgroup"
name         := "sbt-java-version"
sbtPlugin    := true
version      := "0.0." + System.getProperty("BUILD_NUMBER", sys.env.getOrElse("BUILD_NUMBER", "0-SNAPSHOT"))
sbtVersion   in Global := "1.0.3"
scalaVersion := "2.12.3"
crossSbtVersions := Vector("0.13.16", "1.0.3")
publishTo := Some("TIM Group Repo" at "http://repo.youdevise.com:8081/nexus/content/repositories/yd-release-candidates")
credentials += Credentials(new File("/etc/sbt/credentials"))
updateOptions := updateOptions.value.withGigahorse(false)
scalacOptions += "-target:jvm-1.8"

