package info.ragozin.loadlab.wp.actions

import io.gatling.core.Predef.exec
import io.gatling.http.Predef.{http, status}
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import scala.language.postfixOps

class IndexPage {
  val get =
    exec(http("Index").get("/").check(status.is(200)))
}
