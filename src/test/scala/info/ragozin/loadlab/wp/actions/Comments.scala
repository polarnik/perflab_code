package info.ragozin.loadlab.wp.actions

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.language.postfixOps

class Comments {
  def postCreateComment(post: Int, content: String) =
      http("/wp-json/wp/v2/comments (POST)")
        .post("/wp-json/wp/v2/comments")
        .formParam("post", post)
        .formParam("content", content)

  def postCreateComment(post: io.gatling.core.session.Expression[scala.Any], content: io.gatling.core.session.Expression[scala.Any]) =
      http("/wp-json/wp/v2/comments (POST)")
        .post("/wp-json/wp/v2/comments")
        .formParam("post", post)
        .formParam("content", content)
}
