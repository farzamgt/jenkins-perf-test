package assertions

import io.gatling.commons.stats.assertion.Assertion

object AssertionsMap {
  val assertMapping: Map[String, Seq[Assertion]] = Map(
    "defaultAssertion" -> Default.defaultAssertion,
    "order"            -> Default.order
  )
}
