package aether.jvm.app

import aether.jvm.platform.JvmPlatform
import aether.core.platform.Module
import aether.app.shader.ShaderApp
import aether.app.mandelbrot.Mandelbrot
import aether.app.canvas.CanvasApp
import aether.app.shadertoy.ShadertoyApp
import aether.core.platform.Platform.Config

object Apps {

  val projectPackages = Map(
    "aether.app" -> "app",
    "aether.lib" -> "app",
    "aether.core" -> "core",
    "aether.js" -> "js",
    "aether.jvm" -> "jvm",
    "server.jvm" -> "server"
  )

  lazy val platform = JvmPlatform(Config(projectPackages = projectPackages))

  def launch(app: Module) = {
    platform.runApp(app)
  }

  @main
  def shader = launch(ShaderApp(platform))
  @main
  def mandelbrot = launch(Mandelbrot(platform))
  @main
  def canvas = launch(CanvasApp(platform))
  @main
  def shadertoy = launch(ShadertoyApp(platform))
}
