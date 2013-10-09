organization := "hr.element.doit"

name         := "doit-csv"

version      := "0.1.8-SNAPSHOT"

// ### Build settings ###

libraryDependencies += "org.scalatest" %% "scalatest" % "2.0.RC1" % "test"

crossScalaVersions := Seq("2.10.3", "2.9.3", "2.9.2", "2.9.1-1", "2.9.1", "2.9.0-1", "2.9.0")

scalaVersion <<= crossScalaVersions(_.head)

scalacOptions <<= scalaVersion map { sV => 
  val scala2_8 = Seq(
    "-unchecked"
  , "-deprecation"
  , "-optimise"
  , "-encoding", "UTF-8"
  , "-Xcheckinit"
  , "-Xfatal-warnings"
  , "-Yclosure-elim"
  , "-Ydead-code"
  , "-Yinline"
  )
  //  
  val scala2_9 = Seq(
    "-Xmax-classfile-name", "72"
  )
  //  
  val scala2_9_1 = Seq(
    "-Yrepl-sync"
  , "-Xlint"
  , "-Xverify"
  , "-Ywarn-all"
  )
  //  
  val scala2_10 = Seq(
    "-feature"
  , "-language:postfixOps"
  , "-language:implicitConversions"
  , "-language:existentials"
  )
  //  
  scala2_8 ++ (sV match {
    case x if (x startsWith "2.10.")                => scala2_9 ++ scala2_9_1 ++ scala2_10
    case x if (x startsWith "2.9.") && x >= "2.9.1" => scala2_9 ++ scala2_9_1
    case x if (x startsWith "2.9.")                 => scala2_9
    case _ => Nil
  })
}

javaHome := sys.env.get("JDK16_HOME").map(file(_))

javacOptions := Seq(
  "-deprecation"
, "-encoding", "UTF-8"
, "-Xlint:unchecked"
, "-source", "1.6"
, "-target", "1.6"
)

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "UTF-8", "-optimise")

unmanagedSourceDirectories in Compile := (scalaSource in Compile).value  :: Nil

unmanagedSourceDirectories in Test    := (scalaSource in Test).value :: Nil

// ### Publishing ###

resolvers := Seq("Element Nexus" at "http://repo.element.hr/nexus/content/groups/public/")

externalResolvers <<= resolvers map { r =>
  Resolver.withDefaultResolvers(r, mavenCentral = false)
}

publishTo <<= version { version => Some(
   if (version endsWith "SNAPSHOT") 
     "Element Snapshots" at "http://repo.element.hr/nexus/content/repositories/snapshots/"
   else 
     "Element Releases"  at "http://repo.element.hr/nexus/content/repositories/releases/"
)}

credentials += Credentials(Path.userHome / ".config" / "doit-csv" / "nexus.config")

publishArtifact in (Compile, packageDoc) := false

// ### Misc ###

initialCommands := "import hr.element.doit.csv._"

seq(graphSettings: _*)

seq(eclipseSettings: _*)
