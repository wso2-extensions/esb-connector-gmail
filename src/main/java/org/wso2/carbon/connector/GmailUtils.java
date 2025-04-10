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
import com.google.code.javax.activation.DataHandler;
import com.google.code.javax.mail.Message;
import com.google.code.javax.mail.MessagingException;
import com.google.code.javax.mail.Session;
import com.google.code.javax.mail.internet.InternetAddress;
import com.google.code.javax.mail.internet.MimeBodyPart;
import com.google.code.javax.mail.internet.MimeMessage;
import com.google.code.javax.mail.internet.MimeMultipart;
import com.google.code.javax.mail.util.ByteArrayDataSource;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.context.OperationContext;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseConstants;
import org.wso2.integration.connector.core.ConnectException;
import org.wso2.integration.connector.core.util.ConnectorUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public final class GmailUtils {

    /**
     * Log instance.
     */
    private static Log log = LogFactory.getLog(GmailUtils.class);

    /**
     * Making the default constructor private since Utility classes should not
     * have a public constructors
     */
    private GmailUtils() {
    }

    /**
     * Extracts a given parameter from message context
     *
     * @param messageContext Input message context
     * @param paramName      Name of the parameter to extract from the message context
     * @return extracted parameter as a {@link String}
     */
    public static String lookupFunctionParam(MessageContext messageContext, String paramName) {
        return (String) ConnectorUtils.lookupTemplateParamater(messageContext, paramName);
    }

    /**
     * Stores error response in the message context
     *
     * @param messageContext Message Context where the error response should be stored
     * @param e              Exception
     */
    public static void storeErrorResponseStatus(MessageContext messageContext, final Throwable e,
                                                int errorCode) {
        messageContext.setProperty(SynapseConstants.ERROR_EXCEPTION, e);
        messageContext.setProperty(SynapseConstants.ERROR_MESSAGE, e.getMessage());

        if (messageContext.getEnvelope().getBody().getFirstElement() != null) {
            messageContext.getEnvelope().getBody().getFirstElement().detach();
        }

        OMFactory factory = OMAbstractFactory.getOMFactory();
        OMNamespace ns = factory.createOMNamespace("http://org.wso2.esbconnectors.gmail", "ns");
        OMElement result = factory.createOMElement("ErrorResponse", ns);
        OMElement errorMessageElement = factory.createOMElement("ErrorMessage", ns);
        result.addChild(errorMessageElement);
        errorMessageElement.setText(e.getMessage());
        messageContext.getEnvelope().getBody().addChild(result);

        messageContext.setProperty(SynapseConstants.ERROR_CODE, errorCode);
        messageContext.setFaultResponse(true);
        log.info("Stored the error response");
    }

    /**
     * Close and remove the already stored IMAP and SMTP connections
     *
     * @throws com.google.code.javax.mail.MessagingException
     */
    public static void closeConnection(org.apache.axis2.context.MessageContext axis2MessageContext)
            throws MessagingException {
        if (axis2MessageContext.getProperty(GmailConstants.GMAIL_LOGIN_MODE) == null) {
            return;
        }

        OperationContext operationContext = axis2MessageContext.getOperationContext();

        if (operationContext.getProperty(GmailConstants.GMAIL_SMTP_CONNECTION_INSTANCE) != null) {
            log.info("Closing the previously opened SMTP transport");
            ((GmailSMTPConnection) operationContext.getProperty(GmailConstants.GMAIL_SMTP_CONNECTION_INSTANCE)).getTransport()
                    .close();
            operationContext.removeProperty(GmailConstants.GMAIL_SMTP_CONNECTION_INSTANCE);
        }

        axis2MessageContext.removeProperty((String) axis2MessageContext.getProperty(GmailConstants.GMAIL_LOGIN_MODE));
    }

    /**
     * Creates a new {@link com.google.code.javax.mail.Message}.
     *
     * @param session       Mail {@link com.google.code.javax.mail.Session}.
     * @param subject       Subject of the mail.
     * @param textContent   Text content of the mail message.
     * @param toRecipients  'To' recipients of the mail message.
     * @param ccRecipients  'CC' recipients of the mail message.
     * @param bccRecipients 'BCC' recipients of the mail message.
     * @return returns the created {@link #}
     * @throws com.google.code.javax.mail.MessagingException
     * @throws java.io.IOException
     */
    public static Message createNewMessage(Session session, String subject, String textContent, String toRecipients,
                                           String ccRecipients, String bccRecipients, Map attachmentMap)
            throws ConnectException, MessagingException, IOException {
        log.info("Creating the mail message");
        MimeMessage message = new MimeMessage(session);
        if (toRecipients != null) {
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toRecipients));
        }
        if (ccRecipients != null) {
            message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccRecipients));
        }
        if (bccRecipients != null) {
            message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(bccRecipients));
        }
        message.setSubject(subject);
        MimeMultipart content = new MimeMultipart();
        MimeBodyPart mainPart = new MimeBodyPart();
        mainPart.setText(textContent);
        content.addBodyPart(mainPart);

        // Use the current thread's context classloader
        ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader();

        try {
            Thread.currentThread().setContextClassLoader(javax.activation.DataHandler.class.getClassLoader());

            for (Object set : attachmentMap.entrySet()) {
                Map.Entry entry = (Map.Entry) set;
                String fileName = (String) entry.getValue();
                String filePath = (String) entry.getKey();

                // Use URLDataSource instead of FileDataSource
                File file = new File(filePath);
                javax.activation.URLDataSource urlDataSource = new javax.activation.URLDataSource(file.toURI().toURL());
                javax.activation.DataHandler handler = new javax.activation.DataHandler(urlDataSource);

                // Read the data
                InputStream inStream = handler.getInputStream();
                byte[] bytes = IOUtils.toByteArray(inStream);

                // Use handler.getContentType() to determine the content type
                ByteArrayDataSource source = new ByteArrayDataSource(bytes, handler.getContentType());
                MimeBodyPart bodyPart = new MimeBodyPart();
                bodyPart.setDataHandler(new DataHandler(source));
                bodyPart.setFileName(fileName);
                content.addBodyPart(bodyPart);
            }
        } finally {
            Thread.currentThread().setContextClassLoader(originalClassLoader);
        }

        message.setContent(content);
        return message;
    }

    /**
     * Sends the given {@link com.google.code.javax.mail.Message} through the given {@link com.google.code.com.sun.mail.smtp.SMTPTransport}
     *
     * @param message   The message to be sent
     * @param transport The {@link com.google.code.com.sun.mail.smtp.SMTPTransport} through which the message should be
     *                  sent
     * @throws com.google.code.javax.mail.MessagingException as a result of failures in message transportation
     */
    public static void sendMessage(Message message, SMTPTransport transport)
            throws MessagingException {
        log.info("Sending the mail...");
        transport.sendMessage(message, message.getAllRecipients());
        log.info("The mail is succesfully sent");
    }
}
