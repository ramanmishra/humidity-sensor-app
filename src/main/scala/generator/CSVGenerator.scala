package generator

import java.io.{BufferedWriter, File, FileWriter}
import scala.util.{Random, Try}


case class OutputRow(minHumidity: Int,
                     avgHumidity: BigDecimal,
                     maxHumidity: Int,
                     measurementCount: Int):
  override def toString = s"$minHumidity,$avgHumidity,$maxHumidity,$measurementCount"

object CSVGenerator {
  def generateCSV(filePath: String, expectedFilePath:String, fileSizeMB: Double): Unit = {
    val fileSizeBytes = fileSizeMB * 1024 * 1024
    val outputFile = new File(filePath)
    val writer = new BufferedWriter(new FileWriter(outputFile))
    val writeExpected = new BufferedWriter(new FileWriter(expectedFilePath))

    var eO = Map.empty[String, List[Int]]

    Try {
      writer.write("sensor-id,humidity\n")
      var currentSizeBytes = outputFile.length()

      while (currentSizeBytes < fileSizeBytes) {
        val sensorId = s"s${Random.nextInt(10)}" // Generating random sensor id
        val humidity = Random.nextInt(100)+1 // Generating random humidity value between 0 and 100
        eO += (sensorId -> eO.getOrElse(sensorId, Nil).prepended(humidity))
        val humidityStr = if humidity == 0 then "NaN" else humidity
        val line = s"""$sensorId,$humidityStr \n"""
        writer.write(line)
        currentSizeBytes = outputFile.length()
      }
    } match {
      case scala.util.Success(_) =>
        val outputHeader="sensor,max,avg,min,count,total\n"
        val outputRow = eO.view.mapValues(h => OutputRow(h.min, h.sum/h.length, h.max, h.length)).toMap
        val sortedOutPut = outputRow.toList.sortBy(-_._2.avgHumidity)

        writeExpected.write(outputHeader)
        sortedOutPut.foreach{
          case (str, value) =>
            writeExpected.write(s"$str,${value.toString()},${outputRow.map(_._2.measurementCount).sum}\n")
        }

        writer.close()
        writeExpected.close()
        println(s"CSV file generated successfully at: $filePath")
      case scala.util.Failure(exception) =>
        writer.close()
        writeExpected.close()
        outputFile.delete()
        println(s"Error occurred while generating CSV file: ${exception.getMessage}")
    }
  }

  def main(args: Array[String]): Unit = {
    val fileName = "Leader9.csv"
    val outputFilePath = s"src/test/resources/$fileName"
    val expectedOutput = s"src/test/resources/$fileName-expected.csv"
    val fileSizeMB = 1

    generateCSV(outputFilePath, expectedOutput, fileSizeMB)
  }
}
