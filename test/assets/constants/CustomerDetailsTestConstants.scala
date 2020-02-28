/*
 * Copyright 2020 HM Revenue & Customs
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

package assets.constants

import models.{IndicatorModel, CustomerDetails, PendingDeregModel}
import play.api.libs.json.{JsObject, Json}

object CustomerDetailsTestConstants {

  val orgName = "Ancient Antiques Ltd"
  val tradingName = "Dusty Relics"
  val firstName = "Fred"
  val lastName = "Flintstone"

  val customerDetailsJsonMax: JsObject = Json.obj(
    "organisationName" -> orgName,
    "firstName" -> firstName,
    "lastName" -> lastName,
    "tradingName" -> tradingName,
    "hasFlatRateScheme" -> false,
    "ppob" -> Json.obj("contactDetails" -> Some(Json.obj("emailVerified" -> Some(true))))
  )


  val customerDetailsJsonMin: JsObject = Json.obj(
    "hasFlatRateScheme" -> false
  )

  val customerDetailsMax: CustomerDetails = CustomerDetails(
    Some(firstName),
    Some(lastName),
    Some(orgName),
    Some(tradingName),
    Some(true)
  )

  val customerDetailsMin: CustomerDetails = CustomerDetails(
    None,
    None,
    None,
    None,
    None
  )

  val customerDetailsUnverifiedEmail: CustomerDetails = CustomerDetails(
    Some(firstName),
    Some(lastName),
    Some(orgName),
    Some(tradingName),
    Some(false)
  )

  val pendingDeregFalseJson: JsObject = Json.obj(
    "changeIndicators" -> Json.obj(
      "deregister" -> false
    )
  )

  val pendingDeregFalse: IndicatorModel = IndicatorModel(Some(PendingDeregModel(dereg = false)))
  val pendingDeregTrue: IndicatorModel = IndicatorModel(Some(PendingDeregModel(dereg = true)))

  val noPendingDereg: IndicatorModel = IndicatorModel(None)
}
