package info.ragozin.loadlab.wp.simulations

import info.ragozin.loadlab.wp.{scenariofragments, settings}
import io.gatling.core.Predef.{exec, _}
import scala.language.postfixOps
import scala.concurrent.duration._


class RandomPostComment extends Simulation {

  val csvComments = csv("data/comments_20k_seed1.csv").random.circular
  val csvUsers = csv("data/users_100.csv").random.circular

  // Настройки (Settings) для протокола http
  val httpConf = settings.Protocol.httpConf("http://wp.loadlab.ragozin.info")

  val randomPost = new scenariofragments.RandomPost();

  // Экземпляр сценария -- открытие случайной записи и добавление комментария к ней
  val randomPostComment = scenario("RandomPostComment")
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
      // ничего не происходит 5 сек
      nothingFor(5 seconds),

      // 20 пользователей запускаются одновременно
      atOnceUsers(20),

      nothingFor(5 seconds),

      //10 пользователей запускаются равномерно в течение 30 сек (1/3 пользователь в сек)
      rampUsers(10) during (30 seconds),

      nothingFor(5 seconds),

      // в течение 30 секунд будет запускаться по 2 пользователей в сек
      constantUsersPerSec(2) during (30 seconds),

      nothingFor(5 seconds),

      // в течение 10 секунд будет запускаться по 5 пользователей в сек (15 запросов в сек) -- случайные интервалы
      constantUsersPerSec(2) during (30 seconds) randomized,

      nothingFor(5 seconds),

      // в течение 30 секунд будет запускаться по 1 пользователю в сек -- сначала
      // и по 4 пользователей в сек -- в конце
      rampUsersPerSec(1) to (4) during(30 seconds),

      // в течение 30 секунд будет запускаться по 4 пользователей в сек -- сначала
      // и по 1 пользователю в сек -- в конце
      rampUsersPerSec(4) to (1) during(30 seconds),

      nothingFor(5 seconds),

      // в течение 10 секунд будет запускаться по 3 пользователя в сек -- сначала
      // и по 10 пользователей в сек -- в конце
      // нагрузка будет расти неравномерно, а случайно
      rampUsersPerSec(1) to (3) during(30 seconds) randomized,

      nothingFor(5 seconds),

      // виртуальные пользователи в количестве 4 будут подниматься ступенями за время 30 сек
      heavisideUsers(4) during (30 seconds)
    ).protocols(httpConf)
  )
}
