package aether.core.buffers

/**
 * Buffer with data read/write support.
 * Read & write methods may apply to multiple buffer elements.
 * Complex data types are decoded/encoded.
 */
object DataBuffer {
  val BYTE_MASK = 0xff

  trait Read {

    /** Read unsigned byte as Int. */
    final def readUB(): Int = readB() & BYTE_MASK

    def readB(): Byte
    def readS(): Short
    def readI(): Int
    def readL(): Long
    def readF(): Float
    def readD(): Double
    def readVI(): Int = {
      Buffers.readByteGamma(this)
    }
    def readVL(): Long = {
      Buffers.readByteGammaL(this)
    }

    /** Read UTF-8 character. */
    inline def readC(): Char = {
      (readB() match {
        case c if ((c & 0x80) == 0)    ⇒ c.toInt
        case c if ((c & 0xe0) == 0xc0) ⇒ ((c & 0x1f) << 6) | (readB() & 0x3f)
        case c if ((c & 0xf0) == 0xe0) ⇒ ((c & 0x0f) << 12) | ((readB() & 0x3f) << 6) | ((readB() & 0x3f))
      }).toChar
    }

    def read(array: Array[Byte], offset: Int, length: Int): Unit

  }

  trait Write {

    def writeB(value: Int): Unit
    def writeS(value: Int): Unit
    def writeI(value: Int): Unit
    def writeL(value: Long): Unit
    def writeF(value: Float): Unit
    def writeD(value: Double): Unit
    def writeVI(value: Int): Unit = {
      Buffers.writeByteGamma(this, value)
    }
    def writeVL(value: Long): Unit = {
      Buffers.writeByteGammaL(this, value)
    }

    /** Write UTF-8 character. */
    inline def writeC(ch: Char): Unit = {
      if (ch < 0x80) {
        writeB(ch)
      } else if (ch < 0x800) {
        writeB(0xc0 | (ch >> 6))
        writeB(ch & 0x3f)
      } else {
        writeB(0xe0 | (ch >> 12))
        writeB((ch >> 6) & 0x3f)
        writeB(ch & 0x3f)
      }
    }

    def write(array: Array[Byte], offset: Int, length: Int): Unit
    def write(array: Array[Byte]): Unit = write(array, 0, array.length)

  }

  trait IO extends Read with Write {
    def notSupported = sys.error("Buffer read/write operation is not supported for this data type: "+getClass.getSimpleName)
  }
}

trait DataBuffer extends Buffer with DataBuffer.IO {

  /** Write bytes to this buffer. */
  // REFACTOR rename to writeB?
  def write(source: DataBuffer, length: Int): Int = {
    for (i <- 0 until length) writeB(source.readB())
    length
  }
 
  def readLine(): String = {
    import scala.util.boundary
    import scala.util.boundary.break
    val out = StringBuilder()
    boundary:
      while (true) {
        readC() match {
          case '\r' =>
            if (remaining>0) {
              // optional LF
              val pos = position
              if (readB()!='\n') position = pos
            }
            break()
          case '\n' => break()
        }
      }
    out.toString()
  }
}
