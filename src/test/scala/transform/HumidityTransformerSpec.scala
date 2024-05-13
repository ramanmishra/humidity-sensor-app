package transform

import model.{RawData, SensorData}
import zio.Scope
import zio.stream.ZStream
import zio.test.*
import zio.*

object HumidityTransformerSpec extends ZIOSpecDefault:
  private val aSensorData: Chunk[SensorData] = Chunk.succeed(SensorData.sample)
  private val aMapBigInt = Map("s1" -> Chunk.fromIterable(List(BigInt(33), BigInt(97))), "s2" -> Chunk.fromIterable(List(BigInt(22))))

  override def spec: Spec[TestEnvironment & Scope, Any] =
    suite("HumidityTransformer Test")(
      test("Should Successfully Transform the data") {
        for
          a <- HumidityTransformer().group(ZStream.fromZIO(ZIO.succeed(aSensorData)))
            .runCollect
        yield
          assertTrue(a.head == Map("s1" -> Chunk(BigInt(44))))
      },

      test("Should Successfully Transform the data") {
        for
          a <- HumidityTransformer().aggregateByKey(ZStream.fromZIO(ZIO.succeed(aMapBigInt)))
            .runCollect
        yield
          assertTrue(a.head == Map("s1" -> RawData(2, 130, 97, 33, 0), "s2" -> RawData(1, 22, 22, 22, 0)))
      },
    )
