package aether.lib.widget

import aether.core.platform.Event
import aether.lib.visual.Visual
import aether.core.types.RectF
import aether.core.types.Vec2F
import Widget.*
import aether.core.input.TouchEvent
import aether.lib.canvas.Canvas
import aether.core.platform.Module
import org.w3c.dom.Node

object Widget {
  case class Config()

  case class PickResult(visual: Visual, transform: Vec2F)

  // case class SetViewport(view: RectF) extends Event

  class Node {}

  case class Resize(size: Vec2F) extends Event

}

trait Widget extends Visual with Module {

  /** Resize widget to given size. */
  def resize(containerSize: Vec2F): Unit = {}

}
