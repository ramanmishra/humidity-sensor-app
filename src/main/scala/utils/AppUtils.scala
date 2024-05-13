package utils

import java.io.File

trait AppUtils:
  def getListOfFiles(dir: String): Seq[File] = {
    val d = new File(dir)
    if (d.exists && d.isDirectory) {
      d.listFiles.filter(f => f.isFile && f.getName.endsWith(".csv")).toList
    } else {
      throw new Exception("FileNotFound or Not A Directory")
    }
  }
