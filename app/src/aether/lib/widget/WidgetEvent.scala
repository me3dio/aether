package aether.lib.widget

import aether.core.platform.Event
import aether.core.types.Vec2F
import aether.core.input.TouchEvent

trait WidgetEvent extends Event

object WidgetEvent {
  case class PressEvent(pos: Vec2F) extends WidgetEvent
  case class ReleaseEvent(pos: Vec2F, clicked: Boolean) extends WidgetEvent
  case class DragEvent(pos: Vec2F) extends WidgetEvent

  object PinchEvent {
    def avg(pos: Seq[Vec2F]) = (pos(0) + pos(1)) / 2
  }
  case class PinchEvent(state: TouchEvent.Type, pos: Seq[Vec2F]) extends Event {
    assert(pos.size==2)
  }
  // case class ScrollEvent(offset: Vec2F) extends Event
  // case class ZoomEvent(zoom: Float) extends Event
}