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

import assets.constants.BaseTestConstants.errorModel
import forms.YesNoForm.yesNo
import models._
import play.api.http.Status
import play.api.mvc.AnyContentAsFormUrlEncoded
import play.api.test.FakeRequest
import play.api.test.Helpers.{contentType, _}
import services.mocks._

import scala.concurrent.Future

class OutstandingInvoicesControllerSpec extends ControllerBaseSpec {

  object TestOutstandingInvoicesController extends OutstandingInvoicesController(
    messagesApi,
    mockAuthPredicate,
    MockOutstandingInvoicesService.mockStoredAnswersService,
    MockDeregReasonAnswerService.mockStoredAnswersService,
    MockCapitalAssetsAnswerService.mockStoredAnswersService,
    mockConfig
  )

  "Calling .show" when {

    "user is authorised" when {

      "the user does not have a pre selected option" should {

        lazy val result = TestOutstandingInvoicesController.show()(request)

        "return 200 (OK)" in {
          mockAuthResult(Future.successful(mockAuthorisedIndividual))
          MockOutstandingInvoicesService.setupMockGetAnswers(Right(None))
          status(result) shouldBe Status.OK
        }

        "return HTML" in {
          contentType(result) shouldBe Some("text/html")
          charset(result) shouldBe Some("utf-8")
        }
      }

      "the user is has pre selected option" should {

        lazy val result = TestOutstandingInvoicesController.show()(request)

        "return 200 (OK)" in {
          mockAuthResult(Future.successful(mockAuthorisedIndividual))
          MockOutstandingInvoicesService.setupMockGetAnswers(Right(Some(Yes)))
          status(result) shouldBe Status.OK
        }

        "return HTML" in {
          contentType(result) shouldBe Some("text/html")
          charset(result) shouldBe Some("utf-8")
        }

        "has the Yes radio option checked" in {
          document(result).select(s"#$yesNo-yes").hasAttr("checked") shouldBe true
        }
      }

      authChecks(".show", TestOutstandingInvoicesController.show(), request)
    }
  }

