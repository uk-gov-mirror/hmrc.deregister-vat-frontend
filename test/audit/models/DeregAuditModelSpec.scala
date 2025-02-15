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

package audit.models

import utils.TestUtil
import assets.constants.DeregistrationInfoTestConstants._
import play.api.libs.json.Json

class DeregAuditModelSpec extends TestUtil{

  val auditType: String = "SubmitVatDeregistrationRequest"

  lazy val agentHandOff = DeregAuditModel(agentUserPrefYes, deregistrationInfoMaxModel)
  lazy val userHandOff = DeregAuditModel(user, deregistrationInfoMaxModel)

  "DeregAuditModel" should {

    s"have the correct audit type $auditType" in {
      DeregAuditModel.auditType shouldBe auditType
    }

    "user is an Agent" should {

      "have the correct details for the audit event" in {
        Json.toJson(agentHandOff) shouldBe Json.obj(
          "isAgent" -> true,
          "agentReferenceNumber" -> agentUserPrefYes.arn.get,
          "vrn" -> agentUserPrefYes.vrn,
          "deregistrationInfo" -> deregistrationInfoMaxModel
        )
      }
    }

    "user is a non-Agent" should {

      "have the correct details for the audit event" in {
        Json.toJson(userHandOff) shouldBe Json.obj(
          "isAgent" -> false,
          "vrn" -> user.vrn,
          "deregistrationInfo" -> deregistrationInfoMaxModel
        )
      }
    }
  }

}
