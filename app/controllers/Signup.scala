package controllers

import play.api.mvc._

object Signup extends Controller {
  def main(args: Array[String]) = {
    List(2, 3, 50, 100, 40, 20, 120, 20).map(_ * 3).filter(_ < 200).foreach(println)
  }
}
