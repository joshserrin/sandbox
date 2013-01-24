package com.cra.util.file

import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import java.io._
import java.nio.file._
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit
import scala.collection.JavaConverters._
import StandardWatchEventKinds._
import scala.collection.mutable.{ Map => mMap }
import scala.util._

/**
 * The goal of this experiment is to monitor the contents of a file and process
 * each new line that is written to it.  Basically, we have a system that can
 * summarize system performance by tailing/monitoring its log files, extracting
 * important information from the logs and
 */
@RunWith(classOf[JUnitRunner])
object TailerSpec extends Specification {

  sequential

  /*
   * NOTE: For this test I simple used echo "some string" >> test-file.log to populate it.
   * If you don't do this... well, obviously... the tests won't finish.
   */

  private def lineRead(line: String): Unit =
    if (null == line) println("null line received!")
    else println(s"line received: $line")

  /*
   * I don't like how we busy wait for the file to be written to.  But this might be 
   * the easiest way to code to it.  If you have any other suggestions, please let me know
   * 
   * Pros: Current contents of the file are processed
   * Cons: Busy wait is ugly
   */
  "tailing a file as it is dynamically populated using BufferedReaders only" in {
    println("==========================================================")
    println("Processing file using busy waits...")
    println("==========================================================")

    val fs = FileSystems.getDefault
    val reader = Files.newBufferedReader(fs.getPath("log/test-file.log"), Charset.defaultCharset)
    def processLine(iteration: Int): Unit = {
      val line = reader.readLine
      lineRead(line)
      if (null == line) {
        // Nothing new in the file so wait some time before trying again
        println("Waiting for new content...")
        TimeUnit.SECONDS.sleep(1)
        processLine(iteration)
      }
    }

    // only process 5 lines before finishing
    (1 to 5) foreach processLine

    reader.close
  }

  /*
   * this utilized watch services
   * 
   * Pros: No busy wait.  Modification notifications are sent to indicate more lines to process
   * Cons: current contents not processed
   */
  "tailing a file as it is dynamically populated using WatchServices" in {
    println("==========================================================")
    println("Processing file using WatchServices...")
    println("==========================================================")

    val fs = FileSystems.getDefault
    val logPath = fs.getPath("log/test-file.log")
    val ws = fs.newWatchService
    val path = logPath.getParent
    val key = path.register(ws, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY)

    Files.isDirectory(path) must beTrue
    Files.isReadable(logPath) must beTrue

    def catchUp(reader: BufferedReader): Unit = Try(reader.readLine) match {
      case Success(line) if null != line => {
        lineRead(line)
        catchUp(reader)
      }
      case Success(line) if null == line => // all lines processed so far
      case Failure(ex) => println(s"Error! $ex")
    }

    val readers = mMap.empty[Path, BufferedReader]
    readers += logPath -> Files.newBufferedReader(logPath, Charset.defaultCharset)
    readers.values foreach catchUp

    @tailrec
    def process(taken: WatchKey, eventsProcessed: Int): Unit = {
      for {
        event <- taken.pollEvents.asScala if event.kind == ENTRY_MODIFY
        modifiedPath = path.resolve(event.context.asInstanceOf[Path])
        reader <- readers.get(modifiedPath)
      } yield catchUp(reader)

      taken.reset

      if (eventsProcessed >= 5) ()
      else process(ws.take, eventsProcessed + 1)
    }
    // ws.take is a blocking call (at least on my dev machine (linx mint 14))
    process(ws.take, 1)

    ws.close
  }
}