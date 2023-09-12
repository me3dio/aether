package aether.core.platform

import org.scalatest.funsuite.AnyFunSuite

class ResourceTest extends AnyFunSuite {
  given dispatcher: Dispatcher = new Dispatcher()

  val stringRes = Resource("init")

  test("Flatmap") {
    val r2 = stringRes.flatMap { head =>
      Resource(head + " next")
    }
    assert(r2.get == "init next")
  }
  test("Flatmap error") {
    val re = stringRes.flatMap { head =>
      Resource.error("Test error")
    }
    assert(re.error.isDefined)

  }
}
