package models

case class Users(
                  id: Long,
                  name: String,
                  url: Option[String] = None,
                  createdAt: DateTime,
                  deletedAt: Option[DateTime] = None) {

  def save()(implicit session: DBSession = Users.autoSession): Users = Users.save(this)(session)
  def destroy()(implicit session: DBSession = Users.autoSession): Unit = Users.destroy(id)(session)
}