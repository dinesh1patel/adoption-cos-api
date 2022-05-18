package uk.gov.hmcts.reform.adoption.adoptioncase.bulkscan.ccd.event;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.ccd.sdk.ConfigBuilderImpl;
import uk.gov.hmcts.ccd.sdk.api.Event;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.UserRole;
import uk.gov.hmcts.reform.adoption.bulkscan.ccd.ExceptionRecordState;
import uk.gov.hmcts.reform.adoption.bulkscan.ccd.event.CompleteAwaitingPaymentDcnProcessing;
import uk.gov.hmcts.reform.adoption.bulkscan.data.ExceptionRecord;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.adoption.bulkscan.ccd.event.CompleteAwaitingPaymentDcnProcessing.COMPLETE_AWAITING_PAYMENT_DCNPROCESSING;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.createExceptionRecordConfigBuilder;
import static uk.gov.hmcts.reform.adoption.testutil.TestDataHelper.getEventsFrom;


@ExtendWith(MockitoExtension.class)
class CompleteAwaitingPaymentDcnProcessingTest {

    @InjectMocks
    private CompleteAwaitingPaymentDcnProcessing completeAwaitingPaymentDcnProcessing;

    @Test
    void shouldAddConfigurationToConfigBuilder() {
        final ConfigBuilderImpl<ExceptionRecord, ExceptionRecordState, UserRole> configBuilder = createExceptionRecordConfigBuilder();

        completeAwaitingPaymentDcnProcessing.configure(configBuilder);

        assertThat(getEventsFrom(configBuilder).values())
            .extracting(Event::getId)
            .contains(COMPLETE_AWAITING_PAYMENT_DCNPROCESSING);
    }
}