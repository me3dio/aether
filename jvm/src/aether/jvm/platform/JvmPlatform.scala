package aether.jvm.platform

import aether.core.graphics.Display
import aether.core.platform.Dispatcher
import aether.core.platform.Event
import aether.core.platform.Log
import aether.core.platform.Platform
import aether.jvm.graphics.JvmDisplay
import aether.jvm.network.JvmWebSocket
import aether.jvm.network.JvmHttpClient

import java.nio.file.Paths

import Platform.*

class JvmPlatform() extends Platform(Config(), Seq(JvmDisplay)) {
  val name = Platform.Name.Jvm
  val log = new Log {
    def apply(message: String) = {
      println(message)
    }
  }

  val wd = Paths.get("").toAbsolutePath().toString().replaceAll("\\\\", "/")
  val base = new FileBase(wd)

  val resourceBase  = new FileBase("app/src")

  val displayFactory = JvmDisplay.factory(this)
  val httpClientFactory = JvmHttpClient.factory
  val webSocketFactory = JvmWebSocket.factory(using this)

  def run(loop: => Boolean): Unit = {
    while (loop) Thread.`yield`()
  }
}
