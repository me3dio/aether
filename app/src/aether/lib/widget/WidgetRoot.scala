package aether.lib.widget

import aether.core.platform.Module
import aether.lib.canvas.Canvas
import aether.core.platform.Event

object WidgetRoot {
  def apply(widget: Widget) = {
    new WidgetRoot(widget)
  }
}

class WidgetRoot(val root: Widget) extends Module {

  def event(event: Event) = event match {
    // case Canvas.Paint(canvas, rect) => paint(canvas)
      // root.paint(canvas)
    case n =>
      //TODO
      ???
  }

  def paint(canvas: Canvas) = {
    ???
  }
}
