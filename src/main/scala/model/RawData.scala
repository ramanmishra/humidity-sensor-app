package model

case class RawData(sensorCount:BigInt,
                   total: BigInt,
                   maxReading: BigInt,
                   minReading: BigInt,
                   nanCount: BigInt)

object RawData:
  val default = RawData(BigInt(0), BigInt(0), BigInt(-1), BigInt(101), BigInt(0))
