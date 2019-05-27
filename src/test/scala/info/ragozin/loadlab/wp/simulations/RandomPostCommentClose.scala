package info.ragozin.loadlab.wp.simulations

import info.ragozin.loadlab.wp.{scenariofragments, settings}
import io.gatling.core.Predef.{exec, _}
import scala.language.postfixOps
import scala.concurrent.duration._


class RandomPostCommentClose extends Simulation {

  val csvComments = csv("data/comments_20k_seed1.csv").random.circular
  val csvUsers = csv("data/users_100.csv").random.circular

  // Настройки (Settings) для протокола http
  val httpConf = settings.Protocol.httpConf("http://wp.loadlab.ragozin.info")

  val randomPost = new scenariofragments.RandomPost();

  // Экземпляр сценария -- открытие случайной записи и добавление комментария к ней
  val randomPostComment = scenario("RandomPostCommentClose")
    .feed(csvComments)
    .feed(csvUsers)
    .exec(
      randomPost.randomPostPage
    ).exec(
    randomPost.randomPost
  ).exec(
    randomPost.comment("${CommentText}")
      .basicAuth("${login}", "${password}")
  )


  // Запуск теста
  setUp(
    randomPostComment.inject(

      constantConcurrentUsers(1) during (30 seconds),
      rampConcurrentUsers(1) to (10) during (30 seconds)
    ).protocols(httpConf)
  )
}
