package info.ragozin.loadlab.wp.simulations

import info.ragozin.loadlab.wp._
import io.gatling.core.Predef._
import scala.language.postfixOps

class RefreshIndexPageWithStructure extends Simulation {

  // Настройки (Settings) для протокола http
  val httpConf = settings.Protocol.httpConf("http://wp.loadlab.ragozin.info")

  // Экземпляр сценария -- обновление главной страницы 500 раз
  val refreshIndexPage500 = new scenariofragments.RefreshIndexPage500().refreshIndexPage500

  // Запуск теста
  setUp(
    // Профиль нагрузки -- запуск одного пользователя
    refreshIndexPage500.inject(atOnceUsers(1)).protocols(httpConf)
  )
}