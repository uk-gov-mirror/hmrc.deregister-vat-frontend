/*
 * Copyright 2018 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers

import config.{AppConfig, ServiceErrorHandler}
import controllers.predicates.AuthPredicate
import forms.WhyTurnoverBelowForm
import javax.inject.{Inject, Singleton}
import models.DeregisterVatSuccess
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import services.WhyTurnoverBelowAnswerService
import uk.gov.hmrc.play.bootstrap.controller.FrontendController

import scala.concurrent.Future

@Singleton
class WhyTurnoverBelowController @Inject()(val messagesApi: MessagesApi,
                                           val authenticate: AuthPredicate,
                                           val whyTurnoverBelowAnswerService: WhyTurnoverBelowAnswerService,
                                           val serviceErrorHandler: ServiceErrorHandler,
                                           implicit val appConfig: AppConfig) extends FrontendController with I18nSupport {

  val show: Action[AnyContent] = authenticate.async { implicit user =>
    whyTurnoverBelowAnswerService.getAnswer.map {
      case Right(Some(data)) => Ok(views.html.whyTurnoverBelow(WhyTurnoverBelowForm.whyTurnoverBelowForm.fill(data)))
      case _ => Ok(views.html.whyTurnoverBelow(WhyTurnoverBelowForm.whyTurnoverBelowForm))
    }
  }

  val submit: Action[AnyContent] = authenticate.async { implicit user =>
    WhyTurnoverBelowForm.whyTurnoverBelowForm.bindFromRequest().fold(
      error => Future.successful(BadRequest(views.html.whyTurnoverBelow(error))),
      data => {
        whyTurnoverBelowAnswerService.storeAnswer(data).map {
          case Right(DeregisterVatSuccess) => Redirect(controllers.routes.VATAccountsController.show())
          case Left(_) => serviceErrorHandler.showInternalServerError
        }
      }
    )
  }
}
