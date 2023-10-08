package aether.core.platform
import aether.core.base.Base
import aether.core.graphics.Display
import aether.core.graphics.Display.DisplayFactory
import aether.core.network.WebSocket.WebSocketFactory
import aether.core.network.HttpClient.HttpClientFactory

import Dispatcher.CallbackEvent
import Platform.*

object Platform {

  case class Config(
    version: String = "N/A",
    updateStep: Int = 0,
    projectPackages: Map[String, String] = Map(),
  )

  enum Name {
    case Jvm
    case Js
  }
}

trait Platform(config: Config, modules: Seq[Module]) {

  val displayFactory: DisplayFactory
  val webSocketFactory: WebSocketFactory
  val httpClientFactory: HttpClientFactory

  def primaryDisplay: Option[Display] = displayFactory.instances.headOption

  val dispatcher: Dispatcher = new Dispatcher()
  given Dispatcher = dispatcher

  /** Platform name. */
  val name: Name
  val version = config.version

  // val log: Log
  val base: Base
  val env: Env

  /** Base for resource files relative to a package
   * @param source Any object in the package
   */
  def resource(source: Any): Base

  private var running = true

  def exit(): Unit = running = false

    // Initialize system modules before instantiating App
  init()

  def init() = {
    // Log("Module init")
    modules.foreach(_.event(Module.Init(this)))
  }

  // called by renderloop
  def uninit() = {
    // Log("Module uninit")
    modules.reverse.foreach(_.event(Module.Uninit))
  }

  def runApp(app: Module) = {
    app.event(Module.Init(this))
    val mods = modules :+ app
    def send(event: Event) = mods.foreach(_.event(event))

    val startTime = System.currentTimeMillis()
    var updateTime = startTime

    // JVM runs in this thread and returns when app exits
    // JS starts a render loop and returns immediately
    run {
      var processEvents = true
      while (processEvents) {
        dispatcher.getEvent() match {
          case Some(CallbackEvent(callback)) => callback()
          case Some(event) => send(event)
          case None        => processEvents = false
        }
      }
      if (config.updateStep>0) {
        val now = System.currentTimeMillis()
        while (updateTime < now) {
          updateTime += config.updateStep
          send(Module.Update(updateTime))
        }
      }

      displayFactory.instances.foreach(_.render { disp =>
        assert(disp.graphics.target != null)
        send(Display.Paint(disp))
      })
      if (!running) {
        app.event(Module.Uninit)
        uninit()
      }
      running
    }
  }

  def run(loop: => Boolean): Unit

}
