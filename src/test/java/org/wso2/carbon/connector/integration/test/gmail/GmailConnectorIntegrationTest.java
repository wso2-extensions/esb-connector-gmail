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

package org.wso2.carbon.connector.integration.test.gmail;

import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wso2.connector.integration.test.base.ConnectorIntegrationTestBase;
import org.wso2.connector.integration.test.base.RestResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GmailConnectorIntegrationTest extends ConnectorIntegrationTestBase {

    private Map<String, String> esbRequestHeadersMap = new HashMap<String, String>();
    private Map<String, String> apiRequestHeadersMap = new HashMap<String, String>();

    /**
     * Set up the environment.
     */
    @BeforeClass(alwaysRun = true)
    public void setEnvironment() throws Exception {
        init("gmail-connector-3.0.5");
        esbRequestHeadersMap.put("Accept-Charset", "UTF-8");
        esbRequestHeadersMap.put("Content-Type", "application/json");

        String methodName = "gmail_gmailInit";
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(getProxyServiceURL(methodName), "POST", esbRequestHeadersMap, "gmailInitMandatory.json");
        String accessToken = esbRestResponse.getBody().get("access_token").toString();
        connectorProperties.put("access_Token", accessToken);
        String authorization = connectorProperties.getProperty("access_Token");
        apiRequestHeadersMap.put("Authorization", "Bearer " + authorization);
        apiRequestHeadersMap.putAll(esbRequestHeadersMap);
    }

    /**
     * Positive test case for SendMail method with mandatory parameters.
     */
    @Test(enabled = true, description = "gmail {SendMail} integration test with mandatory parameter.")
    public void testSendMailWithMandatoryParameters() throws IOException, JSONException {
        String methodName = "gmail_sendMail";
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(getProxyServiceURL(methodName), "POST", esbRequestHeadersMap, "sendMailMandatory.json");

        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);

        String messageId = esbRestResponse.getBody().get("id").toString();
        String apiEndPoint =
                connectorProperties.getProperty("apiUrl") + "/" +
                        connectorProperties.getProperty("apiVersion") + "/users/" +
                        connectorProperties.getProperty("userId") + "/messages/" +
                        messageId;
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

        Assert.assertEquals(apiRestResponse.getBody().get("id").toString(), messageId);
    }

    /**
     * Positive test case for listAllMails method with mandatory parameters.
     */
    @Test(enabled = true, description = "gmail {listAllMails} integration test with mandatory parameter.")
    public void testListAllMailsWithMandatoryParameters() throws IOException, JSONException {
        String methodName = "gmail_listAllMails";
        String apiEndPoint =
                connectorProperties.getProperty("apiUrl") + "/" +
                        connectorProperties.getProperty("apiVersion") + "/users/" +
                        connectorProperties.getProperty("userId") + "/messages";

        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(getProxyServiceURL(methodName), "POST", esbRequestHeadersMap, "listAllMailsMandatory.json");
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getBody().toString(), apiRestResponse.getBody().toString());
    }

    /**
     * Positive test case for listAllMails method with optional parameters.
     */
    @Test(enabled = true, description = "gmail {listAllMails} integration test with optional parameter.")
    public void testListAllMailsWithOptionalParameters() throws IOException, JSONException {
        String methodName = "gmail_listAllMails";
        String apiEndPoint =
                connectorProperties.getProperty("apiUrl") + "/" +
                        connectorProperties.getProperty("apiVersion") + "/users/" +
                        connectorProperties.getProperty("userId") + "/messages?includeSpamTrash=" +
                        connectorProperties.getProperty("includeSpamTrash") + "&pageToken=" +
                        connectorProperties.getProperty("pageToken") + "&labelIds=" +
                        connectorProperties.getProperty("labelIds") + "&q=" +
                        connectorProperties.getProperty("q") + "&maxResults=" +
                        connectorProperties.getProperty("maxResults");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(getProxyServiceURL(methodName), "POST", esbRequestHeadersMap, "listAllMailsOptional.json");
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getBody().toString(), apiRestResponse.getBody().toString());
    }

    /**
     * Positive test case for readMail method with mandatory parameters.
     */
    @Test(enabled = true, description = "gmail {readMail} integration test with mandatory parameter.")
    public void testReadMailWithMandatoryParameters() throws IOException, JSONException {
        String methodName = "gmail_readMail";
        String apiEndPoint =
                connectorProperties.getProperty("apiUrl") + "/" +
                        connectorProperties.getProperty("apiVersion") + "/users/" +
                        connectorProperties.getProperty("userId") + "/messages/" +
                        connectorProperties.getProperty("mailId");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(getProxyServiceURL(methodName), "POST", esbRequestHeadersMap, "readMailMandatory.json");
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getBody().toString(), apiRestResponse.getBody().toString());
    }

    /**
     * Positive test case for readMail method with optional parameters.
     */
    @Test(enabled = true, description = "gmail {readMail} integration test with optional parameter.")
    public void testReadMailWithOptionalParameters() throws IOException, JSONException {
        String methodName = "gmail_readMail";
        String apiEndPoint =
                connectorProperties.getProperty("apiUrl") + "/" +
                        connectorProperties.getProperty("apiVersion") + "/users/" +
                        connectorProperties.getProperty("userId") + "/messages/" +
                        connectorProperties.getProperty("mailId") + "?format=" +
                        connectorProperties.getProperty("format");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(getProxyServiceURL(methodName), "POST", esbRequestHeadersMap, "readMailOptional.json");
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getBody().toString(), apiRestResponse.getBody().toString());
    }

    /**
     * Positive test case for listLabels method with mandatory parameters.
     */
    @Test(enabled = true, description = "gmail {listLabels} integration test with mandatory parameter.")
    public void testListLabelsWithMandatoryParameters() throws IOException, JSONException {
        String methodName = "gmail_listLabels";
        String apiEndPoint =
                connectorProperties.getProperty("apiUrl") + "/" +
                        connectorProperties.getProperty("apiVersion") + "/users/" +
                        connectorProperties.getProperty("userId") + "/labels";
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(getProxyServiceURL(methodName), "POST", esbRequestHeadersMap, "listLabelsMandatory.json");
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getBody().toString(), apiRestResponse.getBody().toString());
    }

    /**
     * Positive test case for readLabel method with mandatory parameters.
     */
    @Test(enabled = true, description = "gmail {readLabel} integration test with mandatory parameter.")
    public void testReadLabelWithMandatoryParameters() throws IOException, JSONException {
        String methodName = "gmail_readLabel";
        String apiEndPoint =
                connectorProperties.getProperty("apiUrl") + "/" +
                        connectorProperties.getProperty("apiVersion") + "/users/" +
                        connectorProperties.getProperty("userId") + "/labels/" +
                        connectorProperties.getProperty("labelId");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(getProxyServiceURL(methodName), "POST", esbRequestHeadersMap, "readLabelMandatory.json");
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getBody().toString(), apiRestResponse.getBody().toString());
    }

    /**
     * Positive test case for listAllThreads method with mandatory parameters.
     */
    @Test(enabled = true, description = "gmail {listAllThreads} integration test with mandatory parameter.")
    public void testListAllThreadsWithMandatoryParameters() throws IOException, JSONException {
        String methodName = "gmail_listAllThreads";
        String apiEndPoint =
                connectorProperties.getProperty("apiUrl") + "/" +
                        connectorProperties.getProperty("apiVersion") + "/users/" +
                        connectorProperties.getProperty("userId") + "/threads";
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(getProxyServiceURL(methodName), "POST", esbRequestHeadersMap, "listAllThreadsMandatory.json");
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getBody().toString(), apiRestResponse.getBody().toString());
    }

    /**
     * Positive test case for listAllThreads method with optional parameters.
     */
    @Test(enabled = true, description = "gmail {listAllThreads} integration test with optional parameter.")
    public void testListAllThreadsWithOptionalParameters() throws IOException, JSONException {
        String methodName = "gmail_listAllThreads";
        String apiEndPoint =
                connectorProperties.getProperty("apiUrl") + "/" +
                        connectorProperties.getProperty("apiVersion") + "/users/" +
                        connectorProperties.getProperty("userId") + "/threads?includeSpamTrash=" +
                        connectorProperties.getProperty("includeSpamTrash") + "&pageToken=" +
                        connectorProperties.getProperty("pageToken") + "&labelIds=" +
                        connectorProperties.getProperty("labelIds") + "&q=" +
                        connectorProperties.getProperty("q") + "&maxResults=" +
                        connectorProperties.getProperty("maxResults");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(getProxyServiceURL(methodName), "POST", esbRequestHeadersMap, "listAllThreadsOptional.json");
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getBody().toString(), apiRestResponse.getBody().toString());
    }

    /**
     * Positive test case for readThread method with mandatory parameters.
     */
    @Test(enabled = true, description = "gmail {readThread} integration test with mandatory parameter.")
    public void testReadThreadWithMandatoryParameters() throws IOException, JSONException {
        String methodName = "gmail_readThread";
        String apiEndPoint =
                connectorProperties.getProperty("apiUrl") + "/" +
                        connectorProperties.getProperty("apiVersion") + "/users/" +
                        connectorProperties.getProperty("userId") + "/threads/" +
                        connectorProperties.getProperty("threadId");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(getProxyServiceURL(methodName), "POST", esbRequestHeadersMap, "readThreadMandatory.json");
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getBody().toString(), apiRestResponse.getBody().toString());
    }

    /**
     * Positive test case for readThread method with optional parameters.
     */
    @Test(enabled = true, description = "gmail {readThread} integration test with optional parameter.")
    public void testReadThreadWithOptionalParameters() throws IOException, JSONException {
        String methodName = "gmail_readThread";
        String apiEndPoint =
                connectorProperties.getProperty("apiUrl") + "/" +
                        connectorProperties.getProperty("apiVersion") + "/users/" +
                        connectorProperties.getProperty("userId") + "/threads/" +
                        connectorProperties.getProperty("threadId") + "?format=" +
                        connectorProperties.getProperty("format");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(getProxyServiceURL(methodName), "POST", esbRequestHeadersMap, "readThreadOptional.json");
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getBody().toString(), apiRestResponse.getBody().toString());
    }

    /**
     * Positive test case for createDraft method with mandatory parameters.
     */
    @Test(enabled = true, description = "gmail {createDraft} integration test with mandatory parameter.")
    public void testCreateDraftWithMandatoryParameters() throws IOException, JSONException {
        String methodName = "gmail_createDraft";
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(getProxyServiceURL(methodName), "POST", esbRequestHeadersMap, "createDraftMandatory.json");
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        String messageId = esbRestResponse.getBody().get("id").toString();
        String apiEndPoint =
                connectorProperties.getProperty("apiUrl") + "/" +
                        connectorProperties.getProperty("apiVersion") + "/users/" +
                        connectorProperties.getProperty("userId") + "/drafts/" +
                        messageId;
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

        Assert.assertEquals(apiRestResponse.getBody().get("id").toString(), messageId);
    }

    /**
     * Positive test case for listDrafts method with mandatory parameters.
     */
    @Test(enabled = true, description = "gmail {listDrafts} integration test with mandatory parameter.")
    public void testListDraftsWithMandatoryParameters() throws IOException, JSONException {
        String methodName = "gmail_listDrafts";
        String apiEndPoint =
                connectorProperties.getProperty("apiUrl") + "/" +
                        connectorProperties.getProperty("apiVersion") + "/users/" +
                        connectorProperties.getProperty("userId") + "/drafts";
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(getProxyServiceURL(methodName), "POST", esbRequestHeadersMap, "listDraftsMandatory.json");
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getBody().toString(), apiRestResponse.getBody().toString());
    }

    /**
     * Positive test case for listDrafts method with optional parameters.
     */
    @Test(enabled = true, description = "gmail {listDrafts} integration test with optional parameter.")
    public void testListDraftsWithOptionalParameters() throws IOException, JSONException {
        String methodName = "gmail_listDrafts";
        String apiEndPoint =
                connectorProperties.getProperty("apiUrl") + "/" +
                        connectorProperties.getProperty("apiVersion") + "/users/" +
                        connectorProperties.getProperty("userId") + "/drafts?includeSpamTrash=" +
                        connectorProperties.getProperty("includeSpamTrash") + "&pageToken=" +
                        connectorProperties.getProperty("pageToken") + "&labelIds=" +
                        connectorProperties.getProperty("labelIds") + "&q=" +
                        connectorProperties.getProperty("q") + "&maxResults=" +
                        connectorProperties.getProperty("maxResults");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(getProxyServiceURL(methodName), "POST", esbRequestHeadersMap, "listDraftsOptional.json");
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getBody().toString(), apiRestResponse.getBody().toString());
    }

    /**
     * Positive test case for readDraft method with mandatory parameters.
     */
    @Test(enabled = true, description = "gmail {readDraft} integration test with mandatory parameter.")
    public void testReadDraftWithMandatoryParameters() throws IOException, JSONException {
        String methodName = "gmail_readDraft";
        String apiEndPoint =
                connectorProperties.getProperty("apiUrl") + "/" +
                        connectorProperties.getProperty("apiVersion") + "/users/" +
                        connectorProperties.getProperty("userId") + "/drafts/" +
                        connectorProperties.getProperty("draftId");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(getProxyServiceURL(methodName), "POST", esbRequestHeadersMap, "readDraftMandatory.json");
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getBody().toString(), apiRestResponse.getBody().toString());
    }

    /**
     * Positive test case for readDraft method with optional parameters.
     */
    @Test(enabled = true, description = "gmail {readDraft} integration test with optional parameter.")
    public void testReadDraftWithOptionalParameters() throws IOException, JSONException {
        String methodName = "gmail_readDraft";
        String apiEndPoint =
                connectorProperties.getProperty("apiUrl") + "/" +
                        connectorProperties.getProperty("apiVersion") + "/users/" +
                        connectorProperties.getProperty("userId") + "/drafts/" +
                        connectorProperties.getProperty("draftId") + "?format=" +
                        connectorProperties.getProperty("format");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(getProxyServiceURL(methodName), "POST", esbRequestHeadersMap, "readDraftOptional.json");
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getBody().toString(), apiRestResponse.getBody().toString());
    }

    /**
     * Positive test case for getUserProfile method with mandatory parameters.
     */
    @Test(enabled = true, description = "gmail {getUserProfile} integration test with mandatory parameter.")
    public void testGetUserProfileWithMandatoryParameters() throws IOException, JSONException {
        String methodName = "gmail_getUserProfile";
        String apiEndPoint =
                connectorProperties.getProperty("apiUrl") + "/" +
                        connectorProperties.getProperty("apiVersion") + "/users/" +
                        connectorProperties.getProperty("userId") + "/profile";
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(getProxyServiceURL(methodName), "POST", esbRequestHeadersMap, "getUserProfileMandatory.json");
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getBody().toString(), apiRestResponse.getBody().toString());
    }

    /**
     * Positive test case for listTheHistory method with mandatory parameters.
     */
    @Test(enabled = true, description = "gmail {listTheHistory} integration test with mandatory parameter.")
    public void testListTheHistoryWithMandatoryParameters() throws IOException, JSONException {
        String methodName = "gmail_listTheHistory";
        String apiEndPoint =
                connectorProperties.getProperty("apiUrl") + "/" +
                        connectorProperties.getProperty("apiVersion") + "/users/" +
                        connectorProperties.getProperty("userId") + "/history?startHistoryId=" +
                        connectorProperties.getProperty("startHistoryId");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(getProxyServiceURL(methodName), "POST", esbRequestHeadersMap, "listTheHistoryMandatory.json");
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getBody().toString(), apiRestResponse.getBody().toString());
    }

    /**
     * Positive test case for listTheHistory method with optional parameters.
     */
    @Test(enabled = true, description = "gmail {listTheHistory} integration test with optional parameter.")
    public void testListTheHistoryWithOptionalParameters() throws IOException, JSONException {
        String methodName = "gmail_listTheHistory";
        String apiEndPoint =
                connectorProperties.getProperty("apiUrl") + "/" +
                        connectorProperties.getProperty("apiVersion") + "/users/" +
                        connectorProperties.getProperty("userId") + "/history?startHistoryId=" +
                        connectorProperties.getProperty("startHistoryId") + "&labelId=" +
                        connectorProperties.getProperty("labelId") + "&maxResults=" +
                        connectorProperties.getProperty("maxResults");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(getProxyServiceURL(methodName), "POST", esbRequestHeadersMap, "listTheHistoryOptional.json");
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

        Assert.assertEquals(esbRestResponse.getBody().toString(), apiRestResponse.getBody().toString());
    }

    /**
     * Positive test case for createLabels method with mandatory parameters.
     */
    @Test(enabled = true, description = "gmail {createLabels} integration test with mandatory parameter.")
    public void testCreateLabelsWithMandatoryParameters() throws IOException, JSONException {
        String methodName = "gmail_createLabels";
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(getProxyServiceURL(methodName), "POST", esbRequestHeadersMap, "createLabelsMandatory.json");
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        String messageId = esbRestResponse.getBody().get("id").toString();
        String apiEndPoint =
                connectorProperties.getProperty("apiUrl") + "/" +
                        connectorProperties.getProperty("apiVersion") + "/users/" +
                        connectorProperties.getProperty("userId") + "/labels/" +
                        messageId;
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

        Assert.assertEquals(apiRestResponse.getBody().get("name").toString(), connectorProperties.getProperty("labelNameMandatory"));
    }

    /**
     * Positive test case for createLabels method with optional parameters.
     */
    @Test(enabled = true, description = "gmail {createLabels} integration test with optional parameter.")
    public void testCreateLabelsWithOptionalParameters() throws IOException, JSONException {
        String methodName = "gmail_createLabels";
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(getProxyServiceURL(methodName), "POST", esbRequestHeadersMap, "createLabelsOptional.json");
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        String messageId = esbRestResponse.getBody().get("id").toString();
        String apiEndPoint =
                connectorProperties.getProperty("apiUrl") + "/" +
                        connectorProperties.getProperty("apiVersion") + "/users/" +
                        connectorProperties.getProperty("userId") + "/labels/" +
                        messageId;
        RestResponse<JSONObject> apiRestResponse = sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

        Assert.assertEquals(apiRestResponse.getBody().get("name").toString(), connectorProperties.getProperty("labelNameOptional"));
    }
}
