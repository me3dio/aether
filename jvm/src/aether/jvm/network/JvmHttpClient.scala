package aether.jvm.network

import aether.core.network.HttpClient
import aether.core.network.HttpClient.*
import aether.core.platform.Dispatcher
import sttp.client3._
import sttp.model.Uri
import scala.concurrent.Future
import aether.core.buffers.ByteBuffer
import sttp.model.Method

object JvmHttpClient  {

  val factory = new HttpClientFactory {
    def createThis(config: Config): HttpClient = {
      new JvmHttpClient(config)
    }
  }
}

class JvmHttpClient(config: Config) extends HttpClient {
  val backend = HttpURLConnectionBackend()
  import concurrent.ExecutionContext.Implicits.global

  def requestHeaders(url: String): Future[Map[String, String]] = {
    Future{
      // TODO: HEAD option might not be supported
      val response = basicRequest.head(Uri.unsafeParse(url)).send(backend)
      val headers = for (header <- response.headers) yield header.name -> header.value
      headers.toMap
    }
  }

  def requestString(url: String): Future[String] = {
    Future {
      val response = basicRequest.get(Uri.unsafeParse(url)).send(backend)
      response.body match {
        case Left(error) => throw new RuntimeException(error)
        case Right(content) => content
      }
    }
  }

  def requestByteBuffer(url: String): Future[ByteBuffer] = {
    Future{
      val response = basicRequest.response(asByteArray).get(Uri.unsafeParse(url)).send(backend)
      response.body match {
        case Left(error) => throw new RuntimeException(error)
        case Right(array) => ByteBuffer(array)
      }
    }
  }

  // -- public interface

  def headers(url: String) = ???
  def loadString(url: String) = ???
  def loadTexture(url: String) = ???
  def loadBytes(url: String) = ???
  def loadJson(url: String) = ???

  
}