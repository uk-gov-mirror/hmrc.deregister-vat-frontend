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

package services.mocks

import models._
import org.scalamock.scalatest.MockFactory
import play.api.libs.json.Format
import services.TaxableTurnoverAnswerService
import uk.gov.hmrc.http.HeaderCarrier
import utils.TestUtil

import scala.concurrent.ExecutionContext

trait MockTaxableTurnoverAnswerService extends TestUtil with MockFactory {

  val mockTaxableTurnoverAnswerService: TaxableTurnoverAnswerService = mock[TaxableTurnoverAnswerService]

  def setupMockGetTaxableTurnover(response: Either[ErrorModel, Option[TaxableTurnoverModel]])(implicit user: User[_]): Unit =
    (mockTaxableTurnoverAnswerService.getAnswer(_: User[_], _: Format[TaxableTurnoverModel], _: HeaderCarrier, _: ExecutionContext))
      .expects(user, *, *, *)
      .returns(response)

  def setupMockStoreTaxableTurnover(data: TaxableTurnoverModel)(response: Either[ErrorModel, DeregisterVatResponse])(implicit user: User[_]): Unit =
    (mockTaxableTurnoverAnswerService.storeAnswer(_: TaxableTurnoverModel)(_: User[_], _: Format[TaxableTurnoverModel], _: HeaderCarrier, _: ExecutionContext))
      .expects(data, user, *, *, *)
      .returns(response)

  def setupMockDeleteTaxableTurnover(response: Either[ErrorModel, DeregisterVatResponse])(implicit user: User[_]): Unit =
    (mockTaxableTurnoverAnswerService.deleteAnswer(_: User[_], _: HeaderCarrier, _: ExecutionContext))
      .expects(user, *, *)
      .returns(response)

  def setupMockDeleteTaxableTurnoverNotCalled()(implicit user: User[_]): Unit =
    (mockTaxableTurnoverAnswerService.deleteAnswer(_: User[_], _: HeaderCarrier, _: ExecutionContext))
      .expects(user, *, *)
      .never()

}
