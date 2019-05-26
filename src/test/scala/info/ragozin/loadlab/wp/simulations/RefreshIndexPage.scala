package info.ragozin.loadlab.wp.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import scala.language.postfixOps

class RefreshIndexPage  extends Simulation {

  // Настройки (Settings) для протокола http
  val httpConf = http.baseUrl("http://wp.loadlab.ragozin.info")

  // Экземпляр сценария -- обновление главной страницы 500 раз
  val refreshIndexPage = scenario("RefreshIndexPage500").exec(
      // Группа методов -- аналог Transaction Contoller в Apache.JMeter
      group("Refresh Index 200") {
        // Группа может содержать одно действие или фрагмент сценария
        exec(Index.refreshIndexManyTimes(200))
      }
      .group("Refresh Index 300") {
        // Или несколько действий
        exec(Index.refreshIndexManyTimes(10))
          .exec(Index.refreshIndexManyTimes(20))
          .exec(Index.refreshIndexManyTimes(30))
          .exec(Index.refreshIndexManyTimes(40))
          .exec(Index.refreshIndexManyTimes(200))
      }
  )

  // Запуск теста
  setUp(
    // Профиль нагрузки -- запуск одного пользователя
    refreshIndexPage.inject(atOnceUsers(1)).protocols(httpConf)
  )
}

object Index {

  // Действие (Action) -- открытие главной страницы
  def openIndexPage =
    exec(http("Index").get("/").check(status.is(200)))

  // Сценарий (ScenarioFragment) -- обновление главной страницы несколько раз
  def refreshIndexManyTimes(times: Int = 1 ) = repeat(times) {
    openIndexPage
  }
}