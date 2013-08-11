package service

import controllers.routes
import java.net.{MalformedURLException, HttpURLConnection, URL}
import org.jsoup.Jsoup
import org.jsoup.nodes.{TextNode, Element, Node, Document}
import scala.collection.JavaConversions._
import scala.io.Source.fromInputStream

class Dolanizer(urlParam: String) {

  val Consonant = "[bcdefghjklmnpqrtvwxyz]"
  val Vowel = "[aáeéiíoóöőuúüű]"

  val url = if (urlParam contains "http://") urlParam else s"http://$urlParam"
  //  val url = "http://hvg.hu?asd=23"

  implicit def dolanString(s: String) = new DolanString(s)

  implicit def dolanString2String(s: DolanString) = s.toString

  def apply(): Document = {
    val document: Document = createDocument(url)
    for (node <- document.childNodes()) {
      processNode(node)
    }
    document
  }

  def processNode(node: Node): Unit = {
    node match {
      case element: Element => {
        try {
          val href = element.attr("href")
          if (element.tagName == "a")
            element.attr("href", routes.Application.display(createAbsoluteUrl(href, url)).url)
          else
            for (attr <- List("href", "src", "action"))
              if (element.hasAttr(attr))
                element.attr(attr, new URL(new URL(url), element.attr(attr)).toExternalForm)
        } catch {
          case exception: MalformedURLException => exception.printStackTrace()
        }
        element.childNodes().foreach(processNode)
      }
      case node: TextNode => node.text(processText(node.text()))
      case _ =>
    }
  }

  def createAbsoluteUrl(url: String, base: String) = new URL(new URL(base), url).toExternalForm

  protected def createDocument(url: String): Document = {
    val url = new URL(this.url)
    HttpURLConnection.setFollowRedirects(true)
    val hc = url.openConnection()
    hc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.72 Safari/537.36")
    var content = ""
    try {
      content = fromInputStream(hc.getInputStream)(io.Codec("UTF-8")).getLines().mkString("\n")
    }
    catch {
      case _: java.nio.charset.MalformedInputException => content = fromInputStream(hc.getInputStream)(io.Codec("ISO-8859-2")).getLines().mkString("\n")
    }
    Jsoup.parse(content)
  }

  private def processText(text: String, n: Int = 1): String = {
    if (n == 0)
      return text
    val result =
      text.split(" ").map(x => {
        var dolan = new DolanString(x)
        var i = 0
        while (dolan.replaced == 0 && i < 2) {
          i += 1
          dolan = dolan
            .replaceWithProbability("", "j", "ly")
            .replaceWithProbability("", "ly", "j")
            .replaceWithProbability("", "v", "f")
            .replaceWithProbability(Vowel, "dt", "tt")
            .replaceWithProbability(Vowel, "tt", "t")
            .replaceWithProbability(Vowel, "nn", "n")
            .replaceWithProbability(Vowel, "bb", "b")
            .replaceWithProbability(Vowel, "ll", "l")
            .replaceWithProbability(Vowel, "z", "sz")
            .replaceWithProbability("", "í", "i")
            .replaceWithProbability("", "i", "í")
            .replaceWithProbability("", "o", "ó")
            .replaceWithProbability("", "ó", "o")
            .replaceWithProbability(Vowel, "s", "zs")
            .swapRandomCharacters()
        }
        dolan
      })
        .mkString(" ")
    processText(result, n - 1)
  }

}
