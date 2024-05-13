package writer

import model.RawData
import zio.Scope
import zio.test.*
import zio.*

object ConsoleWriterSpec extends ZIOSpecDefault:
  val aResult = Map(
    "s1" -> RawData(BigInt(2), BigInt(130), BigInt(97), BigInt(33), BigInt(0)))
  val aOutput =
    """Num of processed files: 1
      |Num of processed measurements: 2
      |Num of failed measurements: 0
      |
      |Sensors with highest avg humidity: s1
      |
      |sensor-id,min,avg,max
      |s1,33,65,97""".stripMargin

  override def spec: Spec[TestEnvironment & Scope, Any] =
    suite("ConsoleWriter Test")(
      test("Should return the correct output") {
        for
          a <- ZIO.succeed(ConsoleWriter().toOutput(aResult, 1))
          _ <- ZIO.logInfo(a)
        yield
          assertTrue(a == aOutput.stripMargin)
      }
    )
