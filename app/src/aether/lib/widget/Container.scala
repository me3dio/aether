package aether.lib.widget

import aether.lib.visual.Tree
import aether.core.types.Vec2I

trait Container extends Widget with Tree {
  def widgets: Seq[Widget] = nodes.collect {
    case Tree.Node(widget: Widget, _) => widget
  }

}
