package aether.js.platform

import aether.core.base.HttpBase
import aether.core.platform.*
import aether.js.graphics.JsDisplay
import aether.js.network.JsHttpClient
import aether.js.network.JsWebSocket
import org.scalajs.dom

import Platform.Config

class JsPlatform extends Platform(Config(), Seq(JsDisplay)) {
  given Platform = this
  val name = Platform.Name.Js
  val log = new Log {
    def apply(message: String) = {
      println(message)
    }
  }
  val http = new JsHttpClient()
  val origin = dom.window.location.origin
  val base = new HttpBase(http, origin)
  val resourceBase  = new HttpBase(new JsHttpClient(), s"$origin/resources")

  val resourcePath: String = "resource"

  val displayFactory = JsDisplay.factory(dispatcher)
  val webSocketFactory = JsWebSocket.factory

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
