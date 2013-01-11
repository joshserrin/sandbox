package jserrin.sandbox
import scala.slick.direct.AnnotationMapper._
import scala.slick.driver.DerbyDriver.simple._
import Database.threadLocalSession

case class Message(id: String, t1: Long, t2: Long)

object Messages extends Table[(String, Long, Long)]("MESSAGES") {
  def id = column[String]("ID", O.PrimaryKey)
  def t1 = column[Long]("T1")
  def t2 = column[Long]("T2")
  def * = id ~ t1 ~ t2
}

object SlickExample extends App {

	val database = Database.forURL("jdbc:derby:/tmp/messages-" + System.currentTimeMillis + ";create=true",
				       driver = "org.apache.derby.jdbc.EmbeddedDriver")
	database withSession { Messages.ddl.create }
	def populateDatabase: Unit = database withSession {
		Messages.insertAll(("m1", 0L, 1L), ("m2", 2L, 0L))
	}
	def getMessage(id: String): Option[Message] = database withSession {
		val q = Query(Messages)
		val rList = scala.collection.mutable.ListBuffer.empty[Message]
		q.filter(_.id === id) foreach { case (id, t1, t2) => rList += Message(id, t1, t2) }
		//println(q.filter(_.id == id).map { case (id, t1, t2) => Message(id, t1, t2) })
		rList.headOption
	}
	def printDatabase: Unit = database withSession {
		println("================")
		Query(Messages) foreach { case (m, t1, t2) => println(List(m, t1, t2).mkString(",")) }
		println("================")
	}
	def update(id: String, newT1: Long, newT2: Long) = database withSession {
		/*
		val query = for {
			m <- Messages if m.id ===id
		} yield (m.t1, m.t2)
		query.update((newT1, newT2))
		*/
		(for { m <- Messages if m.id === id } yield m.t1).update(newT1)
		(for { m <- Messages if m.id === id } yield m.t2).update(newT2)
	}

	populateDatabase

	printDatabase
	
	println("m1 = " + getMessage("m1"))
	println("m3 = " + getMessage("m3"))

	update("m1", 3, 4)
	update("m2", 6, 7)
	update("m3", 10, 11)

	println("Values updated!")

  	printDatabase

	println("Done!")

}
