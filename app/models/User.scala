package models

import scalikejdbc._
import org.joda.time.DateTime

case class User(
                  id: Long,
                  name: String,
                  age: Int,
                  createdAt: DateTime,
                  updatedAt: Option[DateTime] = None) {

  def save()(implicit session: DBSession = User.autoSession): User = User.save(this)(session)
}


object User extends SQLSyntaxSupport[User] {

  override val tableName = "user"
  def apply(c: SyntaxProvider[User])(rs: WrappedResultSet): User = apply(c.resultName)(rs)
  def apply(c: ResultName[User])(rs: WrappedResultSet): User = new User(
    id = rs.get(c.id),
    name = rs.get(c.name),
    age = rs.get(c.age),
    createdAt = rs.get(c.createdAt),
    updatedAt = rs.get(c.updatedAt)
  )

  val c = User.syntax("c")

  def find(id: Long)(implicit session: DBSession = autoSession): Option[User] = withSQL {
    select.from(User as c).where.eq(c.id, id)
  }.map(User(c)).single.apply()

  def findAll()(implicit session: DBSession = autoSession): List[User] = withSQL {
    select.from(User as c)
      .orderBy(c.id)
  }.map(User(c)).list().apply()

  def countAll()(implicit session: DBSession = autoSession): Long = withSQL {
    select(sqls.count).from(User as c)
  }.map(rs => rs.long(1)).single.apply().get

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[User] = withSQL {
    select.from(User as c)
      .where.append(sqls"${where}")
      .orderBy(c.id)
  }.map(User(c)).list.apply()

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = withSQL {
    select(sqls.count).from(User as c).where.append(sqls"${where}")
  }.map(_.long(1)).single.apply().get

  def create(name: String, age: Int, createdAt: DateTime = DateTime.now)(implicit session: DBSession = autoSession): User = {
    val id = withSQL {
      insert.into(User).namedValues(
        column.name -> name,
        column.age -> age,
        column.createdAt -> createdAt)
    }.updateAndReturnGeneratedKey.apply()

    User(id = id, name = name, age = age, createdAt = createdAt)
  }

  def save(m: User)(implicit session: DBSession = autoSession): User = {
    withSQL {
      update(User).set(
        column.name -> m.name,
        column.age -> m.age
      ).where.eq(column.id, m.id)
    }.update.apply()
    m
  }
}
