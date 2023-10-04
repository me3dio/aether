package aether.server

import scalatags.Text.all._
import scalatags.Text.tags2.title
import io.circe.JsonObject
import io.circe.syntax._
import scalatags.generic.Attr

object AppPage {
  
  val logger = org.log4s.getLogger

  val nocomment = {
    val relays = Seq("ws://localhost")
    script(
      src := "https://nocomment.fiatjaf.com/embed.js",
      id := "nocomment",
      Attr("relays") := JsonObject("data-relays" -> relays.asJson).asJson.noSpaces
    )
  }

  def apply(app: String, pageTitle: String) = {
    html(margin := "0 !important", padding := "0 !important")(
      head(meta(charset := "UTF-8"), title(pageTitle), link(rel := "stylesheet", href := "/static/style.css")),
      body(margin := "0 !important", padding := "0 !important")(
        // canvas(id := "display", width := "1024", height := "512"),
        script(`type` := "text/javascript", src := "/scripts/main.js"),
        script(s"window.onload = function(){ App.$app(); }"),
        nocomment
      )
    ).toString
  }
}
