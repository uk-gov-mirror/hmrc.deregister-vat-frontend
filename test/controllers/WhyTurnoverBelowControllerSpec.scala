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

package controllers

import models.WhyTurnoverBelowModel
import play.api.http.Status
import play.api.mvc.AnyContentAsFormUrlEncoded
import play.api.test.FakeRequest
import play.api.test.Helpers.{contentType, _}

import scala.concurrent.Future

class WhyTurnoverBelowControllerSpec extends ControllerBaseSpec {

  object TestWhyTurnoverBelowController extends WhyTurnoverBelowController(messagesApi, mockAuthPredicate, mockConfig)

  "the user is authorised" when {

    "Calling the .show action" when {

      "the user does not have any checkboxes pre selected" should {

        lazy val result = TestWhyTurnoverBelowController.show()(request)

        "return 200 (OK)" in {
          mockAuthResult(Future.successful(mockAuthorisedIndividual))
          status(result) shouldBe Status.OK
        }

        "return HTML" in {
          contentType(result) shouldBe Some("text/html")
          charset(result) shouldBe Some("utf-8")
        }
      }

      //TODO: These tests needs updating when we actually retrieve stored values from Mongo
      "the user is has checkboxes pre-selected" should {

        lazy val result = TestWhyTurnoverBelowController.show()(request)

        "return 200 (OK)" in {
          mockAuthResult(Future.successful(mockAuthorisedIndividual))
          status(result) shouldBe Status.OK
        }

        "return HTML" in {
          contentType(result) shouldBe Some("text/html")
          charset(result) shouldBe Some("utf-8")
        }
      }

      authChecks(".show", TestWhyTurnoverBelowController.show(), request)

    }

    "Calling the .submit action" when {

      "the user submits after selecting at least one checkbox" should {

        lazy val request: FakeRequest[AnyContentAsFormUrlEncoded] =
          FakeRequest("POST", "/").withFormUrlEncodedBody((WhyTurnoverBelowModel.lostContract, "true"))
        lazy val result = TestWhyTurnoverBelowController.submit()(request)

        "return 303 (SEE OTHER)" in {
          mockAuthResult(Future.successful(mockAuthorisedIndividual))
          status(result) shouldBe Status.SEE_OTHER
        }

        //TODO: This test needs updating as part of the routing story
        s"redirect to ${controllers.routes.HelloWorldController.helloWorld().url}" in {
          redirectLocation(result) shouldBe Some(controllers.routes.HelloWorldController.helloWorld().url)
        }
      }

      "the user submits without selecting an option" should {

        lazy val request: FakeRequest[AnyContentAsFormUrlEncoded] =
          FakeRequest("POST", "/").withFormUrlEncodedBody(("", ""))
        lazy val result = TestWhyTurnoverBelowController.submit()(request)

        "return 400 (BAD REQUEST)" in {
          mockAuthResult(Future.successful(mockAuthorisedIndividual))
          status(result) shouldBe Status.BAD_REQUEST
        }

        "return HTML" in {
          contentType(result) shouldBe Some("text/html")
          charset(result) shouldBe Some("utf-8")
        }
      }
    }

    authChecks(".submit", TestWhyTurnoverBelowController.submit(), FakeRequest("POST", "/")
      .withFormUrlEncodedBody((WhyTurnoverBelowModel.lostContract, "true")))
  }
}
