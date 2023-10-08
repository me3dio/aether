package aether.app.canvas

import aether.core.platform.*
import aether.core.graphics.*
import aether.core.input.*
import aether.core.types.*
import aether.lib.quad.Quad
import aether.lib.quad.QuadGrid
import aether.lib.quad.shader.QuadShader
import aether.lib.canvas.Canvas
import aether.lib.widget.Scroll
import aether.lib.widget.Widget
import aether.lib.widget.WidgetEvent.*
import aether.core.types.VecExt.*
import aether.core.input.KeyEvent.*

class CanvasWidget(platform: Platform, graphics: Graphics, res: Resources, state: State, var size: Vec2F)(using g: Graphics) extends Widget {
  val name = "CanvasWidget"
  // val left = config.left
  // val right = config.right

  val shader = new FragmentShader(graphics)
  shader.setSource(res.vertSource.get, res.fragSource.get)

  val scroll = Scroll.create() {
    RectF(Vec2F.Zero, size)
  }
  scroll.pos = size / 2
  scroll.scale = 8

  val draw = new MouseDraw() {
    def draw(pos: Vec2F) = {
      val p = scroll.txModel * pos
      state.paintCanvas.update(p.floorVec2I, state.color)
    }
  }

  def event(event: Event) = {
    draw.drawEvent(event)
    import KeyEvent.Code._
    event match {
      // case Display.Paint(disp) => paint(disp.size.toVec2F)
      // case Canvas.Render(canvas, area) => paint(area.size)
      // case e: PointerEvent =>
      case e: PointerEvent =>
        //Log.debug(s"PointerEvent $event")
        scroll.event(e)
      case e: PinchEvent =>
        scroll.event(e)
      case KeyPressed(SPACE) => shader.getState().map(Log(_))
      // case KeyPressed(C) =>
      // state.canvas.clear()
      // case KeyPressed(E) =>
      //   GameOfLife.evolve(canvas.grid)
      //   canvas.serializeQuad()
      case KeyEvent(true, true, code, _) if (code >= KeyEvent.Code.N0 && code < KeyEvent.Code.N9) =>
        val i = code - KeyEvent.Code.N9
        Log(s"Select $i")
      case e =>
    }
  }

  def update() = {}

  def paint(renderer: Canvas): Unit = {
    val prog = shader.pass.shader.get.program
    for (canvas <- Seq(state.baseCanvas, state.changeCanvas, state.paintCanvas)) {
      if (canvas.canvasModified) {
        canvas.serializeQuad()
      }
      val tx = scroll.txView.scaled(canvas.grid.radius).toMat3F
      shader.render(size.toVec2I, renderer.view.toRectI, tx, canvas.bufferTex)
    }
  }

}
