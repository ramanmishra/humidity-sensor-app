package reader

import zio.*
import zio.test.Assertion.*
import zio.test.*

import java.io.File
import java.nio.file.NoSuchFileException

object HumidityReaderSpec extends ZIOSpecDefault:
  final val file = new File("src/test/resources/Leader8.csv")
  final val fileNotFount = new File("src/test/resources/test.csv")
  override def spec: Spec[TestEnvironment & Scope, Any] =
    suite("HumidityReader Test")(
      test("Should Read A valid csv file"){
        for
          readerStream <- HumidityReader().readAsStream(file::Nil)
            .runCollect
        yield
          assertTrue(readerStream.flatten.length==5)
      },
      test("Should Give An Error csv file"){
        for
          readerStream <- HumidityReader().readAsStream(fileNotFount::Nil, 0.2).runDrain.exit
        yield
          assert(readerStream)(fails(isSubtype[NoSuchFileException](anything)))
      }
    )