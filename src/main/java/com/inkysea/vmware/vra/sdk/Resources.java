package com.inkysea.vmware.vra.sdk;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.applet.AudioClip;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by kthieler on 6/30/16.
 */
public class Resources {

    private AuthParam params;
    private RestClient restclient;
    private String catalogServiceURL;
    private static final Logger LOGGER = Logger.getLogger(RestClient.class.getName());

    private List<ArrayList<String>> catalogList = new ArrayList<ArrayList<String>>();


    public  Resources(AuthParam params) {

        try {
            this.restclient  = new RestClient(
                    params.getServerUrl(),
                    params.getUserName(),
                    params.getPassword(),
                    params.getTenant()
            );

            this.catalogServiceURL = params.getServerUrl()  + "/catalog-service/api/";


        }catch ( IOException e) {
            e.printStackTrace();
        }
    }

    public void List() {

    }

    public JsonObject GetRequestResourceView(String requestID)  {
        //  get /consumer/requests/{ID}/resourceViews   search for...

        String tempURL = catalogServiceURL + "consumer/requests/%s/resourceViews";


        String url = String.format(tempURL, requestID).replace(' ', '+');

        LOGGER.info("Using URL :" + url);

        HttpResponse vRAResponse = restclient.Get(url);

        String result = null;
        try {
            result = EntityUtils.toString(vRAResponse.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.info("HTTP Response : "+result);

        JsonParser jsonParser = new JsonParser();
        JsonObject jsonResponse = (JsonObject)jsonParser.parse(result);

        LOGGER.info("JSON OBJECT : "+jsonResponse);

        return jsonResponse;

    }

    public String getParentResourceID( String requestID ){

        LOGGER.info("Parent Resource ID");

        String parentResourceID = "";

        JsonObject resources = GetRequestResourceView( requestID );

        JsonArray contentArray = resources.getAsJsonArray("content");
        for (JsonElement content : contentArray) {

            if (content.getAsJsonObject().get("resourceType").getAsString().equals("composition.resource.type.deployment")) {

                 parentResourceID = content.getAsJsonObject().get("resourceId").getAsString();
            }
        }

        return parentResourceID;
    }

    public void getResourceName( String parentResourceID ){

        // catalog-service/api/consumer/resourceViews/125aad49-42a2-4000-b52c-6e1f8d814979

    }

    }
