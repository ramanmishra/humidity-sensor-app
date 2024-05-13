package transform

import model.{RawData, SensorData}
import zio.Chunk
import zio.stream.ZStream

trait DataTransformer:
  extension (b1: BigInt)
    def BigMax(b2: BigInt) = if b1 > b2 then b1 else b2
    def BigMin(b2: BigInt) = if b1 < b2 then b1 else b2

  def group: ZStream[Any, Nothing, Chunk[SensorData]] => ZStream[Any, Nothing, Map[String, Chunk[BigInt]]]

  def aggregateByKey(chunks: ZStream[Any, Nothing, Map[String, Chunk[BigInt]]]): ZStream[Any, Nothing, Map[String, RawData]] 