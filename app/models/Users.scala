package models

import scalikejdbc._
import org.joda.time.DateTime

case class Users(
                  id: Long,
                  name: String,
                  age: Int,
                  createdAt: DateTime,
                  deletedAt: Option[DateTime] = None) {

  def save()(implicit session: DBSession = Users.autoSession): Users = Users.save(this)(session)
  def destroy()(implicit session: DBSession = Users.autoSession): Unit = Users.destroy(id)(session)
}


object Users extends SQLSyntaxSupport[Users] {

  def apply(c: SyntaxProvider[Users])(rs: WrappedResultSet): Users = apply(c.resultName)(rs)
  def apply(c: ResultName[Users])(rs: WrappedResultSet): Users = new Users(
    id = rs.get(c.id),
    name = rs.get(c.name),
    age = rs.get(c.age),
    createdAt = rs.get(c.createdAt),
    deletedAt = rs.get(c.deletedAt)
  )

  val c = Users.syntax("c")
  private val isNotDeleted = sqls.isNull(c.deletedAt)

  def find(id: Long)(implicit session: DBSession = autoSession): Option[Users] = withSQL {
    select.from(Users as c).where.eq(c.id, id).and.append(isNotDeleted)
  }.map(Users(c)).single.apply()

  def findAll()(implicit session: DBSession = autoSession): List[Users] = withSQL {
    select.from(Users as c)
      .where.append(isNotDeleted)
      .orderBy(c.id)
  }.map(Users(c)).list.apply()

  def countAll()(implicit session: DBSession = autoSession): Long = withSQL {
    select(sqls.count).from(Users as c).where.append(isNotDeleted)
  }.map(rs => rs.long(1)).single.apply().get

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Users] = withSQL {
    select.from(Users as c)
      .where.append(isNotDeleted).and.append(sqls"${where}")
      .orderBy(c.id)
  }.map(Users(c)).list.apply()

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = withSQL {
    select(sqls.count).from(Users as c).where.append(isNotDeleted).and.append(sqls"${where}")
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
      ).where.eq(column.id, m.id).and.isNull(column.deletedAt)
    }.update.apply()
    m
  }

  def destroy(id: Long)(implicit session: DBSession = autoSession): Unit = withSQL {
    update(Users).set(column.deletedAt -> DateTime.now).where.eq(column.id, id)
  }.update.apply()

}
