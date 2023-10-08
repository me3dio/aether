package aether.core.input

import aether.core.platform.Event

object KeyEvent {

  /** Event keyCode constants. Using GLFW keys: https://www.glfw.org/docs/3.3/group__keys.html
    */
  object Code extends IntEnum {
    val Unknown = -1
    val Space = 32
    val Apostrophe = 39
    val Comma = 44
    val Minus = 45
    val Period = 46
    val Slash = 47
    val Semicolon = 59
    val Equal = 61
    val LeftBracket = 91
    val Backslash = 92
    val RightBracket = 93
    val GraveAccent = 96
    val World1 = 161
    val World2 = 162
    val Escape = 256
    val Enter = 257
    val Tab = 258
    val Backspace = 259
    val Insert = 260
    val Delete = 261
    val Right = 262
    val Left = 263
    val Down = 264
    val Up = 265
    val PageUp = 266
    val PageDown = 267
    val Home = 268
    val End = 269
    val CapsLock = 280
    val ScrollLock = 281
    val NumLock = 282
    val PrintScreen = 283
    val Pause = 284
    val KpDecimal = 330
    val KpDivide = 331
    val KpMultiply = 332
    val KpSubtract = 333
    val KpAdd = 334
    val KpEnter = 335
    val KpEqual = 336
    val LeftShift = 340
    val LeftControl = 341
    val LeftAlt = 342
    val LeftSuper = 343
    val RightShift = 344
    val RightControl = 345
    val RightAlt = 346
    val RightSuper = 347
    val Menu = 348
    val N0, N1, N2, N3, N4, N5, N6, N7, N8, N9 = enumSeries(0x30)
    val A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z = enumSeries(65)
    val F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12 = enumSeries(290)
    val Num0, Num1, Num2, Num3, Num4, Num5, Num6, Num7, Num8, Num9 = enumSeries(320)
  }

  object Modifier extends IntEnum {
    val Shift, Control, Meta, Alt, AltGraph = enumBits(0)
  }

  // -- Aliases
  object KeyPressed {
    def unapply(e: KeyEvent): Option[Int] = if (e.active && e.changed) Some(e.code) else None
  }

  case class CharEvent(char: Char) extends Event
}

/** @active
  *   Is key pressed.
  * @changed
  *   False for repeated key.
  */
case class KeyEvent(val active: Boolean, val changed: Boolean, code: Int, mods: Int) extends Event {
  //    def control: Boolean = (mods & Event.KeyModifier.Control) != 0
  //    def shift: Boolean = (mods & Event.KeyModifier.Shift) != 0
  //    def alt: Boolean = (mods & Event.KeyModifier.Alt) != 0
}
