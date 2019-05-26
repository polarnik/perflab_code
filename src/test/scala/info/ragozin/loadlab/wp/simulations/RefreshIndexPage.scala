package info.ragozin.loadlab.wp.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import scala.language.postfixOps

class RefreshIndexPage  extends Simulation {

  // Настройки (Settings) для протокола http
  val httpConf = http.baseUrl("http://wp.loadlab.ragozin.info")

  // Экземпляр сценария -- обновление главной страницы 200 раз
  val refreshIndexPage = scenario("RefreshIndexPage300").exec(
      // Группа методов -- аналог Transaction Contoller в Apache.JMeter
      group("Refresh IndexPage 50 times") {
        exec(Index.refreshIndexManyTimes(50))
      }
      .group("Refresh IndexPage 100 times") {
        exec(Index.refreshIndexManyTimes(100))
      }
      .group("Refresh IndexPage 150 times") {
        exec(Index.refreshIndexManyTimes(150))
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