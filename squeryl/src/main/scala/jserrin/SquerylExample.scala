package jserrin.sandbox
import org.squeryl._
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.annotations.Column
import org.squeryl.SessionFactory
import org.squeryl.adapters.DerbyAdapter

object SquerylExample extends App {
  Class.forName("org.apache.derby.jdbc.EmbeddedDriver")
  val session = Session.create(
    java.sql.DriverManager.getConnection("jdbc:derby:/tmp/messages-%s;create=true".format(System.currentTimeMillis)),
    //    java.sql.DriverManager.getConnection("jdbc:derby:memory;create=true"),
    new DerbyAdapter)

  using(session) { ExampleSchema.create }
  using(session) { println(Session.currentSession) }

  import ExampleSchema.messages
  using(session) {
    val m1 = SquerylMessage("m1", 1, 2)
    messages.insert(m1)
    println(from(messages)(m => where(m.id === "m1") select (m)))
  }

  session.close
}
object SquerylMessage {
  val UnknownTime = -1L
}
case class SquerylMessage(
  id: String,
  var t1: Long = SquerylMessage.UnknownTime,
  var t2: Long = SquerylMessage.UnknownTime)

object ExampleSchema extends Schema {
  val messages = table[SquerylMessage]("MESSAGES")

  on(messages)(m => declare(
    m.id is (unique, indexed),
    m.t1 defaultsTo (SquerylMessage.UnknownTime),
    m.t2 defaultsTo (SquerylMessage.UnknownTime)))
}