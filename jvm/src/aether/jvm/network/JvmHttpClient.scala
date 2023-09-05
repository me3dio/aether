package aether.jvm.network

import aether.core.network.HttpClient
import aether.core.network.HttpClient.*
import aether.core.platform.Dispatcher

object JvmHttpClient  {

  val factory = new HttpClientFactory {
    def createThis(config: Config): HttpClient = {
      new JvmHttpClient(config)
    }
  }
}

class JvmHttpClient(config: Config) extends HttpClient {
  def headers(url: String) = ???
  def loadString(url: String) = ???
  def loadTexture(url: String) = ???
  def loadBytes(url: String) = ???
  def loadJson(url: String) = ???
}