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

import uk.gov.hmrc.DefaultBuildSettings._
import uk.gov.hmrc.SbtAutoBuildPlugin
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin._
import uk.gov.hmrc.versioning.SbtGitVersioning
import play.core.PlayVersion
import play.sbt.routes.RoutesKeys
import sbt.Tests.{Group, SubProcess}

val appName: String = "deregister-vat-frontend"

lazy val appDependencies: Seq[ModuleID] = compile ++ test()
lazy val plugins: Seq[Plugins] = Seq.empty
lazy val playSettings: Seq[Setting[_]] = Seq.empty

RoutesKeys.routesImport := Seq.empty

lazy val coverageSettings: Seq[Setting[_]] = {
  import scoverage.ScoverageKeys

  val excludedPackages = Seq(
    "<empty>",
    "Reverse.*",
    "controllers..*Reverse.*",
    "com.kenshoo.play.metrics.*",
    "controllers.zeroRated.javascript",
    "controllers.javascript",
    ".*standardError*.*",
    ".*govuk_wrapper*.*",
    ".*main_template*.*",
    "uk.gov.hmrc.BuildInfo",
    "app.*",
    "prod.*",
    "config.*",
    "testOnlyDoNotUseInAppConf.*",
    "testOnly.*",
    "views.*")

  Seq(
    ScoverageKeys.coverageExcludedPackages := excludedPackages.mkString(";"),
    ScoverageKeys.coverageMinimum := 95,
    ScoverageKeys.coverageFailOnMinimum := true,
    ScoverageKeys.coverageHighlighting := true
  )
}

val compile: Seq[ModuleID] = Seq(
  ws,
  "uk.gov.hmrc"   %% "bootstrap-frontend-play-26"      % "4.2.0",
  "uk.gov.hmrc"   %% "govuk-template"                  % "5.65.0-play-26",
  "uk.gov.hmrc"   %% "play-ui"                         % "9.1.0-play-26",
  "uk.gov.hmrc"   %% "play-partials"                   % "7.1.0-play-26",
  "uk.gov.hmrc"   %% "play-language"                   % "4.12.0-play-26",
  "uk.gov.hmrc"   %% "play-frontend-govuk"             % "0.69.0-play-26",
  "uk.gov.hmrc"   %% "play-frontend-hmrc"              % "0.57.0-play-26",
  "org.typelevel" %% "cats"                            % "0.9.0"
)

def test(scope: String = "test, it"): Seq[ModuleID] = Seq(
  "uk.gov.hmrc"             %% "hmrctest"                     % "3.10.0-play-26"    % scope,
  "org.scalatest"           %% "scalatest"                    % "3.0.9"             % scope,
  "org.pegdown"             %  "pegdown"                      % "1.6.0"             % scope,
  "org.jsoup"               %  "jsoup"                        % "1.13.1"            % scope,
  "com.typesafe.play"       %% "play-test"                    % PlayVersion.current % scope,
  "org.scalatestplus.play"  %% "scalatestplus-play"           % "3.1.3"             % scope,
  "org.scalamock"           %% "scalamock-scalatest-support"  % "3.6.0"             % scope,
  "com.github.tomakehurst"  %  "wiremock-jre8"                % "2.27.2"            % scope,
  "com.vladsch.flexmark"    %  "flexmark-all"                 % "0.62.2"            % scope
)

def oneForkedJvmPerTest(tests: Seq[TestDefinition]): Seq[Group] = tests map {
  test =>
    Group(test.name, Seq(test), SubProcess(
      ForkOptions().withRunJVMOptions(Vector("-Dtest.name=" + test.name, "-Dlogger.resource=logback-test.xml"))
    ))
}

TwirlKeys.templateImports ++= Seq(
  "uk.gov.hmrc.govukfrontend.views.html.components._",
  "uk.gov.hmrc.govukfrontend.views.html.helpers._",
  "uk.gov.hmrc.hmrcfrontend.views.html.components._",
  "uk.gov.hmrc.hmrcfrontend.views.html.helpers._"
)

lazy val microservice: Project = Project(appName, file("."))
  .enablePlugins(Seq(PlayScala, SbtAutoBuildPlugin, SbtGitVersioning, SbtDistributablesPlugin) ++ plugins: _*)
  .disablePlugins(JUnitXmlReportPlugin)
  .settings(PlayKeys.playDefaultPort := 9153)
  .settings(coverageSettings: _*)
  .settings(playSettings: _*)
  .settings(scalaSettings: _*)
  .settings(publishingSettings: _*)
  .settings(defaultSettings(): _*)
  .settings(
    majorVersion := 0,
    scalaVersion := "2.12.11",
    libraryDependencies ++= appDependencies,
    retrieveManaged := true,
    evictionWarningOptions in update := EvictionWarningOptions.default.withWarnScalaVersionEviction(false)
  )
  .configs(IntegrationTest)
  .settings(inConfig(IntegrationTest)(Defaults.itSettings): _*)
  .settings(
    Keys.fork in IntegrationTest := false,
    unmanagedSourceDirectories in IntegrationTest := (baseDirectory in IntegrationTest)(base => Seq(base / "it")).value,
    addTestReportOption(IntegrationTest, "int-test-reports"),
    testGrouping in IntegrationTest := oneForkedJvmPerTest((definedTests in IntegrationTest).value),
    parallelExecution in IntegrationTest := false)
  .settings(resolvers ++= Seq(
    Resolver.bintrayRepo("hmrc", "releases"),
    Resolver.jcenterRepo
  ))
