package info.ragozin.loadlab.wp.scenariofragments

import info.ragozin.loadlab.wp.actions
import scala.language.postfixOps
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class RefreshIndexPage500 {
  val indexPage = new actions.IndexPage();

  // Сценарий (ScenarioFragment) -- обновление главной страницы несколько раз
  def refreshIndexManyTimes(times: Int = 1 ) = repeat(times) {
    indexPage.get
  }

  // Экземпляр сценария -- обновление главной страницы 500 раз
  val refreshIndexPage500 = scenario("RefreshIndexPage500").exec(
    // Группа методов -- аналог Transaction Contoller в Apache.JMeter
    group("Refresh Index 500") {
      exec(refreshIndexManyTimes(500))
    }
  )
}
