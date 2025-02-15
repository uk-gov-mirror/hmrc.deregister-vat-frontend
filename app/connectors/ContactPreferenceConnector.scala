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

package connectors

import config.AppConfig
import connectors.httpParsers.ContactPreferenceHttpParser.ContactPreferenceReads
import javax.inject.{Inject, Singleton}
import models.ErrorModel
import models.contactPreferences.ContactPreference
import play.api.Logger
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.http.HttpClient
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ContactPreferenceConnector @Inject()(val http: HttpClient,
                                           val config: AppConfig) {

  def getContactPreference(vrn: String)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Either[ErrorModel, ContactPreference]] = {
    val url: String = config.contactPreferencesUrl(vrn)
    Logger.debug(s"[ContactPreferenceConnector][getContactPreference]: Calling contact preferences with URL - $url")
    http.GET[Either[ErrorModel, ContactPreference]](url)(ContactPreferenceReads, hc, ec)
  }
}
