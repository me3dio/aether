package aether.server

import cats.effect._
import org.http4s.AuthedRoutes
import org.http4s.HttpRoutes
import org.http4s.MediaType
import org.http4s.dsl.io._
import org.http4s.headers.`Content-Type`
import org.http4s.StaticFile
import fs2.io.file.Path

class WebAppService {
  val logger = org.log4s.getLogger


  val services = HttpRoutes.of[IO] {
    case GET -> Root / "app" / app =>
      Ok(AppPage(app, app), `Content-Type`(MediaType.text.html))
    case req @ GET -> Root / "scripts" / scriptFile =>
      logger.debug(s"Get script $scriptFile")
      StaticFile.fromPath(Path(s"out/js/fastLinkJS.dest/$scriptFile"), Some(req)).getOrElseF {
        logger.debug(s"Get from resource $scriptFile")
        StaticFile.fromResource(s"scripts/$scriptFile", Some(req)).getOrElseF(NotFound())
      }
    case req @ GET -> Root / "static" / file =>
      StaticFile.fromPath(Path(s"static/$file"), Some(req)).getOrElseF {
        StaticFile.fromResource(s"/static/$file", Some(req)).getOrElseF(NotFound())
      }
    case req @ GET -> "resources" /: file =>
      val path = Path(s"app/src/$file")
      logger.debug(s"Get resource $path")
      StaticFile.fromPath(path, Some(req)).getOrElseF(NotFound())
  }
}
