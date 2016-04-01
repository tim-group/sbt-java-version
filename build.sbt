organization := "com.timgroup"
name         := "sbt-java-version"
sbtPlugin    := true
version      := "0.0." + System.getProperty("BUILD_NUMBER", sys.env.getOrElse("BUILD_NUMBER", "0-SNAPSHOT"))
sbtVersion   in Global := "0.13.7"
scalaVersion in Global := "2.10.4"
publishTo := Some("TIM Group Repo" at "http://repo.youdevise.com:8081/nexus/content/repositories/yd-release-candidates")
credentials += Credentials(new File("/etc/sbt/credentials"))
