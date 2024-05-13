package writer

import model.RawData
import zio.stream.ZSink
import zio.*

import java.io.IOException


trait GenericWriter:
  def toOutput(aggregatedData: Map[String, RawData], fileCount: Int): String

  def printSink(fileCount: Int): ZSink[Any, IOException, Map[String, RawData], Nothing, Unit]