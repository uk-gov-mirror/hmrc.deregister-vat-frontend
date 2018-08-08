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

import play.api.http.Status
import play.api.mvc.{AnyContentAsFormUrlEncoded, Result}
import play.api.test.FakeRequest
import play.api.test.Helpers.{contentType, _}
import services.EnrolmentsAuthService
import uk.gov.hmrc.auth.core._
import uk.gov.hmrc.auth.core.authorise.Predicate
import uk.gov.hmrc.auth.core.retrieve.Retrieval
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

class DeregistrationReasonControllerSpec extends ControllerBaseSpec {

  object TestDeregistrationReasonController extends DeregistrationReasonController(messagesApi, mockAuthorisedFunctions, mockConfig)

  "the user is authorised" when {

    "Calling the .show action" when {

      "the user does not have a pre selected option" should {

        lazy val result = TestDeregistrationReasonController.show()(request)

        "return 200 (OK)" in {
          mockAuthResult(Future.successful(individualAuthorised))
          status(result) shouldBe Status.OK
        }

        "return HTML" in {
          contentType(result) shouldBe Some("text/html")
          charset(result) shouldBe Some("utf-8")
        }
      }

      "the user is has pre selected option" should {

        lazy val result: Future[Result] = TestDeregistrationReasonController.show()(request)

        "return 200 (OK)" in {
          mockAuthResult(Future.successful(individualAuthorised))
          status(result) shouldBe Status.OK
        }

        "return HTML" in {
          contentType(result) shouldBe Some("text/html")
          charset(result) shouldBe Some("utf-8")
        }
      }

      authChecks(".show", TestDeregistrationReasonController.show(), request)

    }

    "Calling the .submit action" when {

      "the user submits after selecting an 'stoppedTrading' option" should {

        lazy val request: FakeRequest[AnyContentAsFormUrlEncoded] =
          FakeRequest("POST", "/").withFormUrlEncodedBody(("reason", "stoppedTrading"))
        lazy val result = TestDeregistrationReasonController.submit()(request)

        "return 303 (SEE OTHER)" in {
          mockAuthResult(Future.successful(individualAuthorised))
          status(result) shouldBe Status.SEE_OTHER
        }

        //TODO: This test needs updating as part of the routing story
        s"redirect to ${controllers.routes.HelloWorldController.helloWorld().url}" in {
          redirectLocation(result) shouldBe Some(controllers.routes.HelloWorldController.helloWorld().url)
        }
      }

      "the user submits after selecting the 'turnoverBelowThreshold' option" should {

        lazy val request: FakeRequest[AnyContentAsFormUrlEncoded] =
          FakeRequest("POST", "/").withFormUrlEncodedBody(("reason", "turnoverBelowThreshold"))
        lazy val result = TestDeregistrationReasonController.submit()(request)


        "return 303 (SEE OTHER)" in {
          mockAuthResult(Future.successful(individualAuthorised))
          status(result) shouldBe Status.SEE_OTHER
        }

        //TODO: This test needs updating as part of the routing story
        s"redirect to ${controllers.routes.HelloWorldController.helloWorld().url}" in {
          redirectLocation(result) shouldBe Some(controllers.routes.HelloWorldController.helloWorld().url)
        }
      }

      "the user submits after selecting the 'other' option" should {

        lazy val request: FakeRequest[AnyContentAsFormUrlEncoded] =
          FakeRequest("POST", "/").withFormUrlEncodedBody(("reason", "other"))
        lazy val result = TestDeregistrationReasonController.submit()(request)

        "return 303 (SEE OTHER)" in {
          mockAuthResult(Future.successful(individualAuthorised))
          status(result) shouldBe Status.SEE_OTHER
        }

        s"redirect to ${mockConfig.govUkCancelVatRegistration}" in {
          redirectLocation(result) shouldBe Some(mockConfig.govUkCancelVatRegistration)
        }
      }

      "the user submits without selecting an option" should {

        lazy val request: FakeRequest[AnyContentAsFormUrlEncoded] =
          FakeRequest("POST", "/").withFormUrlEncodedBody(("reason", ""))
        lazy val result = TestDeregistrationReasonController.submit()(request)

        "return 400 (BAD REQUEST)" in {
          mockAuthResult(Future.successful(individualAuthorised))
          status(result) shouldBe Status.BAD_REQUEST
        }

        "return HTML" in {
          contentType(result) shouldBe Some("text/html")
          charset(result) shouldBe Some("utf-8")
        }
      }
    }

    authChecks(".submit", TestDeregistrationReasonController.submit(), FakeRequest("POST", "/").withFormUrlEncodedBody(("reason", "other")))
  }
}
