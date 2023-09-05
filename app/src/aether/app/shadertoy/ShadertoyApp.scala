package aether.app.shadertoy

import aether.core.platform.Platform
import aether.core.platform.Event
import aether.core.graphics.Display
import aether.core.platform.Log
import aether.core.types.Vec2I

class ShadertoyApp(val platform: Platform) extends aether.core.platform.Module {

  val display = platform.displayFactory.create(Display.Config(size = Vec2I(512, 512)))

  def event(event: Event) = {
    event match {
      case Display.Paint(display) => {
        Log(s"Paint")
        display.graphics.clear(1,0,0,1)
      }
      case event => Log(s"event $event")
    }
  }
}
