package aether.lib.console

import scala.collection.mutable.Buffer
import aether.core.types.Color
import EditableFlow.*
import aether.lib.visual.Visual
import EditableFlow.*
import aether.lib.canvas.Canvas
import aether.lib.font.Font
import aether.core.types.Vec2F

object EditableFlow {

  trait Item
  case class Text(text: String, color: Color = Color.White) extends Item
}

class EditableFlow(font: Font) {
  val items = Buffer[Item]()

  def visual(width: Float, height: Float)(render: Canvas => Unit) = {
    new Visual {
      def size = Vec2F(width, height)
      def paint(canvas: Canvas) = render(canvas)
    }
  }

  def visuals: Seq[Visual] = {
    items.toSeq.map {
      case Text(text, color) => visual(font.width(text), font.height) { canvas =>
          canvas.drawString(Vec2F.Zero, font, text, color.argbF)
        }
    }
  }
}
