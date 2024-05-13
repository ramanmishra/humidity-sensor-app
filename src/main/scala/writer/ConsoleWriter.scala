package writer

import model.RawData
import zio.*
import zio.stream.ZSink

import java.io.IOException

case class ConsoleWriter() extends GenericWriter:
  def printSink(fileCount: Int): ZSink[Any, IOException, Map[String, RawData], Nothing, Unit] =
    ZSink.foreach(groupedData => Console.printLine(s"${toOutput(groupedData, fileCount)}"))

  def ifNan(minReading: BigInt, avgReading: BigInt, maxReading: BigInt) = {
    if maxReading < 0 then
      "NaN,NaN,NaN"
    else
      s"$minReading,$avgReading,$maxReading"
  }

  def toOutput(aggregatedData: Map[String, RawData], fileCount: Int): String =
    s"""Num of processed files: $fileCount
       |Num of processed measurements: ${aggregatedData.map(_._2.sensorCount).sum}
       |Num of failed measurements: ${aggregatedData.map(_._2.nanCount).sum}
       |
       |Sensors with highest avg humidity: ${aggregatedData.map(e => e._1 -> e._2.total / e._2.sensorCount).maxBy(_._2)._1}\n\n""".stripMargin +
      s"sensor-id,min,avg,max\n" +
      aggregatedData.map {
        case (sensorId, RawData(sensorCount, total, maxReading, minReading, _)) =>
          sensorId -> (minReading, total / sensorCount, maxReading)
      }.toList.sortBy(-_._2._2).map { kv =>
        val (minReading, avgReading, maxReading) = kv._2
        s"${kv._1},${ifNan(minReading, avgReading, maxReading)}"
      }.mkString("\n")
