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

package models.deregistrationRequest

import play.api.libs.json.{JsString, Json}
import uk.gov.hmrc.play.test.UnitSpec

class BelowThresholdReasonSpec extends UnitSpec {

  "BelowThreshold" when {

    "serializing to JSON" should {

      "for BelowPast12Months output 'belowPast12Months'" in {
        Json.toJson(BelowPast12Months) shouldBe JsString("belowPast12Months")
      }

      "for BelowNext12Months output 'belowNext12Months'" in {
        Json.toJson(BelowNext12Months) shouldBe JsString("belowNext12Months")
      }
    }
  }
}
