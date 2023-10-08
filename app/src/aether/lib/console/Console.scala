package aether.lib.console

import aether.lib.canvas.Canvas
import aether.core.types.RectF
import aether.lib.font.Font
import aether.lib.widget.Widget
import aether.core.platform.Module
import aether.core.types.Vec2F
import aether.core.platform.Event
import aether.core.platform.Log
import EditableFlow.*

object Console {
  def apply(using font: Font) = new Console(font: Font)

  case class Text(text: String)
}

class Console(val font: Font) extends Widget {

  // var view: RectF = RectF.unit
  // def viewport_=(view: RectF): Unit = this.view = view
  var size: Vec2F = Vec2F.Zero

  val padding = 1

  val flow = new EditableFlow(font)
  flow.items += Text("Test1")
  flow.items += Text("Test2")
  modified()
  // def event(event: Visual.RenderEvent) = ???

  def modified() = {
    //TODO
    size = Vec2F(16,16)
  }

  def event(event: Event) = {
    event match {
      case Widget.Resize(newSize) =>
        this.size = newSize
        flow.event(event)
      case e: Log.LogEvent =>
        flow.items += new Text(e.toString)
      case _ =>
    }
  }

  def paint(canvas: Canvas) = {
    var pos = Vec2F.Zero
    val visuals = flow.visuals

  }
}
