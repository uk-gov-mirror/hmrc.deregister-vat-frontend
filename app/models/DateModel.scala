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

package models

import java.time.LocalDate
import java.time.format.{DateTimeFormatter, ResolverStyle}

import play.api.i18n.Messages
import play.api.libs.json.{Json, OFormat}

import scala.util.{Failure, Success, Try}


case class DateModel(dateDay: Int, dateMonth: Int, dateYear: Int) {

  val date: Option[LocalDate] =
    Try(LocalDate.of(dateYear,dateMonth,dateDay)) match {
      case Success(date) => Some(date)
      case Failure(_) => None
    }

  def longDate()(implicit messages: Messages): String = {
    date.fold(""){ datum => datum.format(DateTimeFormatter.ofPattern(
        if(messages.lang.language =="cy"){
          s"""d '${messages(s"month.${datum.getMonthValue}")}' uuuu"""
        } else {
          "d MMMM uuuu"
        }
    ).withResolverStyle(ResolverStyle.STRICT))}
  }
}

object DateModel {
  implicit val format: OFormat[DateModel] = Json.format[DateModel]
}