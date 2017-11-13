/*
 * Copyright 2017 HM Revenue & Customs
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

import javax.inject.Inject

import config.AppConfig
import config.features.SimpleAuthFeature
import forms.FeatureSwitchForm
import models.FeatureSwitchModel
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, Result}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController

import scala.concurrent.Future

class FeatureSwitchController @Inject()(val messagesApi: MessagesApi,
                                        simpleAuthFeature: SimpleAuthFeature,
                                        implicit val appConfig: AppConfig)
  extends FrontendController with I18nSupport {

  def featureSwitch: Action[AnyContent] = Action.async { implicit request =>
    Future.successful(Ok(views.html.featureSwitch(FeatureSwitchForm.form.fill(
      FeatureSwitchModel(simpleAuthEnabled = simpleAuthFeature.enabled)
    ))))
  }

  def submitFeatureSwitch = Action { implicit request =>
    FeatureSwitchForm.form.bindFromRequest().fold(
      hasErrors = _ => Ok("miranda is a noob"),
      success = handleSuccess
    )
  }

  def handleSuccess(model: FeatureSwitchModel): Result = {
    simpleAuthFeature.switchTo(model.simpleAuthEnabled)
    Redirect(routes.HelloWorldController.helloWorld())
  }

}
