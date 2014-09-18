name := "untappd-scala"

version := "1.0"

scalaVersion := "2.10.3"

libraryDependencies += "org.scalatest" % "scalatest_2.10" % "2.2.2"

libraryDependencies += "com.ning" % "async-http-client" % "1.8.13"

libraryDependencies += "org.mockito" % "mockito-all" % "1.9.5"

resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies += "com.typesafe.play" %% "play-json" % "2.2.1"