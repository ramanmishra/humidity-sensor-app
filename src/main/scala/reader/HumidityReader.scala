package reader

import model.SensorData
import zio.{Chunk, ZIO}
import zio.stream.{ZPipeline, ZStream}

import java.io.File

import scala.util.Try

case class HumidityReader() extends FileReader:
  private def toData = (data: Chunk[String]) => {
    data.mapZIO { line =>
      val splitData = line.trim.split(",")
      ZIO.fromEither {
        splitData.length match
          case 2 =>
            Right(SensorData(splitData(0), Try{BigInt(splitData(1).toInt)}.getOrElse(-100)))
          case _ => Left("Invalid Line")
      }
    }
  }

  def readAsStream(files: Seq[File], chunkSize: Double = 1) =
    for
      file <- ZStream.fromIterable(files)
      fileStream <- ZStream
        .fromFile(file, (1024 * 1024 * chunkSize).toInt)
        .via(ZPipeline.utf8Decode)
        .via(ZPipeline.splitLines)
        .drop(1)
        .mapChunksZIO(toData)
        .chunks
    yield
      fileStream
