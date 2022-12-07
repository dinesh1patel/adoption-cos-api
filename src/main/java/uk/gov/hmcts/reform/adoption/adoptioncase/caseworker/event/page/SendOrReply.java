package uk.gov.hmcts.reform.adoption.adoptioncase.caseworker.event.page;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import uk.gov.hmcts.ccd.sdk.api.CaseDetails;
import uk.gov.hmcts.ccd.sdk.api.callback.AboutToStartOrSubmitResponse;
import uk.gov.hmcts.ccd.sdk.type.DynamicList;
import uk.gov.hmcts.ccd.sdk.type.DynamicListElement;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.CaseData;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.State;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.SelectedMessage;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.MessageDocumentList;
import uk.gov.hmcts.reform.adoption.adoptioncase.model.MessageSendDetails;
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
            .mandatory(CaseData::getReplyMsgDynamicList, "messageAction=\"replyMessage\"")
            .page("pageSendOrReply3")
            .showCondition("messageAction=\"replyMessage\"")
            .label("labelReplyMes", "## Reply a message")
            .complex(CaseData::getSelectedMessage)
            .readonly(SelectedMessage::getReasonForMessage)
            .readonly(SelectedMessage::getUrgency)
            .readonly(SelectedMessage::getMessage)
            .readonly(SelectedMessage::getDocumentLink)
            .mandatory(SelectedMessage::getReplyMessage)
            .done()
            .page("pageSendOrReply2")
            .showCondition("messageAction=\"sendMessage\"")
            .label("sendMessage","## Send a message")
            .complex(CaseData::getMessageSendDetails)
            .mandatory(MessageSendDetails::getMessageReceiverRoles)
            .mandatory(MessageSendDetails::getMessageReasonList)
            .mandatory(MessageSendDetails::getMessageUrgencyList)
            .mandatory(MessageSendDetails::getMessage)
            .done()
            .mandatory(CaseData::getSendMessageAttachDocument)
            .mandatory(CaseData::getAttachDocumentList, "sendMessageAttachDocument=\"Yes\"")
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
                if (item.getValue().getName() != null) {
                    UUID result = UUID.nameUUIDFromBytes(item.getValue().getName().getBytes());
                    listElements.add(DynamicListElement.builder()
                                         .label(item.getValue().getDocumentLink().getFilename()).code(result).build());
                    messageDocumentLists.add(new MessageDocumentList(result.toString(), item.getValue().getDocumentLink()));
                }
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
        caseData.setAttachDocumentList(DynamicList.builder().listItems(listElements).value(DynamicListElement.EMPTY).build());

        if (CollectionUtils.isNotEmpty(caseData.getListOfOpenMessages()) && caseData.getReplyMsgDynamicList() != null) {
            var messageDetails = new SelectedMessage();
            var selectedObject = caseData.getListOfOpenMessages().stream()
                .filter(item -> item.getValue().getMessageId().equalsIgnoreCase(caseData.getReplyMsgDynamicList()
                                                                               .getValueCode().toString())).findFirst();
            if (selectedObject.isPresent()) {
                messageDetails.setMessageId(selectedObject.get().getValue().getMessageId());
                messageDetails.setUrgency(selectedObject.get().getValue().getMessageUrgencyList().getLabel());
                messageDetails.setMessage(selectedObject.get().getValue().getMessage());
                messageDetails.setReasonForMessage(selectedObject.get().getValue().getMessageReasonList().getLabel());
                if (caseData.getAttachDocumentList() != null) {
                    System.out.println(caseData.getAttachDocumentList().getValue());
                    var doc = messageDocumentLists.stream().filter(item ->
                                                  item.getMessageId()
                                                      .equalsIgnoreCase(
                                                          selectedObject.get().getValue().getSelectedDocumentId())).findFirst().get();
                    messageDetails.setDocumentLink(doc.getDocumentLink());
                }
                caseData.setSelectedMessage(messageDetails);
            }


        }
        log.info("MidEvent Complete");
        return AboutToStartOrSubmitResponse.<CaseData, State>builder()
            .data(caseData)
            .build();
    }

}