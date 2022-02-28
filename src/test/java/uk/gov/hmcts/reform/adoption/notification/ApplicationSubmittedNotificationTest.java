package uk.gov.hmcts.reform.adoption.notification;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;

import java.time.LocalDate;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;
import static uk.gov.hmcts.reform.adoption.adoptioncase.model.LanguagePreference.ENGLISH;
import static uk.gov.hmcts.reform.adoption.notification.CommonContent.APPLICATION_REFERENCE;
import static uk.gov.hmcts.reform.adoption.notification.CommonContent.SUBMISSION_RESPONSE_DATE;
import static uk.gov.hmcts.reform.adoption.notification.EmailTemplateName.APPLICATION_SUBMITTED;
import static uk.gov.hmcts.reform.adoption.testutil.TestConstants.TEST_USER_EMAIL;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.caseData;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.getMainTemplateVars;

@ExtendWith(MockitoExtension.class)
class ApplicationSubmittedNotificationTest {

    @Mock
    private NotificationService notificationService;

    @Mock
    private CommonContent commonContent;

    @InjectMocks
    private ApplicationSubmittedNotification notification;

    @Test
    void shouldSendEmailToApplicant1WithSubmissionResponseDate() {
        CaseData data = caseData();
        data.setDueDate(LocalDate.of(2021, 4, 21));
        when(commonContent.mainTemplateVars(data, 1234567890123456L, data.getApplicant1(), data.getApplicant2()))
            .thenReturn(getMainTemplateVars());

        notification.sendToApplicant1(data, 1234567890123456L);

        verify(notificationService).sendEmail(
            eq(TEST_USER_EMAIL),
            eq(APPLICATION_SUBMITTED),
            argThat(allOf(
                hasEntry(SUBMISSION_RESPONSE_DATE, "21 April 2021"),
                hasEntry(APPLICATION_REFERENCE, "1234-5678-9012-3456")
            )),
            eq(ENGLISH)
        );
        verify(commonContent).mainTemplateVars(data, 1234567890123456L, data.getApplicant1(), data.getApplicant2());
    }

    @Test
    void shouldSendEmailToCaseWorkerWithSubmissionResponseDate() {
        CaseData data = caseData();
        data.setDueDate(LocalDate.of(2021, 4, 21));
        when(commonContent.mainTemplateVars(data, 1234567890123456L, data.getApplicant1(), data.getApplicant2()))
            .thenReturn(getMainTemplateVars());

        notification.sendToCaseWorker(data, 1234567890123456L);

        verify(notificationService).sendEmail(
            eq(TEST_USER_EMAIL),
            eq(APPLICATION_SUBMITTED),
            argThat(allOf(
                hasEntry(SUBMISSION_RESPONSE_DATE, "21 April 2021"),
                hasEntry(APPLICATION_REFERENCE, "1234-5678-9012-3456")
            )),
            eq(ENGLISH)
        );
        verify(commonContent).mainTemplateVars(data, 1234567890123456L, data.getApplicant1(), data.getApplicant2());
    }

    @Test
    void shouldSendEmailToLocalAuthorityWithSubmissionResponseDate() {
        CaseData data = caseData();
        data.setDueDate(LocalDate.of(2021, 4, 21));
        when(commonContent.mainTemplateVars(data, 1234567890123456L, data.getApplicant1(), data.getApplicant2()))
            .thenReturn(getMainTemplateVars());

        notification.sendToLocalAuthority(data, 1234567890123456L);

        verify(notificationService).sendEmail(
            eq(TEST_USER_EMAIL),
            eq(APPLICATION_SUBMITTED),
            argThat(allOf(
                hasEntry(SUBMISSION_RESPONSE_DATE, "21 April 2021"),
                hasEntry(APPLICATION_REFERENCE, "1234-5678-9012-3456")
            )),
            eq(ENGLISH)
        );
        verify(commonContent).mainTemplateVars(data, 1234567890123456L, data.getApplicant1(), data.getApplicant2());
    }

}