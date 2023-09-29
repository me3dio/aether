package aether.app.canvas

import aether.core.platform.*
import aether.lib.quad.QuadGrid
import aether.lib.quad.Quad
import Sync.*
import aether.core.types.Time
import aether.core.buffers.DynamicBuffer
import aether.core.network.WebSocket
import aether.core.network.WebSocket.*
import aether.core.platform.Module.Update

object Sync {
  // case class QuadGridEvent(grid: QuadGrid[Int]) extends Event
}

class Sync(state: State, handler: Module)(using platform: Platform) {
  val socket = WebSocket.open(state.config.sync.url)
  
  val sendInterval = state.config.sync.interval
  var sendTrigger: Long = 0
  // var time: Time = Time.Undefined
  var dataSerial = 0

  def event(event: WebSocketEvent | Update) = {
    event match {
      case Update(time) =>
        if (socket.isConnected && !state.paintCanvas.isEmpty) {
          if (sendTrigger == 0) {
            sendTrigger = time + sendInterval
          } else if (time>sendTrigger) {
            if (state.changeCanvas.isEmpty) {
              sendTrigger = 0
              save()
            } else {
              //DEBUG
              Log("Waiting for server canvas update")
              sendTrigger = time + sendInterval
            }
          }
        }
      case m: OnConnect =>
        // socket.send("canvas")
      case m: OnTextMessage =>
        Log(s"Received message $m")
      case OnBinaryMessage(socket, buffer) =>
        // Log(s"Received buffer $buffer")
        val serial = buffer.readI() //TODO merge quads based on serial
        val grid = QuadGrid.decode(buffer)

        state.baseCanvas.setGrid(grid)
        state.changeCanvas.clear() 
      case e =>
    }
  }

  val buffer = new DynamicBuffer()
  def save() = {
    buffer.clear()
    buffer.writeI(dataSerial)
    QuadGrid.encode(buffer, state.paintCanvas.grid)
    buffer.flip()
    // Log.debug(s"Send canvas $buffer")
    socket.send(buffer)

    state.changeCanvas.grid.merge(state.paintCanvas.grid)
    state.changeCanvas.serializeQuad()
    state.paintCanvas.clear()


  }
}
