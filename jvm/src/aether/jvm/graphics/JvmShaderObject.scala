package aether.jvm.graphics

import aether.core.graphics.ShaderObject
import aether.core.platform.Resource
import aether.core.graphics.ShaderObject.*
import aether.core.graphics.ShaderObject.Type.*
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL13._
import org.lwjgl.opengl.GL15._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL30._
import org.lwjgl.opengl.GL31._
import org.lwjgl.opengl.GL41._

object JvmShaderObject {
  val factory = new Resource.Factory[ShaderObject, ShaderObject.Config] {
    given ShaderObjectFactory = this
    def createThis(config: Config) = new JvmShaderObject(config)
  }
}

class JvmShaderObject(config: Config)(using factory: ShaderObjectFactory) extends ShaderObject {

  def error: Option[String] = _error
  var _error: Option[String] = Some("uncompiled")

  def release() = {
    factory.released(this)
    glDeleteShader(glShader)
  }

  val glShaderType = config.typ match {
    case Fragment => GL_FRAGMENT_SHADER
    case Vertex   => GL_VERTEX_SHADER
  }

  val glShader = glCreateShader(glShaderType)

  assert(config.source != null)
  glShaderSource(glShader, config.source.get)

  compile()

  def compile(): Boolean = {
    glCompileShader(glShader)
    val compiled = new Array[Int](1)
    glGetShaderiv(glShader, GL_COMPILE_STATUS, compiled)
    val ok = compiled(0) != 0
    if (ok) {
      _error = None
      true
    } else {
      _error = Some(s"Failed to compile ${config.typ} shader:\n" + getLog())
      false
  }
  }

  def getLog(): String = {
    val logLength = new Array[Int](1)
    glGetShaderiv(glShader, GL_INFO_LOG_LENGTH, logLength)

    val log = new Array[Byte](logLength(0))
    if (log.length > 0) {
      glGetShaderInfoLog(glShader, logLength(0))
    } else "[no log]"

  }

}
