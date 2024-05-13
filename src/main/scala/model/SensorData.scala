package model

case class SensorData(sensorId: String, humidity: BigInt)

object SensorData:
  val sample: SensorData = SensorData("s1", 44)