package reader

import model.SensorData
import zio.Chunk
import zio.stream.ZStream

import java.io.File

trait FileReader:
  def readAsStream(files: Seq[File], chunkSize: Double): ZStream[Any, Throwable | String, Chunk[SensorData]]
  
