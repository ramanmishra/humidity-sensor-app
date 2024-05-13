package transform

import model.{RawData, SensorData}
import zio.*
import zio.stream.{ZSink, ZStream}

case class HumidityTransformer() extends DataTransformer:
  def group = (s: ZStream[Any, Nothing, Chunk[SensorData]]) => s.map { chunk =>
    chunk.groupBy(_.sensorId).view.mapValues(_.map(_.humidity)).toMap
  }

  def aggregateByKey(chunks: ZStream[Any, Nothing, Map[String, Chunk[BigInt]]]): ZStream[Any, Nothing, Map[String, RawData]] =
    ZStream.fromZIO(
      chunks.run(ZSink.foldLeft(Map.empty[String, RawData]) { (acc, chunk: Map[String, Chunk[BigInt]]) =>
        chunk.foldLeft(acc) { case (accMap, (key, value)) =>
          val rawData = accMap.getOrElse(key, RawData.default)
          val newCount = rawData.sensorCount + value.length
          val newSum = rawData.total + value.filter(_ >= 0).sum
          val newMax = rawData.maxReading.BigMax(value.max)
          val newMin = rawData.minReading.BigMin(value.min.BigMax(0))
          val newNanCount = rawData.nanCount + value.count(_.equals(BigInt(-100)))

          accMap.updated(key, RawData(newCount, newSum, newMax, newMin, newNanCount))
        }
      }))
