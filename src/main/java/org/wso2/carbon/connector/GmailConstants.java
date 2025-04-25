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

/**
 * Contains all constants used in Gmail connector implementation
 */
public final class GmailConstants {

    /**
     * Gmail login mode. This can be either "OAUTH" or "SASL".
     */
    public static final String GMAIL_LOGIN_MODE = "login.mode";
    /**
     * Gmail "OAUTH" login mode.
     */
    public static final String GMAIL_OAUTH_LOGIN_MODE = "OAUTH";
    /**
     * Name of the "username" parameter in synapse configuration.
     */
    public static final String GMAIL_PARAM_USERNAME = "userId";
    /**
     * Name of the "OAuth access token" parameter in synapse configuration.
     */
    public static final String GMAIL_PARAM_OAUTH_ACCESS_TOKEN = "accessToken";
    /**
     * Property name to store the user name for SASL authentication.
     */
    public static final String GMAIL_USER_USERNAME = "gmail.user.username";
    /**
     * Property name to store the password of the user for SASL authentication.
     */
    public static final String GMAIL_USER_PASSWORD = "gmail.user.password";
    /**
     * Property name to store the user name for OAuth authentication.
     */
    public static final String GMAIL_OAUTH_USERNAME = "gmail.oauth.username";
    /**
     * Property name to store the OAuth2 client ID of the user for SASL
     * authentication.
     */
    public static final String GMAIL_FROM_ADDRESS = "gmail.from.address";
    /**
     * Property name to store the OAuth2 access token of the user for SASL
     * authentication.
     */
    public static final String GMAIL_OAUTH_ACCESS_TOKEN = "gmail.oauth.accessToken";
    /**
     * Property name to store whether the OAuth2 provider is initialized or not.
     */
    public static final String GMAIL_OAUTH2_PROVIDER = "gmail.oauth2.provider";
    /**
     * Property name to store the IMAPStore instance
     */
    public static final String GMAIL_IMAP_STORE_INSTANCE = "gmail.imap.store.instance";
    /**
     * Property name to store the SMTP connection information
     */
    public static final String GMAIL_SMTP_CONNECTION_INSTANCE = "gmail.smtp.session.instance";

    /**
     * Property name to store the SMTP
     */
    public static final String SMTP_PROTOCOL = "smtp";

    /**
     * Name of the "subject" parameter in synapse configuration.
     */
    public static final String GMAIL_PARAM_SUBJECT = "subject";
    /**
     * Name of the "toRecipients" parameter in synapse configuration.
     */
    public static final String GMAIL_PARAM_TO_RECIPIENTS = "to";
    /**
     * Name of the "ccRecipients" parameter in synapse configuration.
     */
    public static final String GMAIL_PARAM_CC_RECIPIENTS = "cc";
    /**
     * Name of the "bccRecipients" parameter in synapse configuration.
     */
    public static final String GMAIL_PARAM_BCC_RECIPIENTS = "bcc";
    /**
     * Name of the "textContent" parameter in synapse configuration.
     */
    public static final String GMAIL_PARAM_TEXT_CONTENT = "messageBody";
    /**
     * Default batch size.
     */
    public static final int GMAIL_BATCH_SIZE = 50;
    /**
     * Gmail folder name for all mails.
     */
    public static final String GMAIL_ALL_MAIL = "[Gmail]/All Mail";
    /**
     * Gmail folder name for trash.
     */
    public static final String GMAIL_TRASH = "[Gmail]/Trash";

    /**
     * Stores the value, "true".
     */
    public static final boolean GMAIL_TRUE_VALUE = true;

    /**
     * Stores the value, "TLSv1.2".
     */
    public static final String GMAIL_SSL_PROTOCOLS = "TLSv1.2";
    /**
     * Gmail authentication mechanism, "XOAUTH2".
     */
    public static final String GMAIL_AUTHENTICATION_MECHANISM = "XOAUTH2";
    /**
     * IMAP host name
     */
    public static final String GMAIL_IMAP_HOST = "imap.gmail.com";
    /**
     * IMAP port
     */
    public static final int GMAIL_IMAP_PORT = 993;
    /**
     * SMTP host name
     */
    public static final String GMAIL_SMTP_HOST = "smtp.gmail.com";
    /**
     * SMTP port
     */
    public static final int GMAIL_SMTP_PORT = 587;
    /**
     * Access Token to access the GMAIL REST api
     */
    public static final String GMAIL_ACCESSTOKEN = "_ACTIVE_ACCESS_TOKEN_";

    /**
     * Property name for enabling SMTP authentication.
     */
    public static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
    /**
     * Property name for enabling STARTTLS.
     */
    public static final String MAIL_SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";
    /**
     * Property name for requiring STARTTLS.
     */
    public static final String MAIL_SMTP_STARTTLS_REQUIRED = "mail.smtp.starttls.required";
    /**
     * Property name for specifying SSL protocols.
     */
    public static final String MAIL_SMTP_SSL_PROTOCOLS = "mail.smtp.ssl.protocols";
    /**
     * Property name for trusting the SMTP server.
     */
    public static final String MAIL_SMTP_SSL_TRUST = "mail.smtp.ssl.trust";
    /**
     * Property name for specifying the SMTP host.
     */
    public static final String MAIL_SMTP_HOST = "mail.smtp.host";
    /**
     * Property name for specifying the SMTP port.
     */
    public static final String MAIL_SMTP_PORT = "mail.smtp.port";
    /**
     * Property name for enabling SASL.
     */
    public static final String MAIL_SMTP_SASL_ENABLE = "mail.smtp.sasl.enable";
    /**
     * Property name for specifying SASL mechanisms.
     */
    public static final String MAIL_SMTP_SASL_MECHANISMS = "mail.smtp.sasl.mechanisms";

    /**
     * Making the default constructor private since Utility classes should not
     * have a public constructors
     */
    private GmailConstants() {
    }
}
