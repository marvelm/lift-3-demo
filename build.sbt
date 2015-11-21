name := "lift-3-demo"

scalaVersion := "2.11.7"

resolvers ++= Seq(
  "Java.net Maven2 Repository"     at "http://download.java.net/maven/2/",
  "Sonatype scala-tools repo"      at "https://oss.sonatype.org/content/groups/scala-tools/",
  "Sonatype scala-tools releases"  at "https://oss.sonatype.org/content/repositories/releases",
  "Sonatype scala-tools snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
)

{
  val liftVersion = "3.0-M6"
  libraryDependencies ++= Seq(
    "net.liftweb"             %% "lift-webkit"       % liftVersion             % "compile",
    "net.liftweb"             %% "lift-mapper"       % liftVersion             % "compile",
    "ch.qos.logback"          %  "logback-classic"   % "1.0.6",
    "junit"                   %  "junit"             % "4.7"                   % "test",
    "org.specs2"              %% "specs2"            % "2.3.11"                % "test",
    "org.pegdown"             %  "pegdown"           % "1.1.0"                 % "test",
    "org.scalatest"           %% "scalatest"         % "2.2.1"                 % "test",
    "org.eclipse.jetty"       %  "jetty-webapp"      % "8.1.7.v20120910"       %  "container,compile",
    "org.eclipse.jetty.orbit" %  "javax.servlet"     % "3.0.0.v201112011016"   %  "container,compile" artifacts Artifact("javax.servlet", "jar", "jar"),
    "net.liftmodules"         %  "mapperauth_3-0_2.11" % "0.4-SNAPSHOT"          %  "compile" intransitive(),
    "net.liftmodules"         %% "extras_3.0"        % "0.4-SNAPSHOT"          %  "compile" intransitive(),
    "com.h2database"          %  "h2"                % "1.2.138",
    "net.databinder.dispatch" %% "dispatch-core"     % "0.11.2"
  )
}

buildInfoSettings

sourceGenerators in Compile <+= buildInfo

buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion)

buildInfoPackage := "code"

seq(lessSettings:_*)

(LessKeys.filter in (Compile, LessKeys.less)) := "styles.less"

(LessKeys.mini in (Compile, LessKeys.less)) := true

(sourceDirectory in (Compile, LessKeys.less)) <<= (sourceDirectory in Compile)(_ / "webapp" / "media" / "styles")

seq(closureSettings:_*)

(ClosureKeys.prettyPrint in (Compile, ClosureKeys.closure)) := false

seq(webSettings :_*)

// add managed resources, where less and closure publish to, to the webapp
(webappResources in Compile) <+= (resourceManaged in Compile)

(resourceManaged in (Compile, LessKeys.less)) <<= (crossTarget in Compile)(_ / "resource_managed" / "main" / "media" / "styles")

(sourceDirectory in (Compile, ClosureKeys.closure)) <<= (sourceDirectory in Compile)(_ / "webapp" / "media" / "js")

(resourceManaged in (Compile, ClosureKeys.closure)) <<= (crossTarget in Compile)(_ / "resource_managed" / "main" / "media" / "js")

// If using JRebel uncomment next line
scanDirectories := Nil

// Remove Java directories, otherwise sbteclipse generates them
unmanagedSourceDirectories in Compile <<= (scalaSource in Compile)(Seq(_))

unmanagedSourceDirectories in Test <<= (scalaSource in Test)(Seq(_))

EclipseKeys.withSource := true

EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource
