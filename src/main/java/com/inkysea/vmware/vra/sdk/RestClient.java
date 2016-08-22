package com.inkysea.vmware.vra.sdk;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by kthieler on 6/21/16.
 */


public class RestClient {

    private final String userName;
    private final String password;
    private final String tenant;
    private final String authRestUrl;
    private final String token;


    private final String TOKEN_JSON = "{\"username\": \"%s\", \"password\": \"%s\", \"tenant\": \"%s\"}";

    private static final Logger LOGGER = Logger.getLogger(RestClient.class.getName());

    public RestClient(String vraHost, String userName, String password, String tenant) throws IOException {

        this.authRestUrl = vraHost + "/identity/api/tokens";

        this.userName = userName;
        this.password = password;
        this.tenant   = tenant;
        this.token = AuthToken();

    }

    public int dummy(){
        return 1;
    }

    public String AuthToken() throws IOException {

        String token = "";

        String tokenPayload = String.format(TOKEN_JSON, userName, password, tenant);
        LOGGER.info("Authentication Payload: "+tokenPayload);

        HttpResponse httpResponse = this.Post(authRestUrl, tokenPayload);
        String result = EntityUtils.toString(httpResponse.getEntity());
        LOGGER.info("HTTP Response : "+result);


        JsonParser jsonParser = new JsonParser();
        JsonObject jsonResponse = (JsonObject)jsonParser.parse(result);

        JsonElement idElement = jsonResponse.get("id");

        if (idElement == null) {
            throw new IOException(jsonResponse.toString());
        } else {
            token = idElement.getAsString();
        }
        LOGGER.info("Token : "+token);

        return token;
    }

    public HttpResponse Get(String URL ){

        LOGGER.info("HTTP GET URL: " + URL);


        CloseableHttpClient httpClient = null;
        try {
            httpClient = HttpClient();
            HttpGet request = new HttpGet(URL);
            request.setHeader("accept", "application/json; charset=utf-8");
            if (StringUtils.isNotBlank(token)) {
                String authorization = "Bearer " + token;
                request.setHeader("Authorization", authorization);
            }
            return httpClient.execute(request);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HttpResponse Post(String URL, String payload){

        LOGGER.info("HTTP POST URL: " + URL);
        LOGGER.info("HTTP POST Payload: " + payload);

        CloseableHttpClient httpClient = null;
        try {
            httpClient = HttpClient();
            HttpPost postRequest = new HttpPost(URL);
            StringEntity input = null;
            try {
                input = new StringEntity(payload);
            } catch (UnsupportedEncodingException e) {
                LOGGER.severe(e.toString());
            }

            input.setContentType("application/json");
            postRequest.setEntity(input);
            postRequest.setHeader("Content-Type", "application/json");
            postRequest.setHeader("accept", "application/json; charset=utf-8");
            if (StringUtils.isNotBlank(token)) {
                String authorization = "Bearer " + token;
                postRequest.setHeader("Authorization", authorization);
            }

            return httpClient.execute(postRequest);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HttpResponse Post(String URL, HttpEntity multipart ){

        LOGGER.info("HTTP POST URL: " + URL);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            httpClient = HttpClient();
            HttpPost postRequest = new HttpPost(URL);

            postRequest.setEntity(multipart);

            //postRequest.setHeader("Content-Type", "multipart/form-data;boundary=vRAJenkinsUpload");
            //postRequest.setHeader("accept", "application/json; charset=utf-8");


            if (StringUtils.isNotBlank(token)) {
                String authorization = "Bearer " + token;
                postRequest.setHeader("Authorization", authorization);
            }
            CloseableHttpResponse response = httpClient.execute(postRequest);
            // assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
            //httpClient.close();

            return response;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HttpResponse Put( String URL, String payload ){
        LOGGER.info("HTTP PUT URL: " + URL);

        CloseableHttpClient httpClient = null;
        try {
            httpClient = HttpClient();
            HttpPut putRequest = new HttpPut(URL);
            StringEntity input = new StringEntity(payload);
            input.setContentType("application/json");
            putRequest.setEntity(input);
            putRequest.setHeader("Content-Type", "application/json");
            putRequest.setHeader("accept", "application/json; charset=utf-8");
            if (StringUtils.isNotBlank(token)) {
                String authorization = "Bearer " + token;
                putRequest.setHeader("Authorization", authorization);
            }

            return httpClient.execute(putRequest);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private CloseableHttpClient HttpClient() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        SSLContextBuilder builder = new SSLContextBuilder();
        builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());
        return HttpClients.custom().setSSLSocketFactory(sslsf).build();
    }

}
