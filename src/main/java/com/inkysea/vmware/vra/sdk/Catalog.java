package com.inkysea.vmware.vra.sdk;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by kthieler on 7/30/16.
 */
public class Catalog {

    private AuthParam params;
    private RestClient restclient;
    private String compositionServiceURL;
    private String catalogServiceURL;
    private static final Logger LOGGER = Logger.getLogger(RestClient.class.getName());

    private List<ArrayList<String>> catalogList = new ArrayList<ArrayList<String>>();


    public  Catalog ( AuthParam params ){

        try {
            this.restclient  = new RestClient(
                    params.getServerUrl(),
                    params.getUserName(),
                    params.getPassword(),
                    params.getTenant()
            );

            catalogServiceURL = params.getServerUrl()  + "/catalog-service/api/";


        }catch ( IOException e) {
            e.printStackTrace();
        }

    }

    public List ListItems(){
        // Returns a list of Catalog Items
        //       "catalogItemId": "eb97686a-4ae6-4425-8fff-27d6373cac44", "name": "CentOS_7",
        //       "description": "CentOS 7 image leased between 1 and 90 days.",



        LOGGER.info("Getting List of Catalog Items");

        String url = this.catalogServiceURL + "consumer/entitledCatalogItemViews";


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


        JsonElement contentElement = jsonResponse.get("content");

        LOGGER.info("JSON Element : "+contentElement);

        JsonArray contentArray = contentElement.getAsJsonArray();

        LOGGER.info("JSON Array : "+contentArray);

        for (JsonElement content : contentArray) {

            ArrayList<String> catalogListData = new ArrayList<String>();
            //    private List<List<String>> catalogList = new ArrayList<List<String>>();


            catalogListData.add(content.getAsJsonObject().get("catalogItemId").getAsString());
            catalogListData.add(content.getAsJsonObject().get("name").getAsString());
            catalogListData.add(content.getAsJsonObject().get("description").getAsString());

            LOGGER.info("Adding to list : "+catalogListData.toString());

            this.catalogList.add(catalogListData);

        }

        LOGGER.info("Returnling list : "+this.catalogList);


        return this.catalogList;

    }


}
