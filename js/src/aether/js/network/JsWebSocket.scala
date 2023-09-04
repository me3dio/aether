package aether.js.network

import aether.core.buffers.ByteBuffer
import aether.core.buffers.DynamicBuffer
import aether.core.buffers.NativeBuffer
import aether.core.network.WebSocket
import aether.core.network.WebSocket.*
import aether.core.platform.Platform
import aether.js.buffers.Int8Buffer
import aether.js.buffers.JsBuffer
import org.scalajs.dom
import org.scalajs.dom.Blob

import scala.scalajs.js
import scala.scalajs.js.typedarray.ArrayBuffer
import scala.scalajs.js.typedarray.Int8Array
import scala.scalajs.js.typedarray.int8Array2ByteArray

import concurrent.ExecutionContext.Implicits.global

object JsWebSocket {

  def factory(using platform: Platform) = new WebSocketFactory {
    given WebSocketFactory = this
    def createThis(config: Config) = new JsWebSocket(config)
  }
}

class JsWebSocket(config: Config)(using platform: Platform, factory: WebSocketFactory) extends WebSocket {
  val socket = dom.WebSocket(config.url, js.Array[String]())

  val handler: (WebSocketEvent) => Unit = platform.dispatcher.add

  socket.onopen = { event =>
    handler(OnConnect(this))
  }
  socket.onclose = { event =>
    handler(OnClose(this, event.code, event.reason))
  }
  socket.onmessage = { event =>
    if (event.data != null) event.data match {
      case s: String => handler(OnStringMessage(this, s))
      case b: Blob =>
        b.arrayBuffer().toFuture.map {
          d => handler(OnBufferMessage(this, DynamicBuffer(new Int8Array(d).toArray)))
        }
      case d => ???
    }
  }
  socket.onerror = { event =>
    handler(OnError(this, event.message))
  }

  def isConnected = socket.readyState == dom.WebSocket.OPEN

  def send(message: String): Unit = {
    socket.send(message)
  }
  def send(message: ByteBuffer): Unit = {
    import js.JSConverters._
    val buf = new ArrayBuffer(message.remaining)
    val array = new Int8Array(buf)
    array.set(message.toByteArray.toJSArray)
    socket.send(buf)
  }

  def release(): Unit = {
    factory.released(this)
    socket.close()
  }
}
