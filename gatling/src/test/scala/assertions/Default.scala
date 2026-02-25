package assertions

import io.gatling.commons.stats.assertion.Assertion
import io.gatling.core.Predef._

object Default {

  val defaultAssertion: Seq[Assertion] = Seq(
    global.successfulRequests.percent.gte(1.0)
  )

  val order: Seq[Assertion] = Seq(
    details("Action: Add to Cart").responseTime.percentile(95).lte(300),
    details("Page: Cart").responseTime.percentile(95).lte(350),
    details("Page: Checkout").responseTime.percentile(95).lte(400),
    details("Page: Submit Order").responseTime.percentile(95).lte(900)
  )
}
