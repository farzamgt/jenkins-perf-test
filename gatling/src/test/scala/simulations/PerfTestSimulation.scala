package simulations

import io.gatling.core.Predef._
import assertions.AssertionsMap._
import assertions.Default._
import config.baseHelpers._
import scenarios.EssentialsOrder.scnHome

/*

mvnw.cmd gatling:test -Dusers=5 -DrampUp=10 -Dduration=600 -DbaseUrl=http://localhost -DassertionType=order

 */


class PerfTestSimulation extends Simulation {

  setUp(
    scnHome(CHAIRS, ORDERS)
      .inject(
        rampConcurrentUsers(1).to(scnOrdersUsers).during(scnOrdersRampUp),
          constantConcurrentUsers(scnOrdersUsers).during(600)
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
