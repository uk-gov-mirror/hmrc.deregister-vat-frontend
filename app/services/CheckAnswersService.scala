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

package services

import cats.data.EitherT
import cats.instances.future._
import config.AppConfig
import javax.inject.{Inject, Singleton}
import models._
import play.api.i18n.Messages
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CheckAnswersService @Inject()(accountingMethodAnswerService: AccountingMethodAnswerService,
                                    capitalAssetsAnswerService: CapitalAssetsAnswerService,
                                    ceasedTradingDateAnswerService: CeasedTradingDateAnswerService,
                                    chooseDeregDateAnswerService: ChooseDeregDateAnswerService,
                                    deregDateAnswerService: DeregDateAnswerService,
                                    deregReasonAnswerService: DeregReasonAnswerService,
                                    nextTaxableTurnoverAnswerService: NextTaxableTurnoverAnswerService,
                                    optionTaxAnswerService: OptionTaxAnswerService,
                                    issueNewInvoicesAnswerService: IssueNewInvoicesAnswerService,
                                    stocksAnswerService: StocksAnswerService,
                                    taxableTurnoverAnswerService: TaxableTurnoverAnswerService,
                                    whyTurnoverBelowAnswerService: WhyTurnoverBelowAnswerService,
                                    outstandingInvoicesAnswerService: OutstandingInvoicesAnswerService,
                                    businessActivityAnswerService: BusinessActivityAnswerService,
                                    sicCodeAnswerService: SicCodeAnswerService,
                                    zeroRatedSuppliesValueService: ZeroRatedSuppliesValueService,
                                    purchasesExceedSuppliesAnswerService: PurchasesExceedSuppliesAnswerService,
                                    implicit val appConfig: AppConfig) {


  def checkYourAnswersModel()(implicit user: User[_], messages: Messages, hc: HeaderCarrier, ec: ExecutionContext)
  : Future[Either[ErrorModel, CheckYourAnswersModel]] = {
    val answers = for {
      deregReason <- EitherT(deregReasonAnswerService.getAnswer)
      ceasedDate <- EitherT(ceasedTradingDateAnswerService.getAnswer)
      turnover <- EitherT(taxableTurnoverAnswerService.getAnswer)
      nextTurnover <- EitherT(nextTaxableTurnoverAnswerService.getAnswer)
      whyBelow <- EitherT(whyTurnoverBelowAnswerService.getAnswer)
      accounting <- EitherT(accountingMethodAnswerService.getAnswer)
      optionTax <- EitherT(optionTaxAnswerService.getAnswer)
      capital <- EitherT(capitalAssetsAnswerService.getAnswer)
      stocks <- EitherT(stocksAnswerService.getAnswer)
      issueInvoice <- EitherT(issueNewInvoicesAnswerService.getAnswer)
      outstanding <- EitherT(outstandingInvoicesAnswerService.getAnswer)
      chooseDeregDate <- EitherT(chooseDeregDateAnswerService.getAnswer)
      deregDate <- EitherT(deregDateAnswerService.getAnswer)
      businessActivity <- EitherT(businessActivityAnswerService.getAnswer)
      sicCode <- EitherT(sicCodeAnswerService.getAnswer)
      zeroRatedSupplies <- EitherT(zeroRatedSuppliesValueService.getAnswer)
      purchasesExceedSupplies <- EitherT(purchasesExceedSuppliesAnswerService.getAnswer)
    } yield {
      CheckYourAnswersModel(
        deregReason,
        ceasedDate,
        turnover,
        nextTurnover,
        whyBelow,
        accounting,
        optionTax,
        capital,
        stocks,
        issueInvoice,
        outstanding,
        chooseDeregDate,
        deregDate,
        businessActivity,
        sicCode,
        zeroRatedSupplies,
        purchasesExceedSupplies
      )
    }
    answers.value
  }
}
