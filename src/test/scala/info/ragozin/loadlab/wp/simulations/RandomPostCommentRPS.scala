package info.ragozin.loadlab.wp.simulations

import info.ragozin.loadlab.wp.{scenariofragments, settings}
import io.gatling.core.Predef.{exec, _}
import scala.language.postfixOps
import scala.concurrent.duration._


class RandomPostCommentRPS extends Simulation {

  val csvComments = csv("data/comments_20k_seed1.csv").random.circular
  val csvUsers = csv("data/users_100.csv").random.circular

  // Настройки (Settings) для протокола http
  val httpConf = settings.Protocol.httpConf("http://wp.loadlab.ragozin.info")

  val randomPost = new scenariofragments.RandomPost();

  // Экземпляр сценария -- открытие случайной записи и добавление комментария к ней
  val randomPostComment = scenario("RandomPostCommentRPS")
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

  /**
    * nothingFor(duration) — указывается длительность паузы duration перед стартом нагрузки
    * atOnceUsers(nbUsers) — виртуальные пользователи в количестве nbUsers будут “подниматься” сразу (по готовности).
    * rampUsers(nbUsers) during (duration) — в течение времени duration будут "подниматься" виртуальные пользователи в количестве nbUsers через равные временные интервалы.
    * constantUsersPerSec(rate) during(duration) — указывается частота “поднятия” виртуальных пользователей rate (вирт. польз. в секунду) и временной интервал duration. В течении duration количество виртуальных пользователей будет увеличиваться на rate каждую секунду.
    * constantUsersPerSec(rate) during(duration) randomized — аналогично верхней конструкции только временные интервалы между "поднятием" виртуальных пользователей будут случайными.
    * rampUsersPerSec(rate1) to (rate2) during(duration) — в течение времени duration виртуальные пользователи будут увеличиваться с частоты rate1 до частоты rate2.
    * rampUsersPerSec(rate1) to(rate2) during(duration) randomized — аналогично верхней конструкции только временные интервалы между "поднятиями" виртуальных пользователей будут случайными.
    * splitUsers(nbUsers) into(injectionStep) separatedBy(duration) — через каждый временной интервал duration будут добавляться виртуальные пользователи по модели injectionStep, пока их количество не достигнет nbUsers. В injectionStep можно указать модели описанные выше.
    * splitUsers(nbUsers) into(injectionStep1) separatedBy(injectionStep2) — аналогично верхней конструкции только разделителем модель injectionStep2.
    * heavisideUsers(nbUsers) over(duration) — виртуальные пользователи в количестве nbUsers будут подниматься ступенями за время duration.
    */

  // Запуск теста
  setUp(
    randomPostComment.inject(
      // в течение 30 секунд будет запускаться по 5 пользователей в сек
      constantUsersPerSec(5) during (30 seconds)
    ).throttle(
      // Лесенка с 0 до 10
      reachRps(10) in (30 seconds),
      // Полочка 10
      holdFor(30 seconds),
      // Лесенка с 10 до 0
      reachRps(1) in (30 seconds),
      // Скачок на 10
      jumpToRps(10),
      // Полочка 10
      holdFor(30 seconds),
    ).protocols(httpConf)
  )

}
