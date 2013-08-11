import play.Project._
import sbt._

object ApplicationBuild extends Build {

  val appName = "play2_fun"
  val appVersion = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    anorm
  )


  val main = play.Project(appName, appVersion, appDependencies)

}
