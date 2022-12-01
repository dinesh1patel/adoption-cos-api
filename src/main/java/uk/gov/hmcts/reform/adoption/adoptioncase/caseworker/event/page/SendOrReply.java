package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.ccd.sdk.type.DynamicList;
import uk.gov.hmcts.ccd.sdk.type.DynamicListElement;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.*;
import uk.gov.hmcts.reform.adoption.common.ccd.CcdPageConfiguration;
import uk.gov.hmcts.reform.adoption.common.ccd.PageBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
public class SendOrReply implements CcdPageConfiguration {
    @Override
    public void addTo(PageBuilder pageBuilder) {
        pageBuilder
            .page("pageSendOrReply1", this::midEvent)
            .mandatory(CaseData::getMessageAction)
            .page("pageSendOrReply2")
            .showCondition("messageAction=\"sendMessage\"")
            .label("sendMessage","## Send a message")
            .complex(CaseData::getManageSendMessagesDetails)
            .mandatory(ManageSendMessagesDetails::getMessageReceiverRoles)
            .mandatory(ManageSendMessagesDetails::getMessageReasonList)
            .mandatory(ManageSendMessagesDetails::getMessageUrgencyList)
            .mandatory(ManageSendMessagesDetails::getMessage)
            .mandatory(ManageSendMessagesDetails::getAttachDocument)
            .mandatory(ManageSendMessagesDetails::getDocumentList, "attachDocument=\"Yes\"")
            .done()
            .page("pageSendOrReply3")
            .mandatory(CaseData::getMessagesList, "messageAction=\"replyMessage\"")
            .showCondition("messageAction=\"replyMessage\"")
            .label("labelReplyMes", "## Reply a message")
            .complex(CaseData::getMessageDetails)
            .readonlyWithLabel(MessageDetails::getMessageId, "Message Id")
            .readonlyWithLabel(MessageDetails::getReasonForMessage, "Reason for message")
            .readonlyWithLabel(MessageDetails::getUrgency, "Urgency")
            .readonlyWithLabel(MessageDetails::getMessage, "Message")
            .readonlyWithLabel(MessageDetails::getDocumentLink, "View documents attached to message")
            .mandatory(MessageDetails::getReplyMessage)
            .done();

    }


    private AboutToStartOrSubmitResponse<CaseData, State> midEvent(CaseDetails<CaseData, State>
        data, CaseDetails<CaseData, State> caseDataStateCaseDetails1) {
        log.info("MidEvent Triggered");
        CaseData caseData = data.getData();

        List<DynamicListElement> listElements = new ArrayList<>();
        List<MessageDocumentList> messageDocumentLists = new ArrayList<>();
        if (caseData.getAdditionalDocumentsCategory() != null && caseData.getApplicationDocumentsCategory().size() > 0) {
            caseData.getAdditionalDocumentsCategory().forEach(item -> {
                UUID result = UUID.nameUUIDFromBytes(item.getValue().getName().getBytes());
                listElements.add(DynamicListElement.builder()
                                     .label(item.getValue().getDocumentLink().getFilename()).code(result).build());
                messageDocumentLists.add(new MessageDocumentList(result.toString(), item.getValue().getDocumentLink()));
            });
        }

        if (caseData.getCorrespondenceDocumentCategory() != null
            && caseData.getCorrespondenceDocumentCategory().size() > 0) {
            caseData.getCorrespondenceDocumentCategory().forEach(item -> {
                UUID result = UUID.nameUUIDFromBytes(item.getValue().getName().getBytes());
                listElements.add(DynamicListElement.builder()
                                     .label(item.getValue().getDocumentLink().getFilename()).code(result).build());
                messageDocumentLists.add(new MessageDocumentList(result.toString(), item.getValue().getDocumentLink()));
            });
        }

        if (caseData.getReportsDocumentCategory() != null
            && caseData.getReportsDocumentCategory().size() > 0) {
            caseData.getReportsDocumentCategory().forEach(item -> {
                UUID result = UUID.nameUUIDFromBytes(item.getValue().getName().getBytes());
                listElements.add(DynamicListElement.builder()
                                     .label(item.getValue().getDocumentLink().getFilename()).code(result).build());
                messageDocumentLists.add(new MessageDocumentList(result.toString(), item.getValue().getDocumentLink()));
            });
        }

        if (caseData.getStatementsDocumentCategory() != null
            && caseData.getStatementsDocumentCategory().size() > 0) {
            caseData.getStatementsDocumentCategory().forEach(item -> {
                UUID result = UUID.nameUUIDFromBytes(item.getValue().getName().getBytes());
                listElements.add(DynamicListElement.builder()
                                     .label(item.getValue().getDocumentLink().getFilename()).code(result).build());
                messageDocumentLists.add(new MessageDocumentList(result.toString(), item.getValue().getDocumentLink()));
            });
        }

        if (caseData.getCourtOrdersDocumentCategory() != null
            && caseData.getCourtOrdersDocumentCategory().size() > 0) {
            caseData.getCourtOrdersDocumentCategory().forEach(item -> {
                UUID result = UUID.nameUUIDFromBytes(item.getValue().getName().getBytes());
                listElements.add(DynamicListElement.builder()
                                     .label(item.getValue().getDocumentLink().getFilename()).code(result).build());
                messageDocumentLists.add(new MessageDocumentList(result.toString(), item.getValue().getDocumentLink()));
            });
        }

        if (caseData.getApplicationDocumentsCategory() != null
            && caseData.getApplicationDocumentsCategory().size() > 0) {
            caseData.getApplicationDocumentsCategory().forEach(item -> {
                UUID result = UUID.nameUUIDFromBytes(item.getValue().getName().getBytes());
                listElements.add(DynamicListElement.builder()
                                     .label(item.getValue().getDocumentLink().getFilename()).code(result).build());
                messageDocumentLists.add(new MessageDocumentList(result.toString(), item.getValue().getDocumentLink()));

            });
        }
        var messageSendDetails = new ManageSendMessagesDetails();
        messageSendDetails.setDocumentList(DynamicList.builder().listItems(listElements).value(DynamicListElement.EMPTY).build());
        caseData.setManageSendMessagesDetails(messageSendDetails);

        if (CollectionUtils.isNotEmpty(caseData.getListOfSendMessages()) && caseData.getMessagesList() != null) {
            var messageDetails = new MessageDetails();
            var selectedObject = caseData.getListOfSendMessages().stream()
                .filter(item -> item.getValue().getMessageId().equalsIgnoreCase(caseData.getMessagesList().getValue()
                .getLabel())).findAny();
            if (selectedObject.isPresent()) {
                messageDetails.setMessageId(selectedObject.get().getValue().getMessageId());
                messageDetails.setUrgency(selectedObject.get().getValue().getMessageUrgencyList().getLabel());
                messageDetails.setMessage(selectedObject.get().getValue().getMessage());
                messageDetails.setReasonForMessage(selectedObject.get().getValue().getMessageReasonList().getLabel());
                if (selectedObject.get().getValue().getDocumentList() != null && CollectionUtils.isNotEmpty(messageDocumentLists)) {
                    messageDetails.setDocumentLink(messageDocumentLists.stream().filter(item ->
                        item.getMessageId().equalsIgnoreCase(selectedObject.get().getValue().getDocumentList()
                        .getValue().getCode().toString())).findFirst().get().getDocumentLink());
                }
                caseData.setMessageDetails(messageDetails);
            }


        }
        log.info("MidEvent Complete");
        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(caseData)
            .build();
    }

}