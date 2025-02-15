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

package services.mocks

import models.{DeregisterVatResponse, ErrorModel, User}
import org.scalamock.scalatest.MockFactory
import play.api.libs.json.Format
import services.StoredAnswersService
import uk.gov.hmrc.http.HeaderCarrier
import utils.TestUtil

import scala.concurrent.ExecutionContext

trait MockStoredAnswersService extends TestUtil with MockFactory {

  def setupMockGetAnswers[T](mockStoredAnswersService: StoredAnswersService[T])
                         (response: Either[ErrorModel, Option[T]])
                         (implicit user: User[_]): Unit = {
    (mockStoredAnswersService.getAnswer(_: User[_], _: Format[T], _: HeaderCarrier, _: ExecutionContext))
      .expects(user, *, *, *)
      .returns(response)
  }

  def setupMockStoreAnswers[T](mockStoredAnswersService: StoredAnswersService[T])
                           (data: T)
                           (response: Either[ErrorModel, DeregisterVatResponse])
                           (implicit user: User[_]): Unit = {
    (mockStoredAnswersService.storeAnswer(_: T)(_: User[_], _: Format[T], _: HeaderCarrier, _: ExecutionContext))
      .expects(data, user, *, *, *)
      .returns(response)
  }

  def setupMockDeleteAnswer[T](mockStoredAnswersService: StoredAnswersService[T])
                           (response: Either[ErrorModel, DeregisterVatResponse])
                           (implicit user: User[_]): Unit = {
    (mockStoredAnswersService.deleteAnswer(_: User[_], _: HeaderCarrier, _: ExecutionContext))
      .expects(user, *, *)
      .returns(response)
  }

  def setupMockDeleteAnswerNotCalled[T](mockStoredAnswersService: StoredAnswersService[T])(implicit user: User[_]): Unit = {
    (mockStoredAnswersService.deleteAnswer(_: User[_], _: HeaderCarrier, _: ExecutionContext))
      .expects(user, *, *)
      .never()
  }
}
