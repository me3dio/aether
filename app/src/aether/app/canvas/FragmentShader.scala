package aether.app.canvas

import aether.core.types.RectI
import aether.core.types.Vec2I
import aether.core.types.VecExt.*
import aether.lib.graphics.Mesh
import aether.lib.graphics.ShaderVarBuffer

import scala.collection.mutable.LinkedHashMap

import FragmentShader.*
import aether.core.graphics.ShaderProgram
import aether.core.types.Mat3F
import aether.core.graphics.Graphics
import aether.lib.graphics.Mesh
import aether.lib.graphics.ReloadShader
import aether.core.types.Vec2F
import aether.core.types.Vec4F
import aether.lib.graphics.Shader
import aether.lib.canvas.Canvas
import aether.core.graphics.Texture
import aether.core.types.Vec4I
import aether.core.types.VecExt.toVec4F

object FragmentShader {
  
  case class ShaderParams(
    iQuadS: Int,
    iQuadSize: Int, 
    bufferTexture: Texture,
    iResolution: Vec2F,
    iViewport: Vec4F,
    iTransform: Mat3F,
    iFrame: Int,
    iTime: Float,
    // iMouse: Vec2F,
    iIterations: Int,
    // iMVP: Mat4F,
    // iCamera: Mat4F,
    // iChannel0: Texture,
  )
}

class FragmentShader(g: Graphics) {
  given Graphics = g
  var pass: ReloadShader = ReloadShader(g)

  var frame = 0
  val startTime = System.currentTimeMillis()

  def ready = pass.ready

  var version = "300 es"

  //TODO Replace define declarations in sources
  def header: String = {
    val lines = s"#version $version" +: defines.map {
      case (name, value) => s"#define $name $value"
    }.toSeq
    lines.mkString("\n")
  }

  val defines = LinkedHashMap[String, String]()

  def setSource(vertShader: String, fragShader: String) = {
    pass.create(Some(vertShader), Some(fragShader))
  }



  val viewMesh = Shader.createMeshUnit2D()


  def render(resolution: Vec2I, viewport: RectI, tx: Mat3F, bufferTex: Texture) = {
    val params = ShaderParams(
      0,
      bufferTex.size.x,
      bufferTex,
      resolution.toVec2F,
      viewport.bounds.toVec4F,
      tx,
      frame,
      (System.currentTimeMillis - startTime) * 0.001f,
      100)

    assert(pass.ready, "Incomplete ShaderPass")
    frame = frame + 1

    val program = pass.shader.get.program
    viewMesh.toProgram(program)

    pass.shader.get.initialize(params)
    
    program.draw(ShaderProgram.Mode.TriangleStrip, 0, 4)

  }

  def getState(): Seq[String] = {
    val program = pass.shader.get.program
    val unis = for (name <- program.uniforms) yield program.uniform(name).get.toString()
    val att = for (name <- program.attributes) yield program.attribute(name).get.toString()
    (unis ++ att).toSeq
  }
}
