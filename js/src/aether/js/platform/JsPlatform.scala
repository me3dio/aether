package aether.js.platform

import aether.core.platform.*
import scala.scalajs.js
import org.scalajs.dom
import aether.js.graphics.JsDisplay
import aether.core.base.HttpBase
import aether.js.network.*
import aether.js.network.JsHttpClient
import Platform.*
import aether.core.platform.Log.LogEvent

class JsPlatform extends Platform(Config(), Seq(JsDisplay)) {
  given Platform = this

  val name = Platform.Name.Js
  val log = new Log {
    def apply(message: LogEvent) = {
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
  val env = new Env {
    def getString(key: String): Option[String] = {
      val value = dom.window.localStorage.getItem(key)
      //TODO: for node, check this
      val env = js.Dynamic.global.process.env(key).asInstanceOf[js.UndefOr[String]]
      Option(value)
    }
  }
  
  val resourceBase  = new HttpBase(http, s"$origin/resources")
  def resource(source: Any) = {
    val className = source.getClass().getName()
    val classPath = className.split("\\.").dropRight(1).mkString("/")
    resourceBase.base(classPath)
  }

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
