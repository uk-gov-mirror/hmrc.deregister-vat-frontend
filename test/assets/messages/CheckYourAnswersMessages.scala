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

package assets.messages

object CheckYourAnswersMessages extends BaseMessages {

  val title = "Check your answers" + titleSuffix
  val heading = "Check your answers"
  val change = "Change"
  val reason = "Reason for cancelling VAT registration"
  val reasonCeased = "The business has stopped trading"
  val reasonBelowThreshold: String => String = threshold => s"Annual turnover is below £$threshold"
  val reasonOther = "Other"
  val ceasedTrading = "Date the business stopped or will stop trading"
  val taxableTurnover: String => String = threshold => s"Taxable turnover below £$threshold in last 12 months"
  val nextTaxableTurnover = "Expected taxable turnover for next 12 months"
  val whyBelow = "Why is turnover expected to be below threshold?"
  val vatAccounts = "How VAT accounts are prepared"
  val optionTax = "VAT charges on land or commercial property"
  val optionTaxValue = "Value of land or commercial property"
  val stocks = "Keeping stock"
  val stocksValue = "Value of stock"
  val capitalAssets = "Keeping capital assets"
  val capitalAssetsValue = "Value of capital assets"
  val outstandingInvoices = "Payment for invoices after cancelling registration"
  val newInvoices = "New invoices after cancelling registration"
  val deregistrationDate = "Choose cancellation date"

  val reasonHidden = "Change the reason why the business is cancelling its VAT registration"
  val ceasedTradingHidden = "Change the date your business stopped or will stop trading"
  val taxableTurnoverHidden = "Change whether last year's turnover was above or below the VAT threshold"
  val nextTaxableTurnoverHidden = "Change the business's expected turnover for the next 12 months"
  val whyBelowHidden = "Change the reason why your expect the business's turnover to be below the threshold"
  val vatAccountsHidden = "The accounting method the business uses to record VAT payments"
  val optionTaxHidden = "Change whether the business charged or claimed VAT on land or commercial property"
  val optionTaxValueHidden = "Change the value of the VAT charged or claimed on land or commercial property"
  val stocksHidden = "Change whether the business will keep any stock"
  val stocksValueHidden = "Change the value of the stock the business will keep"
  val capitalAssetsHidden = "Change whether the business will keep any capital assets"
  val capitalAssetsValueHidden = "Change the value of the capital assets the business will keep"
  val newInvoicesHidden = "Change whether the business will issue new invoices after cancelling VAT registration"
  val outstandingInvoiceHidden = "Change whether the business will receive payment for outstanding invoices after cancelling VAT registration"
  val deregistrationDateHidden = "Change whether the business wants to choose its own VAT cancellation date"

  val noDate = "Not Specified"
  val standard = "Standard accounting"
  val cash = "Cash accounting"
  val confirm = "Confirm and cancel VAT registration"
}
