package models

import scalikejdbc._
import org.joda.time.DateTime

case class Users(
                  id: Long,
                  name: String,
                  age: Int,
                  createdAt: DateTime,
                  updatedAt: Option[DateTime] = None) {

  def save()(implicit session: DBSession = Users.autoSession): Users = Users.save(this)(session)
}


object Users extends SQLSyntaxSupport[Users] {

  def apply(c: SyntaxProvider[Users])(rs: WrappedResultSet): Users = apply(c.resultName)(rs)
  def apply(c: ResultName[Users])(rs: WrappedResultSet): Users = new Users(
    id = rs.get(c.id),
    name = rs.get(c.name),
    age = rs.get(c.age),
    createdAt = rs.get(c.createdAt),
    updatedAt = rs.get(c.updatedAt)
  )

  val c = Users.syntax("c")

  def find(id: Long)(implicit session: DBSession = autoSession): Option[Users] = withSQL {
    select.from(Users as c).where.eq(c.id, id)
  }.map(Users(c)).single.apply()

  def findAll()(implicit session: DBSession = autoSession): List[Users] = withSQL {
    select.from(Users as c)
      .orderBy(c.id)
  }.map(Users(c)).list.apply()

  def countAll()(implicit session: DBSession = autoSession): Long = withSQL {
    select(sqls.count).from(Users as c)
  }.map(rs => rs.long(1)).single.apply().get

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Users] = withSQL {
    select.from(Users as c)
      .where.append(sqls"${where}")
      .orderBy(c.id)
  }.map(Users(c)).list.apply()

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = withSQL {
    select(sqls.count).from(Users as c).where.append(sqls"${where}")
  }.map(_.long(1)).single.apply().get

  def create(name: String, age: Int, createdAt: DateTime = DateTime.now)(implicit session: DBSession = autoSession): Users = {
    val id = withSQL {
      insert.into(Users).namedValues(
        column.name -> name,
        column.age -> age,
        column.createdAt -> createdAt)
    }.updateAndReturnGeneratedKey.apply()

    Users(id = id, name = name, age = age, createdAt = createdAt)
  }

  def save(m: Users)(implicit session: DBSession = autoSession): Users = {
    withSQL {
      update(Users).set(
        column.name -> m.name,
        column.age -> m.age
      ).where.eq(column.id, m.id)
    }.update.apply()
    m
  }
}
