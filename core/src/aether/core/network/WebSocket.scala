package aether.core.network

import WebSocket.*
import aether.core.buffers.ByteBuffer
import aether.core.platform.*

object WebSocket {
  type WebSocketFactory = Resource.Factory[WebSocket, Config]

  case class Config(url: String) extends Resource.Config

  def open(url: String)(using platform: Platform): WebSocket = {
    platform.webSocketFactory.create(Config(url))
  }

  abstract class WebSocketEvent extends Event {
    def socket: WebSocket
  }
  case class OnConnect(socket: WebSocket) extends WebSocketEvent
  case class OnTextMessage(socket: WebSocket, message: String) extends WebSocketEvent
  case class OnBinaryMessage(socket: WebSocket, message: ByteBuffer) extends WebSocketEvent
  case class OnError(socket: WebSocket, message: String) extends WebSocketEvent
  case class OnClose(socket: WebSocket, code: Int, reason: String) extends WebSocketEvent
}

trait WebSocket extends NativeResource[WebSocket, WebSocket.Config] {

  def isConnected: Boolean
  def send(message: String): Unit
  def send(message: ByteBuffer): Unit
}
