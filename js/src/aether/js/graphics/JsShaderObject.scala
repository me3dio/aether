package aether.js.graphics

import org.scalajs.dom.raw.{WebGLRenderingContext => GL}
import aether.core.graphics.ShaderObject
import aether.core.graphics.ShaderObject.Type
import aether.core.graphics.ShaderObject.Type.*

import aether.core.platform.*
import aether.core.graphics.ShaderObject.*

object JsShaderObject {
  def factory(using gl: GL) = new ShaderObjectFactory {
    given ShaderObjectFactory = this
    def createThis(config: Config) = new JsShaderObject(config.typ, config.source.get)
  }
}

class JsShaderObject(typ: Type, source: String)(using factory: ShaderObjectFactory, gl: GL) extends ShaderObject {
  def error: Option[String] = _error
  var _error: Option[String] = Some("uncompiled")

  val glShaderType = typ match {
    case Type.Fragment => GL.FRAGMENT_SHADER
    case Type.Vertex   => GL.VERTEX_SHADER
  }

  val glShader = gl.createShader(glShaderType)
  gl.shaderSource(glShader, source)
  compile()

  //  def this(id: Int, flags: Int, file: Ref) {
  //    this(id, flags, "not implemented")
  //  }

  def compile(): Boolean = {
    gl.compileShader(glShader)
    val compiled = gl.getShaderParameter(glShader, GL.COMPILE_STATUS).asInstanceOf[Boolean]
    if (compiled) {
      _error = None
      true
    } else {
      val log = gl.getShaderInfoLog(glShader)
      _error = Some(s"Failed to compile shader\n$log")
      false
    }
  }

  def release() = {
    factory.released(this)
    gl.deleteShader(glShader)
  }

}
