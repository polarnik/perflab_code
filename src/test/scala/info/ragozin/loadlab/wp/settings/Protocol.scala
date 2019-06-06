package info.ragozin.loadlab.wp.settings

import io.gatling.core.Predef._
import io.gatling.http.Predef._

object Protocol {
  def httpConf(url: String) = http
    .baseUrl(url) // Here is the root for all relative URLs
    .inferHtmlResources() // Загрузка всех ресурсов
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // Here are the common headers
    .doNotTrackHeader("1")
    .acceptEncodingHeader("gzip, deflate, br")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .maxConnectionsPerHostLikeFirefox
    .header("Upgrade-Insecure-Requests", "1")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")
    .disableWarmUp

  //.shareConnections
  //.proxy(Proxy("127.0.0.1", 8888))
  //.header("X-Forwarded-For", "${user_ip_address}")
}
