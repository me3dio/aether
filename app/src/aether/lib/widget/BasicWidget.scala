package aether.lib.widget

import aether.core.types.Vec2F


abstract class BasicWidget(private var iSize: Vec2F) extends Widget {
  def size = iSize
  /** Resize widget to minimum size. */
  override def resizeMinimum(): Unit = {
    iSize = Vec2F(256, 266)
  }

  /**
   * Resize widget to given size.
   */
  override def resize(containerSize: Vec2F): Unit ={
    iSize = containerSize
  }
}
