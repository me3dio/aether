package aether.lib.widget

import aether.lib.visual.Tree

trait Container extends Widget with Tree {
  def widgets: Seq[Widget] = nodes.collect {
    case Tree.Node(widget: Widget, _) => widget
  }
}
