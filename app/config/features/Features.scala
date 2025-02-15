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

package config.features

import javax.inject.{Inject, Singleton}

import config.ConfigKeys
import play.api.Configuration

@Singleton
class Features @Inject()(implicit config: Configuration) {
  val stubAgentClientLookup = new Feature(ConfigKeys.stubAgentClientLookupFeature)
  val stubContactPreferences = new Feature(ConfigKeys.stubContactPreferencesFeature)
  val useLanguageSelector = new Feature(ConfigKeys.useLanguageSelectorFeature)
  val accessibilityStatement = new Feature(ConfigKeys.accessibilityStatement)
  val zeroRatedJourney = new Feature(ConfigKeys.zeroRatedJourney)
  val emailVerifiedFeature = new Feature(ConfigKeys.emailVerifiedFeature)
  val bulkPaperOffFeature = new Feature(ConfigKeys.bulkPaperOffFeature)
  val contactPrefMigrationFeature = new Feature(ConfigKeys.contactPrefMigrationFeature)
}
