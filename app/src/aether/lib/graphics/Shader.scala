package aether.lib.graphics

import Shader._
import aether.core.graphics.ShaderObject
import aether.core.graphics.ShaderProgram
import aether.core.platform.Log
import aether.core.graphics.Graphics
import aether.lib.graphics.Mesh
import aether.core.types.*
import aether.core.graphics.ShaderBuffer.Target.Texture
import aether.core.graphics.Texture

object Shader {

  def createMeshUnit2D()(using Graphics): Mesh = {
    val mesh = Mesh.factory.staticPT(4)
    mesh.positions.put2F(-1, 1)
    mesh.positions.put2F(1, 1)
    mesh.positions.put2F(-1, -1)
    mesh.positions.put2F(1, -1)
    mesh
  }

  def apply(vertexSrc: String, fragmentSrc: String)(using Graphics): Option[Shader] = {
    val vertex = ShaderObject(ShaderObject.Type.Vertex, vertexSrc)
    val fragment = ShaderObject(ShaderObject.Type.Fragment, fragmentSrc)
    val program = ShaderProgram(vertex, fragment)
    val shader = new Shader(vertex, fragment, program)
    if (program.error.isDefined) {
      shader.release()
      None
    } else {
      Some(shader)
    }
    ???
  }

}

class Shader(vertex: ShaderObject, fragment: ShaderObject, val program: ShaderProgram) {

  assert(program.error.isEmpty, program.error.get)

  def release() = {
    program.release()
    vertex.release()
    fragment.release()
  }

  def initialize[P <: Product](params: P): Unit = {
    val entries = params.productElementNames.zip(params.productIterator.toList).toMap
    var texIndex = 0
    for ((name, value) <- entries) {
      program.uniform(name).map { case uniform =>
        // Log(s"Initialize uniform $name with $value")
        value match {
          case v: Int   => uniform.putI(v)
          case v: Float => uniform.putF(v)
          case v: Vec2I => uniform.put2I(v)
          case v: Vec2F => uniform.put2F(v)
          case v: Vec4F => uniform.put4F(v)
          case v: Mat3F => uniform.putMat3F(v)
          case v        => sys.error(s"Unsupported type ${v.getClass.getName}: $v")
        }
      }
      program.attribute(name).map { case attribute =>
        // Log(s"Initialize attribute $name with $value")
        value match {
          case buffer: ShaderVarBuffer =>
            program.attributeBuffer(name, buffer.buffer, buffer.numComponents)
          case _ => // ignore
        }
      }
      value match {
        case texture: Texture =>
          // Log(s"Initialize texture $texIndex, $texture")
          program.textureUnit(texIndex, texture)
          texIndex += 1
        case _ => // ignore
      }
    }
  }

}
