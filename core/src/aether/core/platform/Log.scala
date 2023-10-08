package aether.core.platform

import aether.core.platform.Log.LogEvent
import aether.core.platform.Log.stackTrace
import aether.core.platform.Log.stackSource
import Log.*

object Log {
  private[aether] var global = new Log {
    def apply(message: LogEvent) = println(message.toString)
  }
  inline def apply(message: String) = {
    // TODO: Get source from stack trace
    global(message)
  }

  case class LogEvent(val message: String, val trace: List[StackElement]) extends Event {
    val source = trace.map(_.simpleRef).mkString(" < ").take(20).padTo(20, ' ')
    override def toString = s"$source | $message"
  }

  //  case class TraceEvent(message: String, trace: List[StackElement]) extends LogEvent(message) {

  // Stack trace

  def toString(trace: List[StackElement]): String = trace.take(4).map(_.simpleRef).mkString(" < ")

  case class StackElement(className: String, method: String, file: String, line: Int) {
    def packageName = className.substring(0, className.lastIndexOf("."))
    def simpleClass = className.substring(className.lastIndexOf(".")+1)
    val cleanName = simpleClass.replaceAll("[\\<>]", "").replaceAll("\\$$", "")
    def simpleRef = s"$cleanName:$line"
  }
  
  def stackTrace(context: Throwable): List[StackElement] = {
    context.getStackTrace
      .map(t => StackElement(t.getClassName, t.getMethodName, t.getFileName, t.getLineNumber))
      .toList
  }

  def stackSource(context: Throwable, elementCount: Int = 3): String = {
     stackTrace(context).drop(1).take(elementCount).map(_.simpleRef).mkString(" < ")
  }
}

trait Log {

  def apply(throwable: Throwable): Unit = {
    ???
  }

  def apply(message: String): Unit = {
    val trace = stackTrace(new Throwable()).drop(1).take(3)
    val event = LogEvent(message, trace)
    apply(event)
  }

  def apply(log: LogEvent): Unit
}
