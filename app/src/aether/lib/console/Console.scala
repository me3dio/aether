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

  val text = new EditableFlow(font)
  text.items += Text("Test1")
  text.items += Text("Test2")
  modified()
  // def event(event: Visual.RenderEvent) = ???

  def modified() = {
    //TODO
    size = Vec2F(16,16)
  }

  def event(event: Event) = {
    event match {
      case e: Log.LogEvent =>
        text.items += new Text(e.toString)
      case _ =>
    }
  }

  def paint(canvas: Canvas) = {
    var pos = Vec2F.Zero
    val visuals = text.visuals

  }
}