  "Calling .submit" when {

    "user selects 'Yes'" should {

      lazy val request: FakeRequest[AnyContentAsFormUrlEncoded] = FakeRequest("POST", "/").withFormUrlEncodedBody((yesNo, "yes"))
      lazy val result = TestOutstandingInvoicesController.submit()(request)

      "return 303 (SEE OTHER)" in {
        MockOutstandingInvoicesService.setupMockStoreAnswers(Yes)(Right(DeregisterVatSuccess))
        mockAuthResult(Future.successful(mockAuthorisedIndividual))
        status(result) shouldBe Status.SEE_OTHER
      }

      s"redirect to ${controllers.routes.DeregistrationDateController.show()}" in {
        redirectLocation(result) shouldBe Some(controllers.routes.DeregistrationDateController.show().url)
      }
    }

    "user selects 'No'" when {

      "user is on 'below threshold' journey" should {

        lazy val request: FakeRequest[AnyContentAsFormUrlEncoded] = FakeRequest("POST", "/").withFormUrlEncodedBody((yesNo, "no"))
        lazy val result = TestOutstandingInvoicesController.submit()(request)

        MockDeregReasonAnswerService.setupMockGetAnswers(Right(Some(BelowThreshold)))

        "return 303 (SEE OTHER)" in {
          MockOutstandingInvoicesService.setupMockStoreAnswers(No)(Right(DeregisterVatSuccess))
          mockAuthResult(Future.successful(mockAuthorisedIndividual))
          status(result) shouldBe Status.SEE_OTHER
        }

        s"redirect to ${controllers.routes.DeregistrationDateController.show()}" in {
          redirectLocation(result) shouldBe Some(controllers.routes.DeregistrationDateController.show().url)
        }
      }

      "user is on 'ceased trading' journey" when {

        "user answered 'Yes' to having capital assets" should {

          lazy val request: FakeRequest[AnyContentAsFormUrlEncoded] = FakeRequest("POST", "/").withFormUrlEncodedBody((yesNo, "no"))
          lazy val result = TestOutstandingInvoicesController.submit()(request)

          val capitalAssetsAmount: Int = 1000

          MockDeregReasonAnswerService.setupMockGetAnswers(Right(Some(Ceased)))
          MockCapitalAssetsAnswerService.setupMockGetAnswers(Right(Some(YesNoAmountModel(Yes, Some(capitalAssetsAmount)))))

          "return 303 (SEE OTHER)" in {
            MockOutstandingInvoicesService.setupMockStoreAnswers(No)(Right(DeregisterVatSuccess))
            mockAuthResult(Future.successful(mockAuthorisedIndividual))
            status(result) shouldBe Status.SEE_OTHER
          }

          s"redirect to ${controllers.routes.DeregistrationDateController.show()}" in {
            redirectLocation(result) shouldBe Some(controllers.routes.DeregistrationDateController.show().url)
          }
        }

        "user answered 'No' to having capital assets" should {

          lazy val request: FakeRequest[AnyContentAsFormUrlEncoded] =
            FakeRequest("POST", "/").withFormUrlEncodedBody((yesNo, "no"))
          lazy val result = TestOutstandingInvoicesController.submit()(request)

          MockDeregReasonAnswerService.setupMockGetAnswers(Right(Some(Ceased)))
          MockCapitalAssetsAnswerService.setupMockGetAnswers(Right(Some(YesNoAmountModel(No, None))))

          "return 303 (SEE OTHER)" in {
            MockOutstandingInvoicesService.setupMockStoreAnswers(No)(Right(DeregisterVatSuccess))
            mockAuthResult(Future.successful(mockAuthorisedIndividual))
            status(result) shouldBe Status.SEE_OTHER
          }

          s"redirect to ${controllers.routes.CheckAnswersController.show()}" in {
            redirectLocation(result) shouldBe Some(controllers.routes.CheckAnswersController.show().url)
          }
        }
      }

      "no answer is returned for 'deregistration reason'" should {

        lazy val request: FakeRequest[AnyContentAsFormUrlEncoded] = FakeRequest("POST", "/").withFormUrlEncodedBody((yesNo, "no"))
        lazy val result = TestOutstandingInvoicesController.submit()(request)

        MockDeregReasonAnswerService.setupMockGetAnswers(Right(None))

        "return 500 (ISE)" in {
          MockOutstandingInvoicesService.setupMockStoreAnswers(No)(Right(DeregisterVatSuccess))
          mockAuthResult(Future.successful(mockAuthorisedIndividual))
          status(result) shouldBe Status.INTERNAL_SERVER_ERROR
        }
      }

      "an error is returned for 'deregistration reason'" should {

        lazy val request: FakeRequest[AnyContentAsFormUrlEncoded] = FakeRequest("POST", "/").withFormUrlEncodedBody((yesNo, "no"))
        lazy val result = TestOutstandingInvoicesController.submit()(request)

        MockDeregReasonAnswerService.setupMockGetAnswers(Left(errorModel))

        "return 500 (ISE)" in {
          MockOutstandingInvoicesService.setupMockStoreAnswers(No)(Right(DeregisterVatSuccess))
          mockAuthResult(Future.successful(mockAuthorisedIndividual))
          status(result) shouldBe Status.INTERNAL_SERVER_ERROR
        }
      }
    }

    "an error is returned when storing the answer" should {

      lazy val request: FakeRequest[AnyContentAsFormUrlEncoded] = FakeRequest("POST", "/").withFormUrlEncodedBody((yesNo, "no"))
      lazy val result = TestOutstandingInvoicesController.submit()(request)

      "return 500 (ISE)" in {
        MockOutstandingInvoicesService.setupMockStoreAnswers(No)(Left(errorModel))
        mockAuthResult(Future.successful(mockAuthorisedIndividual))
        status(result) shouldBe Status.INTERNAL_SERVER_ERROR
      }
    }

    "the user submits without selecting an option" should {

      lazy val request: FakeRequest[AnyContentAsFormUrlEncoded] = FakeRequest("POST", "/").withFormUrlEncodedBody(("yes_no", ""))
      lazy val result = TestOutstandingInvoicesController.submit()(request)

      "return 400 (BAD REQUEST)" in {
        mockAuthResult(Future.successful(mockAuthorisedIndividual))
        status(result) shouldBe Status.BAD_REQUEST
      }

      "return HTML" in {
        contentType(result) shouldBe Some("text/html")
        charset(result) shouldBe Some("utf-8")
      }
    }

    authChecks(".submit", TestOutstandingInvoicesController.submit(), FakeRequest("POST", "/").withFormUrlEncodedBody(("yes_no", "no")))
  }
}