/*
 * Copyright 2021 HM Revenue & Customs
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

package testOnly.controllers

import config.AppConfig
import javax.inject.Inject
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents, Result}
import testOnly.forms.FeatureSwitchForm
import testOnly.models.FeatureSwitchModel
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import testOnly.views.html.FeatureSwitch

class FeatureSwitchController @Inject()(featureSwitch: FeatureSwitch,
                                        val mcc: MessagesControllerComponents,
                                        implicit val appConfig: AppConfig)
  extends FrontendController(mcc) with I18nSupport {

  def featureSwitch: Action[AnyContent] = Action { implicit request =>
    Ok(featureSwitch(FeatureSwitchForm.form.fill(
      FeatureSwitchModel(
        stubAgentClientLookup = appConfig.features.stubAgentClientLookup(),
        stubContactPreferences = appConfig.features.stubContactPreferences(),
        useLanguagePreferencesFeature = appConfig.features.useLanguageSelector(),
        accessibilityStatementEnabled = appConfig.features.accessibilityStatement(),
        zeroRatedJourney = appConfig.features.zeroRatedJourney(),
        emailVerifiedFeature = appConfig.features.emailVerifiedFeature(),
        bulkPaperOffFeature = appConfig.features.bulkPaperOffFeature(),
        contactPrefMigrationFeature = appConfig.features.contactPrefMigrationFeature()
      )
    )))
  }

  def submitFeatureSwitch: Action[AnyContent] = Action { implicit request =>
    FeatureSwitchForm.form.bindFromRequest().fold(
      _ => Redirect(routes.FeatureSwitchController.featureSwitch()),
      success = handleSuccess
    )
  }

  def handleSuccess(model: FeatureSwitchModel): Result = {
    appConfig.features.stubAgentClientLookup(model.stubAgentClientLookup)
    appConfig.features.stubContactPreferences(model.stubContactPreferences)
    appConfig.features.useLanguageSelector(model.useLanguagePreferencesFeature)
    appConfig.features.accessibilityStatement(model.accessibilityStatementEnabled)
    appConfig.features.zeroRatedJourney(model.zeroRatedJourney)
    appConfig.features.emailVerifiedFeature(model.emailVerifiedFeature)
    appConfig.features.bulkPaperOffFeature(model.bulkPaperOffFeature)
    appConfig.features.contactPrefMigrationFeature(model.contactPrefMigrationFeature)
    Redirect(routes.FeatureSwitchController.featureSwitch())
  }

}
