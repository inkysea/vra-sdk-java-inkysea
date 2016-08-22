package com.inkysea.vmware.vra.sdk;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by kthieler on 6/30/16.
 */
public class Blueprint extends CatalogItem {
    public Blueprint(AuthParam params ) {
        super(params);
    }

    public void create() {
    }

    public void exists() {
    }

    public void update() {
    }

    public void SetStatus() {
    }

    public void Destroy() {
    }

    public void Export() {
    }

    public void Import() {
    }

    public void Publish() {
    }

    public JsonObject GetBlueprint(String blueprintName) {
/*
        https://vra-app-1.inkysea.com/composition-service/api/blueprints/?$filter=(name eq 'CentOS_7')
        ///calls {{vRAURL}}/composition-service/api/blueprints/blueprintName
        String BLUEPRINTS_REST = this.compositionServiceURL + "blueprints/%s";

        String url = String.format(BLUEPRINTS_REST, blueprintName ).replace(' ', '+');
        LOGGER.info("Retrieving Blueprint From URL : "+url);


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


        return jsonResponse;
        */
        return null;

    }

}
