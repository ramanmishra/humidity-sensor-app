import sbt.*

object Dependencies {
  final val zioV = "2.1.0-RC5"

  private val zioLibs: Seq[ModuleID] = Seq(
    "dev.zio" %% "zio" % zioV,
    "dev.zio" %% "zio-streams" % zioV)

  private val testLib: Seq[ModuleID] = Seq(
    "dev.zio" %% "zio-test" % zioV % Test,
    "dev.zio" %% "zio-test" % zioV % Test,
    "dev.zio" %% "zio-test-sbt" % zioV % Test)

  val all: Seq[sbt.ModuleID] = zioLibs ++ testLib
}