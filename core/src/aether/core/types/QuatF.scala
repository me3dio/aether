package aether.core.types

trait Quat[T](x: T, y: T, z: T, w: T) extends IndexedSeq[T] {
  val length = 4
  def apply(index: Int): T = index match {
    case 0 => x
    case 1 => y
    case 2 => z
    case 3 => w
    case i => throw IndexOutOfBoundsException(s"Index out of range: $i")
  }

}

case class QuatF(x: Float, y: Float, z: Float, w: Float) extends Quat[Float](x,y,z,w) {

}
