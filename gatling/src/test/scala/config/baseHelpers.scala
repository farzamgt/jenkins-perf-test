package config

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.util.Random
import java.util.UUID

object baseHelpers {

  val MIN_THINK_TIME: Int = Integer.parseInt(System.getProperty("minThinkTime", "1")) // seconds
  val MAX_THINK_TIME: Int = Integer.parseInt(System.getProperty("maxThinkTime", "5")) // seconds
  val CHAIRS: Double = java.lang.Double.parseDouble(System.getProperty("chairsRatio", "0.5")) // Double to define percentage for chairs
  val ORDERS: Double = java.lang.Double.parseDouble(System.getProperty("ordersRatio", "0.3")) // Double to define percentage for orders
  val scnOrdersUsers: Int = Integer.parseInt(System.getProperty("users", "5")) // number of users
  val scnOrdersRampUp: Int = Integer.parseInt(System.getProperty("rampUp", "1")) // in seconds
  val assertionType: String = System.getProperty("assertionType", "defaultAssertion") // to set assertion map
  val BASE_URL: String = System.getProperty("baseUrl", "http://localhost")

  val httpProtocolWithoutInferHtmlResources = http
    .baseUrl(BASE_URL)
    .acceptHeader("*/*")
    .acceptEncodingHeader("gzip, deflate, br, zstd")
    .acceptLanguageHeader("en-GB,en-US;q=0.9,en;q=0.8")
    .upgradeInsecureRequestsHeader("1")
    .userAgentHeader(
      "Mozilla/5.0 (----Gatling 3.8----  Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36"
    )

  val billingFeeder = Iterator.continually(
    Map(
      "cart_name"    -> s"User_${Random.nextInt(100000)}",
      "cart_address" -> s"Street_${UUID.randomUUID().toString.take(8)}",
      "cart_postal"  -> (10000 + Random.nextInt(90000)).toString,
      "cart_city"    -> s"city_${UUID.randomUUID().toString.take(6)}",
      "cart_country" -> "UA",
      "cart_phone"   -> s"38050${100000 + Random.nextInt(900000)}",
      "cart_email"   -> s"user_${UUID.randomUUID().toString.take(6)}@test.com"
    )
  )
}
