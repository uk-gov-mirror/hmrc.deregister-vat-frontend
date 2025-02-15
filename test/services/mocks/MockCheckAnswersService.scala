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

import models.{CheckYourAnswersModel, ErrorModel, User}
import org.scalamock.scalatest.MockFactory
import play.api.i18n.Messages
import services.CheckAnswersService
import uk.gov.hmrc.http.HeaderCarrier
import utils.TestUtil

import scala.concurrent.ExecutionContext

trait MockCheckAnswersService extends TestUtil with MockFactory {

  val mockCheckAnswersService: CheckAnswersService = mock[CheckAnswersService]

  def setupMockCheckYourAnswersModel(response: Either[ErrorModel, CheckYourAnswersModel])(implicit user: User[_]): Unit = {
    (mockCheckAnswersService.checkYourAnswersModel()(_: User[_], _: Messages, _: HeaderCarrier, _: ExecutionContext))
      .expects(user, *, *, *)
      .returns(response)
  }
}
