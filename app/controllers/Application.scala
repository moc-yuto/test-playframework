package controllers

import models.Users
import play.api._
import play.api.mvc._
import views._

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("It Works!"))
  }

  def hello(name:String) = Action {
    Ok(views.html.index("Hello " + name))
  }

  def show = Action {
    val list = Users.findAll().map(_.name)
    Ok(views.html.show("Hello Scala Templates!", list))
  }

  def getJson(name:String) = TODO

}