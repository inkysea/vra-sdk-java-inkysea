package com.inkysea.vmware.vra.sdk;

import com.google.gson.*;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by kthieler on 6/30/16.
 */
public class CatalogItem {

    private AuthParam params;
    private RestClient restclient;
    private String compositionServiceURL;
    private String catalogServiceURL;
    private static final Logger LOGGER = Logger.getLogger(RestClient.class.getName());

    private JsonObject catalogItemJson = null;
    private JsonObject catalogItemTemplateJSON = null;


    public CatalogItem( AuthParam params ) {

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

    public CatalogItem( AuthParam params , String catalogItemName) {

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

        this.catalogItemJson = this.Get(catalogItemName);
        this.catalogItemTemplateJSON = this.Get(catalogItemName);


    }


    public void Create() {
    }

    public void Update() {
    }

    public void Destroy() {
    }

    public JsonObject Get( String name) {

        LOGGER.info("Getting Catalog Item: "+name);

        String FETCH_CATALOG_ITEM = this.catalogServiceURL + "consumer/entitledCatalogItemViews?$filter=name+eq+'%s'";

        JsonObject response = null;

        String url = String.format(FETCH_CATALOG_ITEM, name).replace(' ', '+');

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

        JsonArray contents = contentElement.getAsJsonArray();

        LOGGER.info("JSON Array : "+contents);

            if (contents.size() == 1) {
                response = contents.get(0).getAsJsonObject();

            } else {
                if (contents.size() > 1) {
                    try {
                        throw new IOException("More than one blueprint with name "  + name + " found");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (contents.size() < 1) {
                    try {
                        throw new IOException("Blueprint with name " + name + " not found");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        return jsonResponse;

    }

    public JsonObject GetTemplate(String name){

        JsonObject response = this.Get(name);

        JsonArray catalogItemContentArray = response.getAsJsonArray("content");

        String catalogId = catalogItemContentArray.get(0).getAsJsonObject()
                .get("catalogItemId").getAsString();

        JsonArray linkArray = catalogItemContentArray.get(0)
                .getAsJsonObject()
                .getAsJsonArray("links");

        LOGGER.info("JSON links Array : "+linkArray);


        String templateURL = linkArray.get(0).getAsJsonObject().get("href").getAsString();

        LOGGER.info("Found Catalog Item Template URL : "+templateURL);

        HttpResponse vRAResponse = restclient.Get(templateURL);

        String result = null;
        try {
            result = EntityUtils.toString(vRAResponse.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.info("HTTP Response : "+result);

        JsonParser jsonParser = new JsonParser();
        JsonObject jsonResponse = (JsonObject)jsonParser.parse(result);

        LOGGER.info("Returning JSON OBJECT : "+jsonResponse);

        return jsonResponse ;
    }

    public String GetCatalogID(String name){
        String catalogID = null;

        // Validate item exists

        if (!this.Exists(name)){

            LOGGER.info("Requested Catalog Item does not exist: "+name);

            return null;
        }

        JsonObject template = this.GetTemplate(name);

        catalogID = template.get("catalogItemId").getAsString();

        LOGGER.info("Returning Catalog ID "+catalogID);


        return catalogID;
    }

    public boolean Exists( String name) {

        LOGGER.info("Getting Catalog Item: "+name);

        String FETCH_CATALOG_ITEM = this.catalogServiceURL + "consumer/entitledCatalogItemViews?$filter=name+eq+'%s'";

        JsonObject response = null;

        String url = String.format(FETCH_CATALOG_ITEM, name).replace(' ', '+');

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
        JsonArray contents = contentElement.getAsJsonArray();

        LOGGER.info("JSON Array : "+contents);

        if (contents.size() == 0) {
            return false;
        }else {
            return true;
        }
    }

    public String Request(String name) {

        String requestID = null;
        String urlTemp = this.catalogServiceURL + "consumer/entitledCatalogItems/%s/requests";


        // Validate item exists

        if (!this.Exists(name)){

            LOGGER.info("Requesting Catalog Item does not exist: "+name);

            return null;
        }

        LOGGER.info("Requesting Catalog Item: "+name);


        String url = String.format(urlTemp, this.GetCatalogID(name)).replace(' ', '+');

        JsonObject template = this.GetTemplate(name);

        Gson gson = new Gson();

        LOGGER.info("Requesting Catalog Item does not exist: "+name);


        HttpResponse response = restclient.Post(url, template.toString());

        String result = null;
        try {
            result = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }


        JsonParser jsonParser = new JsonParser();
        JsonObject jsonResponse = (JsonObject)jsonParser.parse(result);

        LOGGER.info("Returning JSON OBJECT : "+jsonResponse);


        requestID = jsonResponse.get("id").getAsString();

        return requestID;

    }

}
