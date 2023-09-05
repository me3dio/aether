package aether.app.shadertoy

import aether.core.base.Ref
import io.circe.generic.auto.*
import io.circe.syntax.EncoderOps
import aether.core.platform.Dispatcher
import aether.app.shadertoy.ShadertoyApi.Shader
import aether.core.platform.Log
import aether.core.platform.Resource

object Shadertoy {
  def getShader(appKey: String, shaderId: String) = s"https://www.shadertoy.com/api/v1/shaders/$shaderId?key=$appKey"

  def load(ref: Ref, appKey: String, shaderId: String)(using Dispatcher): Resource[Shadertoy] = {
    ref.loadJson().map(_.as[ShadertoyApi.Shader]).map { config =>
      Log(s"Read config $config")
      config match {
        case Left(error) => sys.error(error.toString)
        case Right(config) => new Shadertoy(config)
      }
    }
  }
}

class Shadertoy(config: ShadertoyApi.Shader) {
}

