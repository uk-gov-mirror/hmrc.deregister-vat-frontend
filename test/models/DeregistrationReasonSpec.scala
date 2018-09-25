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

package models

import play.api.libs.json.{JsString, Json}
import utils.TestUtil

class DeregistrationReasonSpec extends TestUtil {

  "DeregistrationReason.Ceased" should {

    "serialize to the correct JSON" in {
      Json.toJson(Ceased) shouldBe JsString(Ceased.value)
    }

    "deserialize from the correct JSON" in {
      Json.obj(DeregistrationReason.id -> Ceased.value).as[DeregistrationReason] shouldBe Ceased
    }
  }

  "DeregistrationReason.BelowThreshold" should {

    "serialize to the correct JSON" in {
      Json.toJson(BelowThreshold) shouldBe JsString(BelowThreshold.value)
    }

    "deserialize from the correct JSON" in {
      Json.obj(DeregistrationReason.id -> BelowThreshold.value).as[DeregistrationReason] shouldBe BelowThreshold
    }
  }

  "DeregistrationReason.Other" should {

    "serialize to the correct JSON" in {
      Json.toJson(Other) shouldBe JsString(Other.value)
    }

    "deserialize from the correct JSON" in {
      Json.obj(DeregistrationReason.id -> Other.value).as[DeregistrationReason] shouldBe Other
    }
  }
}
