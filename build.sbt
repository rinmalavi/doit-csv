organization := "hr.element.doit"

name         := "doit-csv"

version      := "0.1.1-1"


// ### Build settings ###

libraryDependencies += "org.scalatest" %% "scalatest" % "1.7.1" % "test"

crossScalaVersions := Seq("2.9.1", "2.9.0-1", "2.9.0")

scalaVersion <<= (crossScalaVersions)(_.head)

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "UTF-8", "-optimise")

unmanagedSourceDirectories in Compile <<= (scalaSource in Compile)( _ :: Nil)

unmanagedSourceDirectories in Test    <<= (scalaSource in Test   )( _ :: Nil)


// ### Publishing ###

publishTo := Some("Element Releases"  at "http://maven.element.hr/nexus/content/repositories/releases/")

credentials += Credentials(Path.userHome / ".publish" / "element.credentials")

publishArtifact in (Compile, packageDoc) := false


// ### Misc ###

initialCommands := "import hr.element.doit.csv.CSVFactory"
