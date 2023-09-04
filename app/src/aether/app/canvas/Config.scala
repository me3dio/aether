package aether.app.canvas

import aether.core.types.Vec2I
import Config.*

object Config {
    case class Sync(url: String = "http://localhost/ws", interval: Int = 1000)
}

final case class Config(sync: Sync = Sync(), dispSize: Vec2I = Vec2I(512, 512))
