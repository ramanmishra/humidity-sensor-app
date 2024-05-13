
import reader.HumidityReader
import transform.HumidityTransformer
import utils.AppUtils
import writer.ConsoleWriter
import zio.stream.*
import zio.*

import java.util.concurrent.TimeUnit

object SensorApp extends ZIOAppDefault with AppUtils:
  def run: ZIO[Environment & ZIOAppArgs & Scope, Any, Any] = {
    for
      args <- getArgs.map(_.mkString)
      files <- ZIO.attempt(getListOfFiles(args))
      _ <- ZIO.logInfo(s"Reading from Source Directory: $args")

      reader <- ZIO.succeed(HumidityReader())
      transformer <- ZIO.succeed(HumidityTransformer())
      writer <- ZIO.succeed(ConsoleWriter())

      t1 <- Clock.currentTime(TimeUnit.MILLISECONDS)
      ec <-
        reader.readAsStream(files, 5)
          .via(ZPipeline.fromFunction(transformer.group))
          .via(ZPipeline.fromFunction(transformer.aggregateByKey))
          .run(writer.printSink(files.length))
          .logError("Error:")
          .exitCode
      t2 <- Clock.currentTime(TimeUnit.MILLISECONDS)

      _ <- ZIO.logInfo(s"TimeTaken In Seconds: ${(t2 - t1) / 1000}")
    yield
      ec
  }

