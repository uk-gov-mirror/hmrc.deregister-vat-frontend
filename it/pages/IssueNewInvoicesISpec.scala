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

package pages

import assets.IntegrationTestConstants._
import forms.YesNoForm
import helpers.IntegrationBaseSpec
import models.{No, Yes, YesNo}
import play.api.http.Status._
import play.api.libs.json.Json
import play.api.libs.ws.WSResponse
import services.{IssueNewInvoicesAnswerService, OutstandingInvoicesAnswerService}
import stubs.DeregisterVatStub


class IssueNewInvoicesISpec extends IntegrationBaseSpec {

  "Calling the GET IssueNewInvoices" when {

    def getRequest(): WSResponse = get("/issue-new-invoices")

    "the user is authorised" should {

      "return 200 OK" in {

        given.user.isAuthorised

        DeregisterVatStub.successfulGetAnswer(vrn,IssueNewInvoicesAnswerService.key)(Json.toJson(Yes))

        val response: WSResponse = getRequest()

        response should have(
          httpStatus(OK),
          pageTitle("Is the business expecting to issue any new invoices after deregistering?")
        )
      }
    }

    "the user is not authenticated" should {

      "return 401 UNAUTHORIZED" in {

        given.user.isNotAuthenticated

        val response: WSResponse = getRequest()

        response should have(
          httpStatus(UNAUTHORIZED),
          pageTitle("Your session has timed out")
        )
      }
    }

    "the user is not authorised" should {

      "return 403 FORBIDDEN" in {

        given.user.isNotAuthorised

        val response: WSResponse = getRequest()

        response should have(
          httpStatus(FORBIDDEN),
          pageTitle("You can’t use this service yet")
        )
      }
    }
  }


  "Calling the POST IssueNewInvoices" when {

    def postRequest(data: YesNo): WSResponse =
      post("/issue-new-invoices")(toFormData(YesNoForm.yesNoForm, data))

    "the user is authorised" when {

      "posting 'Yes' data" should {

        "return 303 SEE_OTHER" in {

          given.user.isAuthorised

          DeregisterVatStub.successfulPutAnswer(vrn,IssueNewInvoicesAnswerService.key)
          DeregisterVatStub.successfulDeleteAnswer(vrn,OutstandingInvoicesAnswerService.key)

          val response: WSResponse = postRequest(Yes)

          response should have(
            httpStatus(SEE_OTHER),
            redirectURI(controllers.routes.DeregistrationDateController.show().url)
          )
        }
      }
    }

    "the user is authorised" when {

      "posting 'No' data" should {

        "return 303 SEE_OTHER" in {

          given.user.isAuthorised

          DeregisterVatStub.successfulPutAnswer(vrn,IssueNewInvoicesAnswerService.key)

          val response: WSResponse = postRequest(No)

          response should have(
            httpStatus(SEE_OTHER),
            redirectURI(controllers.routes.OutstandingInvoicesController.show().url)
          )
        }
      }
    }

    "the user is authorised" when {

      "posting 'Yes' data and an error is returned when deleting redundant data" should {

        "return 303 SEE_OTHER" in {

          given.user.isAuthorised

          DeregisterVatStub.successfulPutAnswer(vrn,IssueNewInvoicesAnswerService.key)
          DeregisterVatStub.deleteAnswerError(vrn,OutstandingInvoicesAnswerService.key)

          val response: WSResponse = postRequest(Yes)

          response should have(
            httpStatus(INTERNAL_SERVER_ERROR)
          )
        }
      }
    }

    "the user is not authenticated" should {

      "return 401 UNAUTHORIZED" in {

        given.user.isNotAuthenticated

        val response: WSResponse = postRequest(Yes)

        response should have(
          httpStatus(UNAUTHORIZED),
          pageTitle("Your session has timed out")
        )
      }
    }

    "the user is not authorised" should {

      "return 403 FORBIDDEN" in {

        given.user.isNotAuthorised

        val response: WSResponse = postRequest(No)

        response should have(
          httpStatus(FORBIDDEN),
          pageTitle("You can’t use this service yet")
        )
      }
    }
  }
}