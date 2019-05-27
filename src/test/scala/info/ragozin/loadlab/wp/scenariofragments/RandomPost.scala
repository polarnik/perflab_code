package info.ragozin.loadlab.wp.scenariofragments

import info.ragozin.loadlab.wp.actions

import scala.language.postfixOps
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class RandomPost {

  val posts = new actions.Posts()
  val comments = new actions.Comments()

  val randomPostPage = {

    exec(session =>
      session.set("pageNumber", (Math.random() * 100.0).toInt + 1)
    ).exec(
      posts.getListPosts("${pageNumber}")
        .check(jsonPath("$..id").saveAs("randomPostId"))
    )
  }

  val randomPost = {
    http("/wp/v2/posts?page/{id} (GET)")
      .get("/wp-json/wp/v2/posts/${randomPostId}")
  }

  def comment(comment: String) = {
    comments.postCreateComment("${randomPostId}", comment)
  }

}
