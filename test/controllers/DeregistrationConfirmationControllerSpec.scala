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

package controllers

import models.{DeregisterVatSuccess, ErrorModel}
import play.api.http.Status
import play.api.test.Helpers._
import services.mocks.{MockAuditService, MockContactPreferencesService, MockCustomerDetailsService, MockDeleteAllStoredAnswersService}
import assets.constants.CustomerDetailsTestConstants.{customerDetailsMax, customerDetailsUnverifiedEmail}
import assets.constants.ContactPreferencesTestConstants.{contactPreferencesDigital, contactPreferencesPaper}
import assets.constants.BaseTestConstants.vrn
import assets.messages.{DeregistrationConfirmationMessages => Messages}
import common.SessionKeys
import org.jsoup.Jsoup
import play.api.mvc.AnyContentAsEmpty
import play.api.test.FakeRequest
import views.html.DeregistrationConfirmation

import scala.concurrent.Future

class DeregistrationConfirmationControllerSpec extends ControllerBaseSpec with MockDeleteAllStoredAnswersService
  with MockCustomerDetailsService with MockContactPreferencesService with MockAuditService {

  lazy val deregistrationConfirmation: DeregistrationConfirmation = injector.instanceOf[DeregistrationConfirmation]

  object TestDeregistrationConfirmationController
    extends DeregistrationConfirmationController(
      deregistrationConfirmation,
      mcc,
      mockAuthPredicate,
      mockDeleteAllStoredAnswersService,
      serviceErrorHandler,
      mockCustomerDetailsService,
      mockAuditService,
      mockContactPreferencesService,
      ec,
      mockConfig)

 lazy val requestWithSession: FakeRequest[AnyContentAsEmpty.type] = request.withSession(SessionKeys.deregSuccessful -> "true")

  "the user is authorised" when {

    "Calling the .show action" when {

      "User has successful dereg session key" when {

        "answers are deleted successfully and a customerDetails is received" when {

          "contactPrefMigrationFeature is enabled" should {

            lazy val result = {
              mockConfig.features.contactPrefMigrationFeature(true)
              setupMockDeleteAllStoredAnswers(Right(DeregisterVatSuccess))
              mockAuthResult(Future.successful(mockAuthorisedIndividual))
              setupMockCustomerDetails(vrn)(Right(customerDetailsMax))
              TestDeregistrationConfirmationController.show()(requestWithSession)
            }

            "return 200 (OK)" in {
              status(result) shouldBe Status.OK
            }

            "return HTML" in {
              contentType(result) shouldBe Some("text/html")
              charset(result) shouldBe Some("utf-8")
            }
          }

          "contactPrefMigrationFeature is disabled" when {

            "emailVerifiedFeature is enabled" when {

              "Contact Preference is set to 'DIGITAL'" when {

                "the user has a verified email" should {
                  lazy val result = TestDeregistrationConfirmationController.show()(requestWithSession)

                  lazy val document = Jsoup.parse(bodyOf(result))

                  "return 200 (OK)" in {
                    mockConfig.features.contactPrefMigrationFeature(false)
                    mockConfig.features.emailVerifiedFeature(true)
                    setupMockDeleteAllStoredAnswers(Right(DeregisterVatSuccess))
                    mockAuthResult(Future.successful(mockAuthorisedIndividual))
                    setupMockContactPreferences(vrn)(Right(contactPreferencesDigital))
                    setupMockCustomerDetails(vrn)(Right(customerDetailsMax))
                    setupAuditExtendedEvent()

                    status(result) shouldBe Status.OK
                  }

                  "return HTML" in {
                    contentType(result) shouldBe Some("text/html")
                    charset(result) shouldBe Some("utf-8")
                  }

                  "return the correct first paragraph" in {
                    messages(document.getElementsByClass("govuk-body").first().text()) shouldBe Messages.emailPreference
                  }

                }

                "the user has an unverified email" should {
                  lazy val result = TestDeregistrationConfirmationController.show()(requestWithSession)

                  lazy val document = Jsoup.parse(bodyOf(result))

                  "return 200 (OK)" in {
                    mockConfig.features.contactPrefMigrationFeature(false)
                    mockConfig.features.emailVerifiedFeature(true)
                    setupMockDeleteAllStoredAnswers(Right(DeregisterVatSuccess))
                    mockAuthResult(Future.successful(mockAuthorisedIndividual))
                    setupMockContactPreferences(vrn)(Right(contactPreferencesDigital))
                    setupMockCustomerDetails(vrn)(Right(customerDetailsUnverifiedEmail))
                    setupAuditExtendedEvent()

                    status(result) shouldBe Status.OK
                  }

                  "return HTML" in {
                    contentType(result) shouldBe Some("text/html")
                    charset(result) shouldBe Some("utf-8")
                  }

                  "return the correct first paragraph" in {
                    messages(document.getElementsByClass("govuk-body").first().text()) shouldBe Messages.digitalPreference
                  }
                }
              }
            }
          }

          "emailVerifiedFeature is disabled" when {

            "Contact Preference is set to 'DIGITAL'" should {

              lazy val result = TestDeregistrationConfirmationController.show()(requestWithSession)

              lazy val document = Jsoup.parse(bodyOf(result))

              "return 200 (OK)" in {
                mockConfig.features.contactPrefMigrationFeature(false)
                mockConfig.features.emailVerifiedFeature(false)
                setupMockDeleteAllStoredAnswers(Right(DeregisterVatSuccess))
                mockAuthResult(Future.successful(mockAuthorisedIndividual))
                setupMockContactPreferences(vrn)(Right(contactPreferencesDigital))
                setupMockCustomerDetails(vrn)(Right(customerDetailsMax))
                setupAuditExtendedEvent()

                status(result) shouldBe Status.OK
              }

              "return HTML" in {
                contentType(result) shouldBe Some("text/html")
                charset(result) shouldBe Some("utf-8")
              }

              "return the correct first paragraph" in {
                messages(document.getElementsByClass("govuk-body").first().text()) shouldBe Messages.digitalPreference
              }

            }

          }

        }

        "answers are deleted successfully and an error is received for CustomerDetails call" when {

          "Contact Preference is set to 'PAPER'" should {

            lazy val result = TestDeregistrationConfirmationController.show()(requestWithSession)

            lazy val document = Jsoup.parse(bodyOf(result))

            "return 200 (OK)" in {
              setupMockDeleteAllStoredAnswers(Right(DeregisterVatSuccess))
              mockAuthResult(Future.successful(mockAuthorisedIndividual))
              setupMockContactPreferences(vrn)(Right(contactPreferencesPaper))
              setupMockCustomerDetails(vrn)(Left(ErrorModel(INTERNAL_SERVER_ERROR, "bad things")))
              setupAuditExtendedEvent()
              status(result) shouldBe Status.OK
            }

            "return HTML" in {
              contentType(result) shouldBe Some("text/html")
              charset(result) shouldBe Some("utf-8")
            }

            "return the correct first paragraph" in {
              messages(document.getElementsByClass("govuk-body").first().text()) shouldBe Messages.paperPreference
            }
          }

          "contact preferences returns an error" should {

            lazy val result = TestDeregistrationConfirmationController.show()(requestWithSession)

            lazy val document = Jsoup.parse(bodyOf(result))

            "return 200 (OK)" in {
              setupMockDeleteAllStoredAnswers(Right(DeregisterVatSuccess))
              mockAuthResult(Future.successful(mockAuthorisedIndividual))
              setupMockContactPreferences(vrn)(Left(ErrorModel(Status.INTERNAL_SERVER_ERROR, "I got nothing")))
              setupMockCustomerDetails(vrn)(Left(ErrorModel(INTERNAL_SERVER_ERROR, "bad things")))
              status(result) shouldBe Status.OK
            }

            "return HTML" in {
              contentType(result) shouldBe Some("text/html")
              charset(result) shouldBe Some("utf-8")
            }

            "return the correct first paragraph" in {
              messages(document.getElementsByClass("govuk-body").first().text()) shouldBe Messages.contactPrefError
            }
          }
        }

        "answers are not deleted successfully should return internal server error" in {
          lazy val result = TestDeregistrationConfirmationController.show()(requestWithSession)
          setupMockDeleteAllStoredAnswers(Left(ErrorModel(INTERNAL_SERVER_ERROR, "bad things")))
          mockAuthResult(Future.successful(mockAuthorisedIndividual))
          status(result) shouldBe Status.INTERNAL_SERVER_ERROR
        }
      }

      "user does not have session key" should {
        lazy val result = {
          mockAuthResult(Future.successful(mockAuthorisedIndividual))
          TestDeregistrationConfirmationController.show()(request)
        }

        "return 303" in {
          status(result) shouldBe Status.SEE_OTHER
        }

        "redirect to Deregister for VAT controller" in {
          redirectLocation(result) shouldBe Some(controllers.routes.DeregisterForVATController.redirect().url)
        }
      }
    }
  }
}
