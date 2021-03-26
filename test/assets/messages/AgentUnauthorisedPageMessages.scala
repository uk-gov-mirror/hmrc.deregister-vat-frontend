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

package assets.messages

object AgentUnauthorisedPageMessages extends BaseMessages {

  val title: String = "You can’t use this service yet" + titleSuffixOther
  val agentTitle: String = "You can’t use this service yet" + titleSuffixAgent
  val pageHeading = "You can’t use this service yet"
  val instructions = "To use this service, you need to set up an agent services account."
  val clientInstructions = "You need to sign up to use software to submit your VAT Returns."
}
