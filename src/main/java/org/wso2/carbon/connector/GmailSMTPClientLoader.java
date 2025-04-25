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

import com.google.code.javax.mail.MessagingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.wso2.integration.connector.core.ConnectException;

/**
 * Class which loads the {@link GmailSMTPConnection} according to the
 * authentication mode.
 */
public class GmailSMTPClientLoader {

    /**
     * Log instance.
     */
    private static Log log = LogFactory.getLog(GmailSMTPClientLoader.class);

    /**
     * Method which loads the {@link GmailSMTPConnection} instance
     * according to the authentication mode.
     *
     * @param messageContext Message context where the instantiated
     *                       {@link GmailSMTPConnection} instance is stored.
     * @return the loaded {@link GmailSMTPConnection} instance
     * @throws org.wso2.carbon.connector.core.ConnectException as a result of invalid configuration
     * @throws com.google.code.javax.mail.MessagingException   as a result of authentication failures
     */
    public GmailSMTPConnection loadSMTPSession(MessageContext messageContext)
            throws ConnectException, MessagingException {
        org.apache.axis2.context.MessageContext axis2MsgCtx =
                ((Axis2MessageContext) messageContext).getAxis2MessageContext();
        Object prestoredInstance = axis2MsgCtx.getOperationContext()
                .getProperty(GmailConstants.GMAIL_SMTP_CONNECTION_INSTANCE);

        // Use if there exists an already stored GmailSMTPConnectionObject
        // instance.
        if (prestoredInstance != null) {
            log.info("Restoring the preinstantiated SMTP session");
            return (GmailSMTPConnection) prestoredInstance;
        }

        GmailSMTPConnection smtpConnectionObject = null;

        if (axis2MsgCtx.getProperty(GmailConstants.GMAIL_OAUTH2_PROVIDER) == null) {
            GmailOAuth2SASLAuthenticator.initializeOAuth2Provider();
            axis2MsgCtx.getOperationContext().setProperty(GmailConstants.GMAIL_OAUTH2_PROVIDER, "initialized");
        }
        smtpConnectionObject = GmailOAuth2SASLAuthenticator.connectToSMTP(messageContext
                        .getProperty(GmailConstants.GMAIL_FROM_ADDRESS).toString(),
                messageContext.getProperty(GmailConstants.GMAIL_ACCESSTOKEN).toString());

        // Stores the newly instantiated GmailSMTPConnectionObject in the
        // operation context.
        axis2MsgCtx.getOperationContext().setProperty(GmailConstants.GMAIL_SMTP_CONNECTION_INSTANCE, smtpConnectionObject);
        return smtpConnectionObject;
    }
}
