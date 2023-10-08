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
import aether.core.base.Base
import aether.core.platform.Env
import io.github.cdimascio.dotenv.Dotenv
import aether.core.platform.Log.LogEvent

class JvmPlatform(config: Config = Config()) extends Platform(config, Seq(JvmDisplay)) {
  val name = Platform.Name.Jvm
  val log = new Log {
    def apply(message: LogEvent) = {
      println(message)
      dispatcher.add(message)
    }
  }
  Log.global = log

  val wd = Paths.get("").toAbsolutePath().toString().replaceAll("\\\\", "/")
  val base = new FileBase(wd)
  val env = new Env {
    // Load .env to system properties
    val dot = Dotenv.configure().systemProperties().load()

    def getString(key: String): Option[String] = Option(dot.get(key))
  }

  def resource(source: Any): Base = {
    val className = source.getClass().getName()
    val path = config.projectPackages.collectFirst {
      case (pack, path) if className.startsWith(pack) => s"$path/src"
    }
    assert(path.isDefined, s"Resource path not found for $className")
    val classPath = className.split("\\.").dropRight(1).mkString("/")
    val basePath = path.get +"/"+ classPath
    Log(s"Resource base path for $className: $basePath")
    FileBase(basePath)
  }

  val displayFactory = JvmDisplay.factory(this)
  val httpClientFactory = JvmHttpClient.factory
  val webSocketFactory = JvmWebSocket.factory(using this)

  def run(loop: => Boolean): Unit = {
    while (loop) Thread.`yield`()
    System.exit(0)
  }
}
