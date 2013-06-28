package jserrin.sandbox

import java.awt.BorderLayout
import java.awt.Color
import java.awt.FlowLayout

import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartPanel
import org.jfree.chart.plot.PlotOrientation
import org.jfree.chart.plot.XYPlot
import org.jfree.chart.renderer.xy.XYAreaRenderer
import org.joda.time.DateTime

import javax.swing.JDialog
import javax.swing.JPanel
import spray.json._
import spray.json.DefaultJsonProtocol._

object Launch extends App {

  val graphiteURL = """http://192.168.220.140"""
  //  val query = """render/?target=keepLastValue(carbon.agents.graphite-a.avgUpdateTime)&target=keepLastValue(carbon.agents.graphite-a.committedPoints)&target=keepLastValue(carbon.agents.graphite-a.cpuUsage)&target=keepLastValue(carbon.agents.graphite-a.creates)&target=keepLastValue(carbon.agents.graphite-a.errors)&target=keepLastValue(carbon.agents.graphite-a.memUsage)&target=keepLastValue(carbon.agents.graphite-a.metricsReceived)&target=keepLastValue(carbon.agents.graphite-a.pointsPerUpdate)&target=keepLastValue(carbon.agents.graphite-a.updateOperations)&from=-10minutes&format=json"""
  val query = """render/?target=carbon.agents.graphite-a.avgUpdateTime&target=carbon.agents.graphite-a.committedPoints&from=-10minutes&format=json"""
  val request = scalaj.http.Http.get(s"$graphiteURL/$query")

  val responseString = request.asString
  //  println(responseString)

  //  [{"target": "carbon.agents.graphite-a.avgUpdateTime", "datapoints": [[9.1736133282001203e-05, 1372452540], [6.5473409799429093e-05, 1372452600], [6.0888437124399039e-05, 1372452660], [0.00024613967308631312, 1372452720], [6.1346934391902046e-05, 1372452780], [6.9068028376652641e-05, 1372452840], [6.0925116905799281e-05, 1372452900], [6.0008122370793266e-05, 1372452960], [6.5491749690129204e-05, 1372453020], [6.1053496140700124e-05, 1372453080]]}, {"target": "carbon.agents.graphite-a.committedPoints", "datapoints": [[13.0, 1372452540], [13.0, 1372452600], [13.0, 1372452660], [13.0, 1372452720], [13.0, 1372452780], [13.0, 1372452840], [13.0, 1372452900], [13.0, 1372452960], [13.0, 1372453020], [13.0, 1372453080]]}]

  val dataset = JSONParser.parseResponse(responseString)
  println(dataset)

  val panels = dataset.map(display)
  val mainPanel = new JPanel(new FlowLayout)
  for {
    data <- dataset
    panel = display(data)
  } yield mainPanel.add(panel)
  val dialog = new JDialog
  dialog.setLayout(new BorderLayout)
  dialog.add(mainPanel, BorderLayout.CENTER)
  dialog.pack
  dialog.setVisible(true)

  def display(data: Data): JPanel = {
    import org.jfree.data.xy.XYSeriesCollection
    import org.jfree.data.xy.XYSeries
    val dataset = new XYSeriesCollection
    val series = new XYSeries(data.metric, false)
    for {
      p <- data.points
      time = p.time.getMillis
      value = p.value
    } yield series.add(time, value)
    dataset.addSeries(series)

    val title = data.metric
    val xAxisLabel = "time"
    val yAxisLabel = "TODO"
    val orientation = PlotOrientation.VERTICAL
    val legend = false
    val tooltips = false
    val urls = false
    val chart = ChartFactory.createScatterPlot(title, xAxisLabel, yAxisLabel,
      dataset, orientation, legend, tooltips, urls)
    val panel = new ChartPanel(chart)

    val plot = chart.getPlot.asInstanceOf[XYPlot]
    val renderer = new XYAreaRenderer
    plot.setForegroundAlpha(0.5f)
    renderer.setSeriesPaint(0, Color.blue)
    plot.setRenderer(renderer)

    panel
  }
}
case class Point(value: Double, time: DateTime)
case class Data(metric: String, points: Seq[Point])
object JSONParser extends DefaultJsonProtocol {

  case class DataDAO(target: String, datapoints: Array[Array[Double]]) {
    def toData: Data = Data(target, datapoints.map {
      // Remember, graphite times are in seconds!
      case Array(value, time) => new Point(value, new DateTime(time.toLong * 1000))
    })
  }
  implicit lazy val dataFormat = jsonFormat2(DataDAO)

  def parseResponse(str: String): Seq[Data] = JsonParser(str) match {
    case JsArray(dps) => dps.map(_.convertTo[DataDAO]).map(_.toData)
    case _@ other => deserializationError("Unknown type: " + other)
  }
}