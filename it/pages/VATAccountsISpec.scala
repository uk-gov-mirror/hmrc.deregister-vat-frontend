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
import forms.{DeregistrationReasonForm, VATAccountsForm}
import helpers.IntegrationBaseSpec
import models._
import play.api.http.Status._
import play.api.libs.json.Json
import play.api.libs.ws.WSResponse
import services._
import stubs.DeregisterVatStub


class VATAccountsISpec extends IntegrationBaseSpec {

  "Calling the GET VatAccounts" when {

    def getRequest(): WSResponse = get("/accounting-method")

    "the user is authorised" should {

      "return 200 OK" in {

        given.user.isAuthorised

        DeregisterVatStub.successfulGetAnswer(vrn,AccountingMethodAnswerService.key)(Json.toJson(StandardAccounting))
        DeregisterVatStub.successfulGetAnswer(vrn,DeregReasonAnswerService.key)(Json.toJson(Ceased))

        val response: WSResponse = getRequest()

        response should have(
          httpStatus(OK),
          pageTitle("How are the business’s VAT accounts prepared?")
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


  "Calling the POST VatAccounts" when {

    def postRequest(data: VATAccountsModel): WSResponse =
      post("/accounting-method")(toFormData(VATAccountsForm.vatAccountsForm, data))


    "the user is authorised" when {

      "the post request includes valid data" should {

        "return 303 SEE_OTHER" in {

          given.user.isAuthorised

          DeregisterVatStub.successfulPutAnswer(vrn,AccountingMethodAnswerService.key)

          val response: WSResponse = postRequest(StandardAccounting)

          response should have(
            httpStatus(SEE_OTHER),
            redirectURI(controllers.routes.OptionTaxController.show().url)
          )
        }
      }

      "the post returns an error" should {

        "return 500 INTERNAL_SERVER_ERROR" in {

          given.user.isAuthorised

          DeregisterVatStub.putAnswerError(vrn,AccountingMethodAnswerService.key)

          val response: WSResponse = postRequest(StandardAccounting)

          response should have(
            httpStatus(INTERNAL_SERVER_ERROR)
          )
        }
      }
    }

    "the user is not authenticated" should {

      "return 401 UNAUTHORIZED" in {

        given.user.isNotAuthenticated

        val response: WSResponse = postRequest(StandardAccounting)

        response should have(
          httpStatus(UNAUTHORIZED),
          pageTitle("Your session has timed out")
        )
      }
    }

    "the user is not authorised" should {

      "return 403 FORBIDDEN" in {

        given.user.isNotAuthorised

        val response: WSResponse = postRequest(StandardAccounting)

        response should have(
          httpStatus(FORBIDDEN),
          pageTitle("You can’t use this service yet")
        )
      }
    }

  }
}