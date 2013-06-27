package jserrin.sandbox
import org.rrd4j.core.RrdDef
import org.rrd4j.ConsolFun._
import org.rrd4j.core.RrdDb
import org.rrd4j.graph.RrdGraphDef
import org.rrd4j.graph.RrdGraph
import java.awt.Color

object Launch extends App {

  Demo.main(Array.empty[String])

  //  val rrdpath = "target/rrd.db"
  //  val rrdDef = new RrdDef(rrdpath)
  //  rrdDef.addArchive(AVERAGE, .5, 1, 1000)
  //  rrdDef.addArchive(AVERAGE, .5, 6, 1000)
  //  rrdDef.addArchive(MAX, .5, 1, 1000)
  //
  //  val db = new RrdDb(rrdDef)
  //  val sample = db.createSample
  //
  //  val now = System.currentTimeMillis
  //  for {
  //    i <- (0 to 1000)
  //    _ = sample.setTime(now + i)
  //    _ = sample.setValue("inbytes", Math.random)
  //    _ = sample.setValue("outbytes", Math.random)
  //  } yield sample.update
  //  db.close
  //
  //  val gDef = new RrdGraphDef
  //  gDef.setWidth(500)
  //  gDef.setHeight(300)
  ////  gDef.setFilename(imgPath)
  //  gDef.setStartTime(now)
  //  gDef.setEndTime(now + 1000)
  //  gDef.setTitle("My Title");
  //  gDef.setVerticalLabel("bytes");
  //  
  //  gDef.datasource("bytes", rrdpath, "inbytes", AVERAGE)
  //  gDef.hrule(2568, Color.GREEN, "hrule")
  //  gDef.setImageFormat("png")
  //  
  //  val graph = new RrdGraph(gDef)

}