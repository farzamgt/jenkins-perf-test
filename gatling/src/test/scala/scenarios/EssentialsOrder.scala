package scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import api.essentials._
import config.baseHelpers
import config.baseHelpers.{MAX_THINK_TIME, MIN_THINK_TIME}

import scala.util.Random

object EssentialsOrder {

  def scnHome(chairs: Double, orders: Double) = {
    scenario("Gatling Essentials 100/50/30")
      .exec(flushHttpCache)
      .exec(flushCookieJar)
      .exitBlockOnFail {
        exec(home())
          .pause(MIN_THINK_TIME, MAX_THINK_TIME)
          .exec(navigateToCategory("tables"))
          .exec(feed(csv("data/tables.csv").random))
          .exec(_.set("sessionQuantity", "1"))
          .exec(navigateToProduct("id"))
          .pause(MIN_THINK_TIME, MAX_THINK_TIME)
          .exec(addProductToCart("sessionQuantity", "Tables"))
          .pause(MIN_THINK_TIME, MAX_THINK_TIME)

          // Chairs (50%)
          .randomSwitch(
            chairs * 100 -> exec(
              navigateToCategory("chairs")
                .exec(_.set("sessionQuantity", "1"))
                .pause(MIN_THINK_TIME, MAX_THINK_TIME)
                .exec(navigateToProduct("product"))
                .pause(MIN_THINK_TIME, MAX_THINK_TIME)
                .exec(addProductToCart("sessionQuantity", "Chairs"))
                .pause(MIN_THINK_TIME, MAX_THINK_TIME)
            )
          )

          // Checkout (30%)
          .randomSwitch(
            orders * 100 -> exec(
              openCart()
                .exec { session =>
                  session("total_net").asOption[String] match {
                    case Some(v) => session.set("total_net", v.replace(",", ""))
                    case None    => session
                  }
                }
                .exec { session =>
                  val pIds  = session("productIds").asOption[Vector[String]].getOrElse(Vector.empty)
                  val pQtys = session("productQuantities").asOption[Vector[String]].getOrElse(Vector.empty)

                  if (pIds.isEmpty || pQtys.isEmpty) {
                    println(s"No products in cart for VU=${session.userId}")
                    session.markAsFailed
                  } else {
                    val params: List[(String, String)] =
                      pIds.zip(pQtys).flatMap { case (id, qty) =>
                        List("p_id[]" -> id, "p_quantity[]" -> qty)
                      }.toList

                    session.set("productParams", params)
                  }
                }
                .pause(MIN_THINK_TIME, MAX_THINK_TIME)
                .exec(placeOrder())
                .pause(MIN_THINK_TIME, MAX_THINK_TIME)
                .exec(feed(baseHelpers.billingFeeder))
                .exec(submitOrder())
            )
          )
      }
  }
}
