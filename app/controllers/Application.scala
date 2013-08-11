package controllers

import org.jsoup.nodes.Document
import play.api.mvc._
import service.Dolanizer

object Application extends Controller {

  def index() = Action {
    request =>
      if (request.queryString.contains("url"))
        Redirect(routes.Application.display(request.queryString("url")(0)))
      else
        Ok(views.html.index.render())
  }

  def display(urlParam: String) = Action {
    request =>
      val url =
        if (request.rawQueryString.length > 0) urlParam + "?" + request.rawQueryString
        else urlParam
      val dolanizer = new Dolanizer(url)
      val document: Document = dolanizer()
      document.title("Dolanizátor :: " + document.title())
      document.body prepend views.html.wrapper.render().toString()
      Ok(document.outerHtml) as "text/html"
  }
}
