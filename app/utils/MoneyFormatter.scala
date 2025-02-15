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

package utils

import java.text.DecimalFormat

import play.twirl.api.Html

object MoneyFormatter {

  private val formatter = new DecimalFormat("#,##0.00")
  private val removeZeroDecimals: String => String = _.replace(".00","")
  val formatHtmlAmount: BigDecimal => Html = x => Html(s"&pound;${removeZeroDecimals(formatter.format(x))}")
  val formatStringAmount: Int => String = x => s"${removeZeroDecimals(formatter.format(x))}"

}
