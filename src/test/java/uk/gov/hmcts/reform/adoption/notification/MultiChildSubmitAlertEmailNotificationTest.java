package uk.gov.hmcts.reform.adoption.notification;

import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.Applicant;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.common.config.EmailTemplatesConfig;
import uk.gov.hmcts.reform.adoption.idam.IdamService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.LanguagePreference.ENGLISH;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.NO;
import static uk.gov.hmcts.reform.adoption.document.DocumentConstants.YES;
import static uk.gov.hmcts.reform.adoption.notification.EmailTemplateName.MULTI_CHILD_SUBMIT_APPLICATION_EMAIL_ALERT;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.APPLICANT_1_FULL_NAME;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.APPLICANT_2_FULL_NAME;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.HAS_MULTIPLE_APPLICANT;
import static uk.gov.hmcts.reform.adoption.notification.NotificationConstants.HAS_SECOND_APPLICANT;
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.TEST_USER_EMAIL;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;

@ExtendWith(MockitoExtension.class)
class MultiChildSubmitAlertEmailNotificationTest {


    @Mock
    IdamService idamService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private CommonContent commonContent;

    @Mock
    private EmailTemplatesConfig emailTemplatesConfig;

    @InjectMocks
    private MultiChildSubmitAlertEmailNotification multiChildSubmitAlertEmailNotification;


    @Test
    void draftApplicationExpiringNotificationTest_sendToApplicants() {

        CaseData caseData = caseData();
        caseData.setDueDate(LocalDate.of(2021, 4, 21));
        caseData.setFamilyCourtName(StringUtils.EMPTY);
        Map<String, Object> templateVars = new HashMap<>();
        templateVars.put(APPLICANT_1_FULL_NAME, caseData.getApplicant1().getFirstName() + " "
            + caseData.getApplicant1().getLastName());
        if (caseData.getApplicant2() != null) {
            templateVars.put(
                APPLICANT_2_FULL_NAME,
                caseData.getApplicant2().getFirstName() + " " + caseData.getApplicant2().getLastName()
            );
            templateVars.put(HAS_SECOND_APPLICANT, YES);
        } else {
            templateVars.put(HAS_SECOND_APPLICANT, NO);
            templateVars.put(APPLICANT_2_FULL_NAME, StringUtils.EMPTY);
        }
        templateVars.put(HAS_MULTIPLE_APPLICANT, YES);

        multiChildSubmitAlertEmailNotification.sendToApplicants(caseData, 1234567890123456L);

        verify(notificationService, times(2)).sendEmail(
            eq(TEST_USER_EMAIL),
            eq(MULTI_CHILD_SUBMIT_APPLICATION_EMAIL_ALERT),
            eq(templateVars),
            eq(ENGLISH)
        );
    }

    @Test
    void draftApplicationExpiringNotificationTest_sendToApplicants_scenario2() {

        CaseData caseData = caseData();
        caseData.setDueDate(LocalDate.of(2021, 4, 21));
        caseData.setFamilyCourtName(StringUtils.EMPTY);
        caseData.setApplicant2(new Applicant());
        Map<String, Object> templateVars = new HashMap<>();
        templateVars.put(APPLICANT_1_FULL_NAME, caseData.getApplicant1().getFirstName() + " "
            + caseData.getApplicant1().getLastName());

        templateVars.put(HAS_SECOND_APPLICANT, NO);
        templateVars.put(HAS_MULTIPLE_APPLICANT, NO);
        templateVars.put(APPLICANT_2_FULL_NAME, StringUtils.EMPTY);
        multiChildSubmitAlertEmailNotification.sendToApplicants(caseData, 1234567890123456L);

        verify(notificationService, times(1)).sendEmail(
            eq(TEST_USER_EMAIL),
            eq(MULTI_CHILD_SUBMIT_APPLICATION_EMAIL_ALERT),
            eq(templateVars),
            eq(ENGLISH)
        );
    }
}