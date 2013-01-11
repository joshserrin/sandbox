package jerrin.sandbox
import net.fwbrasil.activate.ActivateContext
import net.fwbrasil.activate.storage.memory.TransientMemoryStorage
import net.fwbrasil.activate.storage.relational.PooledJdbcRelationalStorage
import net.fwbrasil.activate.storage.relational.idiom.derbyDialect

object MemoryContext extends ActivateContext {
  val storage = new TransientMemoryStorage

  //  val storage = new PooledJdbcRelationalStorage {
  //    val jdbcDriver = "org.apache.derby.jdbc.EmbeddedDriver"
  //    val user = "USER"
  //    val password = "PASS"
  //    val url = "jdbc:derby:/tmp/messages-" + java.util.UUID.randomUUID + ";create=true"
  //    val dialect = derbyDialect
  //  }
}

import MemoryContext._
object Message {
  val unknown: Long = -1
}
case class Message(msgId: String, t1: Long = Message.unknown, var t2: Long = Message.unknown) extends Entity

class CreateMessagesTableMigration extends Migration {
  def timestamp = System.currentTimeMillis

  def up = {
    table[Message]
      .createTable(t => {
        t.column[String]("msgId")
        t.column[Long]("t1")
        t.column[Long]("t2")
      })
  }
}
object ActiveExample extends App {
  println("hi")

  transactional {
    println(new Message("m1", 0, 1))
    println(new Message("m2"))
  }
  println()
  println("Created...")
  println()
  def printAll = transactional { all[Message] foreach println }
  printAll

  transactional {
    println(select[Message].where(_.msgId :== "m1"))
    println(select[Message].where(_.msgId :== "m3"))
  }

  transactional {
    select[Message].where(_.msgId :== "m1").headOption match {
      case None => println("huh?!?!")
      case Some(message) => message.t2 = 5
    }
  }

  printAll
}
