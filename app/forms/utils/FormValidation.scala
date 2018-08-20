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

package forms.utils

import java.time.LocalDate

import models.DateModel
import play.api.data.validation.{Constraint, Invalid, Valid}

import scala.util.{Failure, Success, Try}

trait FormValidation {

  val isNumeric: String => Boolean = amt => Try(BigDecimal(amt)) match {
    case Success(_) => true
    case _ => false
  }

  val hasMoreThanTwoDecimals: String => Boolean = amtAsString =>
    amtAsString.lastIndexOf(".") >= 0  && (amtAsString.length - amtAsString.lastIndexOf(".") - 1) > 2

  def isNumericConstraint(errMsg: String): Constraint[String] = Constraint("isNumeric"){
    amt => if (isNumeric(amt)) Valid else Invalid(errMsg)
  }

  def hasMaxTwoDecimalsConstraint(errMsg: String): Constraint[String] = Constraint("isNumeric") {
    amt => if(hasMoreThanTwoDecimals(amt)) Invalid(errMsg) else Valid
  }

  def isPositive(errMsg: String): Constraint[BigDecimal] = Constraint[BigDecimal]("isPositive") {
    amt => if(amt >= 0) Valid else Invalid(errMsg)
  }

  def doesNotExceed(max: BigDecimal, errMsg: String): Constraint[BigDecimal] = Constraint[BigDecimal]("isLessThanMax") {
    amt => if(amt <= max) Valid else Invalid(errMsg, max)
  }

  def isValidDay(errMsg: String): Constraint[Int] = Constraint[Int]("isValidDate") {
    day => if(1 to 31 contains day) Valid else Invalid(errMsg)
  }

  def isValidMonth(errMsg: String): Constraint[Int] = Constraint[Int]("isValidDate") {
    month => if(1 to 12 contains month) Valid else Invalid(errMsg)
  }

  def isValidYear(errMsg: String): Constraint[Int] = Constraint[Int]("isValidDate") {
    year => if(year.toString().length == 4) Valid else Invalid(errMsg)
  }

  def isValidDate(errMsg: String): Constraint[DateModel] = Constraint[DateModel]("isValidDate") {
    date =>
      Try(LocalDate.of(date.dateYear,date.dateMonth,date.dateDay)) match {
        case Success(_) => Valid
        case Failure(_) => Invalid(errMsg)
    }
  }
}
