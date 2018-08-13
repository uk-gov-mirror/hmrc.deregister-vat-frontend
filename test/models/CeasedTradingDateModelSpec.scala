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

package models

import java.time.LocalDate

import utils.TestUtil

class CeasedTradingDateModelSpec extends TestUtil {

  "CeasedTradingModel.date" when {

    "given a valid date" should {

      "return Some(LocalDate)" in {
        CeasedTradingDateModel(1,1,2018).date shouldBe Some(LocalDate.of(2018,1,1))
      }
    }

    "given an invalid date" should {

      "return None" in {
        CeasedTradingDateModel(99,99,9999).date shouldBe None
      }
    }
  }
}