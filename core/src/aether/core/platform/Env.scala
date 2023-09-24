package aether.core.platform

/** Environment variables. */
trait Env {
  def getString(key: String): String
}
