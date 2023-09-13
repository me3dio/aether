package aether.lib.graphics

import aether.core.graphics.ShaderObject
import aether.core.graphics.ShaderProgram
import aether.core.platform.Log
import aether.core.graphics.Graphics

class ReloadShader(graphics: Graphics) {
  given Graphics = graphics

  var sourceVertex: Option[String] = None
  var sourceFragment: Option[String] = None
  var shader: Option[Shader] = None

  def ready = shader.isDefined

  def create(vertex: Option[String], fragment: Option[String]): Boolean = {
    if (vertex.isDefined) sourceVertex = vertex
    if (fragment.isDefined) sourceFragment = fragment
    create()
  }

  def create(): Boolean = {
    release()
    val vertex = ShaderObject(ShaderObject.Type.Vertex, sourceVertex.get)
    val fragment = ShaderObject(ShaderObject.Type.Fragment, sourceFragment.get)
    val program = ShaderProgram(vertex, fragment)
    shader = Some(Shader(vertex, fragment, program))
    if (program.error.isDefined) {
      Log("Compilation failed:\n" + program.error)
      release()
      false
    } else {
      true
    }
  }

  def release() = {
    shader.foreach(_.release())
    shader = None
  }

}
