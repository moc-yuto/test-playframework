package controllers

import com.sun.xml.internal.bind.v2.TODO
import org.omg.CosNaming.NamingContextPackage.NotFound
import play.api._
import play.api.mvc._
import views._
import scala.collection.immutable

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("It Works!"))
  }

  def hello(name:String) = Action {
    Ok(views.html.index("Hello " + name))
  }

  def show = Action {
    val list = List[String]("lemon","mikan","nanao")
    Ok(views.html.show("Hello Scala Templates!",list))
  }

  def getJson(name:String) = TODO

}