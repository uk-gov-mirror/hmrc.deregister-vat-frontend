/*
 * Copyright 2019 HM Revenue & Customs
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
import forms.ZeroRatedSuppliesForm
import helpers.IntegrationBaseSpec
import models.{No, NumberInputModel, Yes, ZeroRated}
import play.api.http.Status._
import play.api.libs.json.Json
import play.api.libs.ws.WSResponse
import stubs.DeregisterVatStub
import services._

class ZeroRatedSuppliesISpec extends IntegrationBaseSpec {

  "Calling the GET BusinessActivity" when {

    def getRequest: WSResponse = get("/expected-value-zero-rated-supplies", formatPendingDereg(Some("false")))

    "the user is authorised" should {

      "return 200 OK" in {

        given.user.isAuthorised

        DeregisterVatStub.successfulGetAnswer(vrn, ZeroRatedSuppliesValueService.key)(Json.toJson(zeroRatedSuppliesModel))

        val response: WSResponse = getRequest

        response should have(
          httpStatus(OK),
          pageTitle("What is the expected value of zero-rate supplies for the next 12 months?" + titleSuffix)
        )
      }
    }

    "the user is not authenticated" should {

      "return 303 SEE_OTHER" in {

        given.user.isNotAuthenticated

        val response: WSResponse = getRequest

        response should have(
          httpStatus(SEE_OTHER),
          redirectURI(appConfig.signInUrl)
        )
      }
    }

    "the user is not authorised" should {

      "return 403 FORBIDDEN" in {

        given.user.isNotAuthorised

        val response: WSResponse = getRequest

        response should have(
          httpStatus(FORBIDDEN),
          pageTitle("You can’t use this service yet" + titleSuffixOther)
        )
      }
    }
  }

  "Calling the GET BusinessActivity" when {

    def getRequest(pendingDereg: Option[String]): WSResponse = get("/expected-value-zero-rated-supplies", formatPendingDereg(pendingDereg))

    "user has a pending dereg request" should {

      "redirect the user" in {
        given.user.isAuthorised

        val response: WSResponse = getRequest(Some("true"))

        response should have(
          httpStatus(SEE_OTHER),
          redirectURI(appConfig.manageVatSubscriptionFrontendUrl)
        )
      }
    }

    "no pending dereg data in session and vat-subscription returns 'no pending dereg'" should {

      "redirect user to the start of the journey" in {
        given.user.isAuthorised
        given.user.noDeregPending

        val response: WSResponse = getRequest(None)

        response should have(
          httpStatus(SEE_OTHER),
          redirectURI(controllers.routes.DeregisterForVATController.redirect().url)
        )
      }
    }

    "no pending dereg data in session and vat-subscription returns 'pending dereg'" should {

      "redirect the user" in {
        given.user.isAuthorised
        given.user.deregPending

        val response: WSResponse = getRequest(None)

        response should have(
          httpStatus(SEE_OTHER),
          redirectURI(appConfig.manageVatSubscriptionFrontendUrl)
        )
      }
    }

    "no pending dereg data in session and vat-subscription returns 'None'" should {

      "redirect user to the start of the journey" in {
        given.user.isAuthorised
        given.user.noPendingData

        val response: WSResponse = getRequest(None)

        response should have(
          httpStatus(SEE_OTHER),
          redirectURI(controllers.routes.DeregisterForVATController.redirect().url)
        )
      }
    }
  }



  "Calling the POST ZeroRatedSupplies" when {

    def postRequest(data: NumberInputModel): WSResponse =
      post("/expected-value-zero-rated-supplies")(toFormData(ZeroRatedSuppliesForm.zeroRatedSuppliesForm, data))

    "the user is authorised" when {

      "posting numeric data" should {

        "return 303 SEE_OTHER" in {

          given.user.isAuthorised

          DeregisterVatStub.successfulPutAnswer(vrn, ZeroRatedSuppliesValueService.key)

          val response: WSResponse = postRequest(zeroRatedSuppliesModel)

          response should have(
            httpStatus(SEE_OTHER),
            redirectURI(controllers.zeroRated.routes.PurchasesExceedSuppliesController.show().url)
          )
        }
      }
    }

    "the user is not authenticated" should {

      "return 303 Redirect" in {

        given.user.isNotAuthenticated

        val response: WSResponse = postRequest(zeroRatedSuppliesModel)

        response should have(
          httpStatus(SEE_OTHER),
          redirectURI(appConfig.signInUrl)
        )
      }
    }

    "the user is not authorised" should {

      "return 403 FORBIDDEN" in {

        given.user.isNotAuthorised

        val response: WSResponse = postRequest(zeroRatedSuppliesModel)

        response should have(
          httpStatus(FORBIDDEN),
          pageTitle("You can’t use this service yet" + titleSuffixOther)
        )
      }
    }
  }

}