/*
 * Copyright 2017 HM Revenue & Customs
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
import play.api.test.Helpers._
import services.EnrolmentsAuthService
import uk.gov.hmrc.auth.core._
import uk.gov.hmrc.auth.core.authorise.Predicate
import uk.gov.hmrc.auth.core.retrieve.Retrieval
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

class HelloWorldControllerSpec extends ControllerBaseSpec {

  private trait Test {
    val failure: Option[AuthorisationException] = None
    val enrolments: Option[Enrolments] = None
    val mockAuthConnector: AuthConnector = mock[AuthConnector]

    private def setup() {
      val result = (enrolments, failure) match {
        case (None, Some(ex)) => Future.failed(ex)
        case (Some(services), None) => Future.successful(services)
        case _ => fail("Invalid test scenario")
      }

      (mockAuthConnector.authorise(_: Predicate, _: Retrieval[Enrolments])(_: HeaderCarrier, _: ExecutionContext))
        .expects(*, *, *, *)
        .returns(result)
    }

    val mockAuthorisedFunctions: AuthorisedFunctions = new EnrolmentsAuthService(mockAuthConnector)

    def target: HelloWorldController = {
      setup()
      new HelloWorldController(messages, mockAuthorisedFunctions, mockConfig)
    }
  }

  "Calling the .helloWorld action" when {

    "the user is authorised" should {

      val goodEnrolments: Enrolments = Enrolments(
        Set(
          Enrolment(
            "HMRC-MTD-VAT",
            Seq(EnrolmentIdentifier("", "VRN1234567890")),
            "Active")
        )
      )

      "return 200" in new Test {
        override val enrolments = Some(goodEnrolments)
        private val result = target.helloWorld()(fakeRequest)

        status(result) shouldBe Status.OK
      }

      "return HTML" in new Test {
        override val enrolments = Some(goodEnrolments)
        private val result = target.helloWorld()(fakeRequest)

        contentType(result) shouldBe Some("text/html")
        charset(result) shouldBe Some("utf-8")
      }
    }

    "the user is not authenticated" should {

      "return 303" in new Test {
        override val failure = Some(MissingBearerToken())
        private val result = target.helloWorld()(fakeRequest)

        status(result) shouldBe Status.UNAUTHORIZED
      }
    }

    "the user is not authorised" should {

      "return 303" in new Test {
        override val failure = Some(InsufficientEnrolments())
        private val result = target.helloWorld()(fakeRequest)

        status(result) shouldBe Status.FORBIDDEN
      }
    }

  }
}