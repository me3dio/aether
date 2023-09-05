package aether.app.shadertoy

object ShadertoyApi {

  case class Info(
      id: String,
      date: String,
      viewed: Int,
      name: String,
      username: String,
      description: String,
      likes: Int,
      published: Int,
      flags: Int,
      usePreview: Int,
      tags: List[String],
      hasliked: Int
  )

  case class Sampler(
      filter: String,
      wrap: String,
      vflip: String,
      srgb: String,
      internal: String
  )
  case class Input(
      id: Int,
      src: String,
      ctype: String,
      channel: Int,
      sampler: Sampler,
      published: Int
  )

  case class Output(id: Int, channel: Int)

  case class Renderpass(
      inputs: Option[List[Input]],
      outputs: List[Output],
      code: String,
      name: String,
      description: String,
      `type`: String
  )

  case class Shader(ver: String, info: Info, renderpass: List[Renderpass])

}
