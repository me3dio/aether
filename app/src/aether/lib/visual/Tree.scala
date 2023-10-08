package aether.lib.visual

import aether.lib.types.Tx2FAxis
import aether.core.types.Vec2F

object Tree {
  case class Node(visual: Visual, transform: Tx2FAxis)
}
trait Tree {

  def nodes: Seq[Tree.Node]
  
  def pick(pos: Vec2F): Seq[Visual] = {
    nodes.flatMap{
      case node: Tree.Node => 
        ???
        // val pos2 = transform.inverse(pos)
        // visual.pick(pos2)

    }
  }
}
