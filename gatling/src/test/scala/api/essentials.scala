package api

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import config.baseHelpers.BASE_URL

object essentials {

  def home() =
    group("Page: Home") {
      exec(
        http("001_Home")
          .get("/")
          .check(css("h1.entry-title").is("All Products"))
      )
    }

  def navigateToCategory(category: String) =
    group("Page: Category") {
      exec(
        http(s"002_Open_Category - $category")
          .get(s"/$category")
          .check(
            regex("/products/([^\"']+)")
              .findRandom
              .saveAs("product")
          )
      )
    }

  def navigateToProduct(product: String) =
    group("Page: Product Card") {
      exec(
        http(s"003_Open_Product_Card - $product")
          .get(s"/products/#{${product}}")
          .check(
            regex("""name="current_product" value="(\d+)"""").saveAs("currentProduct"),
            regex("""name="cart_content" value='([^']*)'""").optional.saveAs("currentCartContent")
          )
      )
    }

  def addProductToCart(quantity: String, prefix: String) =
    group("Action: Add to Cart") {
      exec { session =>
        val productId       = session("currentProduct").as[String]
        val productQuantity = session(quantity).as[String]
        val cartContent =
          if (session.contains("currentCartContent"))
            session("currentCartContent").as[String]
          else "{}"

        val addData =
          s"current_product=$productId&cart_content=$cartContent&current_quantity=$productQuantity"

        session.set("addData", addData)
      }.exec(
        http(s"004_Add_to_Cart - $prefix")
          .post("/wp-admin/admin-ajax.php")
          .header("X-Requested-With", "XMLHttpRequest")
          .formParam("action", "ic_add_to_cart")
          .formParam("add_cart_data", "#{addData}")
          .formParam("cart_widget", "0")
          .formParam("cart_container", "0")
          .check(
            regex("\"cart-added-info\"").exists,
            regex("Added!").exists
          )
      )
    }

  def openCart() =
    group("Page: Cart") {
      exec(
        http("005_Open_Cart")
          .get("/cart")
          .check(
            css("input[name='cart_content']", "value").saveAs("cart_content"),
            css("input[name='total_net']", "value").saveAs("total_net"),
            css("input[name='trans_id']", "value").saveAs("trans_id"),
            css("input[name='p_id[]']", "value").findAll.saveAs("productIds"),
            css("input[name='p_quantity[]']", "value").findAll.saveAs("productQuantities"),
            css("input.edit-product-quantity", "data-price").findAll.saveAs("productPrices")
          )
      )
    }

  def placeOrder() =
    group("Page: Checkout") {
      exec(
        http("006_Place_Order")
          .post("/checkout")
          .formParam("cart_content", "#{cart_content}")
          .formParam("total_net", "#{total_net}")
          .formParam("trans_id", "#{trans_id}")
          .formParam("shipping", "order")
          .formParamSeq(session => session("productParams").as[List[(String, Any)]])
          .check(
            css("h1.entry-title").is("Checkout"),
            css("input[name='trans_id']", "value").saveAs("checkout_trans_id")
          )
      )
    }

  def submitOrder() =
    group("Page: Submit Order") {
      exec(
        http("007_Submit_Order")
          .post("/checkout")
          .formParam("ic_formbuilder_redirect", s"$BASE_URL/thank-you")
          .formParam("cart_content", "#{cart_content}")
          .formParam("cart_type", "order")
          .formParam("total_net", "#{total_net}")
          .formParam("trans_id", "#{checkout_trans_id}")
          .formParam("shipping", "order")
          .formParamSeq { session =>
            val ids =
              session("productIds").asOption[Vector[String]].getOrElse(Vector.empty).toList
            val prices =
              session("productPrices").asOption[Vector[String]].getOrElse(Vector.empty).toList
            ids.zip(prices).map { case (id, price) =>
              "product_price_" + id.replace("__", "") -> price
            }
          }
          .formParam("cart_inside_header_1", "<b>BILLING ADDRESS</b>")
          .formParam("cart_name", "#{cart_name}")
          .formParam("cart_address", "#{cart_address}")
          .formParam("cart_postal", "#{cart_postal}")
          .formParam("cart_city", "#{cart_city}")
          .formParam("cart_country", "#{cart_country}")
          .formParam("cart_phone", "#{cart_phone}")
          .formParam("cart_email", "#{cart_email}")
          .formParam("cart_comment", session =>
            s"VU=${session.userId}, trans_id=${session("checkout_trans_id").as[String]}"
          )
          .formParam("cart_submit", "Place Order")
      )
    }
}
