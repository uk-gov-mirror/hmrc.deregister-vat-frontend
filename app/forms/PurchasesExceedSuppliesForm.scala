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

package forms

import models.{No, Yes, YesNo}
import play.api.data.{Form, FormError}
import play.api.data.Forms.{of, single}
import play.api.data.format.Formatter

object PurchasesExceedSuppliesForm {

  val yesNo: String = "yes_no"

  val yes: String = "yes"

  val no: String = "no"

  val purchasesExceedSupplyError: String = "purchasesExceedSupplies.error"

  val formatter: Formatter[YesNo] = new Formatter[YesNo] {
    override def bind(key: String, data: Map[String, String]): Either[Seq[FormError], YesNo] = {
      data.get(key) match {
        case Some(`yes`) => Right(Yes)
        case Some(`no`) => Right(No)
        case _ => Left(Seq(FormError(key, purchasesExceedSupplyError)))
      }
    }

    override def unbind(key: String, value: YesNo): Map[String, String] = {
      val stringValue = value match {
        case Yes => yes
        case No => no
      }

      Map(key -> stringValue)
    }
  }

  val purchasesExceedSuppliesForm: Form[YesNo] = Form(
    single(
      yesNo -> of(formatter)
    )
  )

}
