package aether.jvm.network

import aether.core.buffers.ByteBuffer
import aether.core.network.WebSocket
import aether.core.network.WebSocket.*
import aether.core.platform.Log
import aether.core.platform.Platform
import aether.jvm.buffers.WrappedBuffer
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake

import java.net.URI
import java.nio

object JvmWebSocket {

  def factory(using platform: Platform) = new WebSocketFactory {
    given WebSocketFactory = this
    def createThis(config: Config) = new JvmWebSocket(config)
  }

}

class JvmWebSocket(config: Config)(using platform: Platform, factory: WebSocketFactory) extends WebSocket {

  class Client(handler: (WebSocketEvent) => Unit, uri: String) extends WebSocketClient(new URI(uri)) {
    def onOpen(handshake: ServerHandshake): Unit = handler(OnConnect(JvmWebSocket.this))
    def onClose(code: Int, reason: String, remote: Boolean): Unit = handler(OnClose(JvmWebSocket.this, code, reason))
    def onMessage(message: String): Unit = handler(OnStringMessage(JvmWebSocket.this, message))
    override def onMessage(bytes: nio.ByteBuffer): Unit = handler(OnBufferMessage(JvmWebSocket.this, WrappedBuffer(bytes)))
    def onError(ex: Exception): Unit = handler(OnError(JvmWebSocket.this, ex.getMessage))
  }

  val client = new Client(platform.dispatcher.add, config.url)
  client.connect()
  Log(s"Connected $client")
  // client.send("test")

  def isConnected = client.isOpen

  def send(message: String): Unit = {
    client.send(message)
  }
  def send(message: ByteBuffer): Unit = {
    val array = new Array[Byte](message.remaining)
    message.read(array, 0, message.remaining)
    client.send(array)
  }

  def release(): Unit = {
    factory.released(this)
    client.close()
  }
}
