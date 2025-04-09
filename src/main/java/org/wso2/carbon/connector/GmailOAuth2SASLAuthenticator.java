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
import com.google.code.javax.mail.MessagingException;
import com.google.code.javax.mail.Session;
import com.google.code.javax.mail.Transport;
import com.google.code.samples.oauth2.OAuth2Authenticator;
import com.google.code.samples.oauth2.OAuth2SaslClientFactory;

import java.util.Properties;

/**
 * Provide the OAuth authentication for both IMAP and SMTP.
 */
public final class GmailOAuth2SASLAuthenticator {

    /**
     * Making the default constructor private since Utility classes should not
     * have a public constructors
     */
    private GmailOAuth2SASLAuthenticator() {
    }

    /**
     * Installing the OAuth2 SASL provider.
     */
    public static void initializeOAuth2Provider() {
        OAuth2Authenticator.initialize();
    }

    /**
     * Connects to SMTP transport and mail session.
     *
     * @param username    user name
     * @param accessToken OAuth access token of the user
     * @return {@link GmailSMTPConnection} instance
     * @throws com.google.code.javax.mail.MessagingException as a result of authentication failure
     */
    public static GmailSMTPConnection connectToSMTP(String username, String accessToken)
            throws MessagingException {
        Properties props = new Properties();
        props.put(GmailConstants.MAIL_SMTP_AUTH, GmailConstants.GMAIL_TRUE_VALUE);
        props.put(GmailConstants.MAIL_SMTP_STARTTLS_ENABLE, GmailConstants.GMAIL_TRUE_VALUE);
        props.put(GmailConstants.MAIL_SMTP_STARTTLS_REQUIRED, GmailConstants.GMAIL_TRUE_VALUE);
        props.put(GmailConstants.MAIL_SMTP_SSL_PROTOCOLS, GmailConstants.GMAIL_SSL_PROTOCOLS);
        props.put(GmailConstants.MAIL_SMTP_SSL_TRUST, GmailConstants.GMAIL_SMTP_HOST);
        props.put(GmailConstants.MAIL_SMTP_HOST, GmailConstants.GMAIL_SMTP_HOST);
        props.put(GmailConstants.MAIL_SMTP_PORT, String.valueOf(GmailConstants.GMAIL_SMTP_PORT));
        props.put(GmailConstants.MAIL_SMTP_SASL_ENABLE, GmailConstants.GMAIL_TRUE_VALUE);
        props.put(GmailConstants.MAIL_SMTP_SASL_MECHANISMS, GmailConstants.GMAIL_AUTHENTICATION_MECHANISM);
        props.put(OAuth2SaslClientFactory.OAUTH_TOKEN_PROP, accessToken);

        Session session = Session.getInstance(props);

        // Get transport through the session instead of creating it directly
        Transport transport = session.getTransport(GmailConstants.SMTP_PROTOCOL);
        transport.connect(GmailConstants.GMAIL_SMTP_HOST, GmailConstants.GMAIL_SMTP_PORT, username, "");

        return new GmailSMTPConnection(session, (SMTPTransport) transport);
    }
}