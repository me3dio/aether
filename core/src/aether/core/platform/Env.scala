package aether.core.platform

/** Environment variables. */
trait Env {
  def getString(key: String): Option[String]

  final def getString(key: String, defaultValue: String): String = getString(key).getOrElse(defaultValue)

}
