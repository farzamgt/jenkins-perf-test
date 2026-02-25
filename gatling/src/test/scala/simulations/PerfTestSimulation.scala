package simulations

import io.gatling.core.Predef._
import assertions.AssertionsMap._
import assertions.Default._
import config.baseHelpers._
import scenarios.EssentialsOrder.scnHome

/*

mvnw.cmd gatling:test -Dusers=60 -DrampUp=180 -DbaseUrl=http://localhost -DassertionType=order

 */


class PerfTestSimulation extends Simulation {

  setUp(
    scnHome(CHAIRS, ORDERS)
      .inject(
        rampConcurrentUsers(1).to(scnOrdersUsers).during(scnOrdersRampUp),
          constantConcurrentUsers(scnOrdersUsers).during(14400)
      )
  )

    .protocols(httpProtocolWithoutInferHtmlResources)
    .assertions(
      assertMapping.getOrElse(
        assertionType,
        defaultAssertion
      ): _*
    )
}
