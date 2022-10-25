package uk.gov.hmcts.reform.adoption.common.service.task;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.type.ListValue;
import uk.gov.hmcts.ccd.sdk.type.YesOrNo;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.PlacementOrder;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.PlacementOrderType;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.task.CaseTask;

import java.util.List;
import java.util.stream.Collectors;

import static uk.gov.hmcts.reform.adoption.adoptioncase.model.State.LaSubmitted;
import static uk.gov.hmcts.reform.adoption.document.DocumentType.APPLICATION_LA_SUMMARY_EN;


@Component
@Slf4j
public class SetStateAfterLaSubmission implements CaseTask {

    @Override
    public CaseDetails<CaseData, State> apply(final CaseDetails<CaseData, State> caseDetails) {
        caseDetails.setState(LaSubmitted);
        caseDetails.getData().setStatus(LaSubmitted);
        caseDetails.getData().getBirthMother()
            .setDeceased(caseDetails.getData().getBirthMother().getStillAlive()
                             .equals(YesOrNo.YES) ? YesOrNo.NO : YesOrNo.YES);
        if (caseDetails.getData().getBirthFather() != null && caseDetails.getData().getBirthFather().getStillAlive() != null) {
            caseDetails.getData().getBirthFather()
                .setDeceased(caseDetails.getData().getBirthFather().getStillAlive()
                                 .equals(YesOrNo.YES) ? YesOrNo.NO : YesOrNo.YES);
        }
        List<ListValue<PlacementOrder>> placementList = caseDetails.getData().getPlacementOrders();
        placementList.stream()
            .filter(item -> item.getValue().getPlacementOrderType() == null)
            .findFirst()
            .ifPresent(item -> {
                caseDetails.getData().setPlacementOrder(PlacementOrder
                    .builder()
                    .placementOrderId(item.getValue().getPlacementOrderId())
                    .placementOrderNumber(item.getValue().getPlacementOrderNumber())
                    .placementOrderType(PlacementOrderType.PLACEMENT_ORDER)
                    .placementOrderCourt(item.getValue().getPlacementOrderCourt())
                    .placementOrderDate(item.getValue().getPlacementOrderDate())
                    .otherPlacementOrderType(item.getValue().getOtherPlacementOrderType())
                    .build());
                caseDetails.getData().setPlacementOrders(placementList.stream().filter(el -> !el.equals(item)).collect(
                    Collectors.toList()));
            });

        caseDetails.getData().getLaDocumentsUploaded().stream()
            .forEach(laUploadedDocument -> {
                if (!StringUtils.isEmpty(caseDetails.getData().getLaSotFullName())) {
                    log.info("Setting LA Full name {}", caseDetails.getData().getLaSotFullName());
                    laUploadedDocument.getValue().setUser(caseDetails.getData().getLaSotFullName());
                }
            });

        caseDetails.getData().getCombinedDocumentsGenerated().stream()
            .forEach(listValue -> {
                log.info("DocumentType: {}", listValue.getValue().getDocumentType());
                listValue.getValue().getDocumentType()
                .equals(APPLICATION_LA_SUMMARY_EN);
                log.info("LaUploadedDocuments list before: {}",caseDetails.getData().getLaDocumentsUploaded());
                log.info("LaUploadedDocuments list size before: {}",caseDetails.getData().getLaDocumentsUploaded().size());
                caseDetails.getData().getLaDocumentsUploaded().add(listValue);
                log.info("LaUploadedDocuments list after: {}",caseDetails.getData().getLaDocumentsUploaded());
                log.info("LaUploadedDocuments list size after: {}",caseDetails.getData().getLaDocumentsUploaded().size());
            });

        log.info("State set to {}, CaseID {}", caseDetails.getState(), caseDetails.getId());

        return caseDetails;
    }
}
