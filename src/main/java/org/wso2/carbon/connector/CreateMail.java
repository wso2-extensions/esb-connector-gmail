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

import org.apache.commons.codec.binary.Base64;
import org.apache.synapse.MessageContext;
import org.wso2.integration.connector.core.AbstractConnector;
import org.wso2.integration.connector.core.ConnectException;


public class CreateMail extends AbstractConnector {
    public static final String parameters = "parameters";

    public void connect(MessageContext messageContext)
            throws ConnectException {
        Object templateParam = getParameter(messageContext, "generated_param");
        try {
            String parameter = messageContext.getProperty("parameters").toString();
            byte[] encodedBytes = Base64.encodeBase64(parameter.getBytes());
            String encodedVal = new String(encodedBytes);
            encodedVal = encodedVal.replace('+', '-');
            encodedVal = encodedVal.replace('/', '_');
            messageContext.setProperty("uri.var.encoded", encodedVal);
        } catch (Exception e) {
            throw new ConnectException(e);
        }
    }
}