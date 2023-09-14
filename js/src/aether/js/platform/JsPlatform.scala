package aether.js.platform

import aether.core.platform.*
import org.scalajs.dom
import aether.js.graphics.JsDisplay
import aether.core.base.HttpBase
import aether.js.network.*
import aether.js.network.JsHttpClient
import Platform.*

class JsPlatform extends Platform(Config(), Seq(JsDisplay)) {
  given Platform = this

  val name = Platform.Name.Js
  val log = new Log {
    def apply(message: String) = {
      println(message)
    }
  }
  val resourcePath: String = "resource"

  val displayFactory = JsDisplay.factory(dispatcher)
  val httpClientFactory = JsHttpClient.factory(dispatcher)
  val webSocketFactory = JsWebSocket.factory

  val http = JsHttpClient()
  val origin = dom.window.location.origin
  val base = new HttpBase(http, origin)
  val resourceBase  = new HttpBase(http, s"$origin/resources")

  def run(loop: => Boolean): Unit = {
    def frame(time: Double): Unit = {
      val cont = loop
      if (cont) {
        dom.window.requestAnimationFrame(frame _)
      }
    }
    frame(0)

  }

}
