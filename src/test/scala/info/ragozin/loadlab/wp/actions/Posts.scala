package info.ragozin.loadlab.wp.actions

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.language.postfixOps

class Posts {

  def getListPosts(page: io.gatling.core.session.Expression[scala.Any]) =
      http("/wp/v2/posts?{page} (GET)")
        .get("/wp-json/wp/v2/posts")
        .queryParam("page", page)
        .check(header("X-WP-TotalPages").saveAs("ListPosts_TotalPages"))
        .check(header("X-WP-Total").saveAs("ListPosts_Total"))


  def getListPosts(page: Int = 1) =
      http("/wp/v2/posts?{page} (GET)")
      .get("/wp-json/wp/v2/posts")
        .queryParam("page", page)
        .check(header("X-WP-TotalPages").saveAs("ListPosts_TotalPages"))
        .check(header("X-WP-Total").saveAs("ListPosts_Total"))

  def getListPosts(map: Map[String, Any]) =
      http("/wp/v2/posts?{...} (GET)")
        .get("/wp-json/wp/v2/posts")
        .queryParamMap(map)
        .check(header("X-WP-TotalPages").saveAs("ListPosts_TotalPages"))
        .check(header("X-WP-Total").saveAs("ListPosts_Total"))

  def getRetrievePost(id: Int) =
      http("/wp/v2/posts?page/{id} (GET)")
        .get(s"/wp-json/wp/v2/posts/$id")


}
