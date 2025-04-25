/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *   WSO2 Inc. licenses this file to you under the Apache License,
 *   Version 2.0 (the "License"); you may not use this file except
 *   in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.wso2.carbon.connector;

import com.google.code.com.sun.mail.smtp.SMTPTransport;
import com.google.code.javax.mail.Message;
import com.google.code.javax.mail.MessagingException;
import com.google.code.javax.mail.Session;
import org.apache.commons.lang.StringUtils;
import org.apache.synapse.MessageContext;
import org.wso2.integration.connector.core.AbstractConnector;
import org.wso2.integration.connector.core.ConnectException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class performs the "send mail" operation.
 */
public class GmailMailSender extends AbstractConnector {

    /*
     * Sends an e-mail message to specified recipients.
     */
    @Override
    public void connect(MessageContext messageContext) {
        try {
            // Reading input parameters from the message context
            String toRecipients = (String) messageContext.getProperty(GmailConstants.GMAIL_PARAM_TO_RECIPIENTS);
            String ccRecipients = (String) messageContext.getProperty(GmailConstants.GMAIL_PARAM_CC_RECIPIENTS);
            String bccRecipients = (String) messageContext.getProperty(GmailConstants.GMAIL_PARAM_BCC_RECIPIENTS);
            if (StringUtils.isEmpty(toRecipients) && StringUtils.isEmpty(bccRecipients) && StringUtils.isEmpty(ccRecipients)) {
                String errorLog = "No recipients are found";
                log.error(errorLog);
                ConnectException connectException = new ConnectException(errorLog);
                GmailUtils.storeErrorResponseStatus(messageContext, connectException,
                        GmailErrorCodes.GMAIL_ERROR_CODE_CONNECT_EXCEPTION);
                handleException(connectException.getMessage(), connectException, messageContext);
            }
            String attachmentList = (String) messageContext.getProperty("fileName");
            String attachmentName = (String) messageContext.getProperty("filePath");
            String subject = this.setSubject(messageContext);
            String textContent = this.setTextContent(messageContext);
            GmailSMTPClientLoader smtpClientLoader = new GmailSMTPClientLoader();
            GmailSMTPConnection smtpConnectionObject = smtpClientLoader.loadSMTPSession(messageContext);
            Session session = smtpConnectionObject.getSession();
            SMTPTransport transport = smtpConnectionObject.getTransport();
            Message message = GmailUtils.createNewMessage(session, subject, textContent, toRecipients, ccRecipients,
                    bccRecipients, getAttachmentList(attachmentName, attachmentList));
            GmailUtils.sendMessage(message, transport);
            messageContext.setProperty("Status", "Success");
        } catch (MessagingException e) {
            GmailUtils.storeErrorResponseStatus(messageContext, e, GmailErrorCodes.GMAIL_ERROR_CODE_MESSAGING_EXCEPTION);
            handleException(e.getMessage(), e, messageContext);
        } catch (IOException e) {
            GmailUtils.storeErrorResponseStatus(messageContext, e, GmailErrorCodes.GMAIL_ERROR_CODE_IO_EXCEPTION);
            handleException(e.getMessage(), e, messageContext);
        } catch (Exception e) {
            GmailUtils.storeErrorResponseStatus(messageContext, e, GmailErrorCodes.GMAIL_COMMON_EXCEPTION);
            handleException(e.getMessage(), e, messageContext);
        }
    }

    /**
     * Reads mail's subject parameter from the message context.
     *
     * @param messageContext from where the mail subject should be read
     * @return the mail subject
     */
    private String setSubject(MessageContext messageContext) {
        String subject =
                GmailUtils.lookupFunctionParam(messageContext,
                        GmailConstants.GMAIL_PARAM_SUBJECT);
        if (subject == null || "".equals(subject.trim())) {
            log.warn("Mail subject is not provided. Mail will be sent without a subject");
            subject = "(no suject)";
        }
        return subject;
    }

    private Map<String, String> getAttachmentList(String attachment, String attachmentPath) {
        if (StringUtils.isEmpty(attachment) && StringUtils.isEmpty(attachmentPath)) {
            return null;
        }
        String[] attachmentNameList = attachment.split(",");
        String[] attachmentPathList = attachmentPath.split(",");
        if (attachmentPathList.length != attachmentNameList.length) {
            return null;
        }
        Map<String, String> attachmentMap = new HashMap<String, String>();
        for (String attachmentEntry : attachmentNameList) {
            for (String attachmentPathEntry : attachmentPathList) {
                attachmentMap.put(attachmentEntry, attachmentPathEntry);
                break;
            }
        }
        return attachmentMap;
    }

    /**
     * Reads mail's text content from the message context.
     *
     * @param messageContext from where the text content should be read
     * @return mail's text content
     */
    private String setTextContent(MessageContext messageContext) {
        String textContent =
                GmailUtils.lookupFunctionParam(messageContext,
                        GmailConstants.GMAIL_PARAM_TEXT_CONTENT);
        if (textContent == null || "".equals(textContent.trim())) {
            log.warn("Mail text content is not provided. Mail will be sent without a text content");
            textContent = "";
        }
        return textContent;
    }

    /**
     * Reads recipients parameter from message context
     *
     * @param messageContext from where the recipients should be read
     * @param paramName      Name of the input parameter
     * @return comma separated list of recipients' addresses
     */
    private String setRecipients(MessageContext messageContext, String paramName) {
        String recipients = GmailUtils.lookupFunctionParam(messageContext, paramName);
        if (recipients == null || "".equals(recipients.trim())) {
            return null;
        }
        return recipients;
    }
}