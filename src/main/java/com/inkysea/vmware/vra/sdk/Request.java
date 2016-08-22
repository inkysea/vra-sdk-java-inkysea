package com.inkysea.vmware.vra.sdk;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by kthieler on 6/30/16.
 */
public class Request {

    private AuthParam params;
    private RestClient restclient;
    private String catalogServiceURL;

    private static final Logger LOGGER = Logger.getLogger(RestClient.class.getName());



    public Request( AuthParam params ) {

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


    public String Status( String requestID) {

        LOGGER.info("Getting Catalog Item: " + requestID);
        String phase = null;

        String tempURL = this.catalogServiceURL + "consumer/requests/%s";;
        JsonObject response = null;


        String url = String.format(tempURL, requestID).replace(' ', '+');

        LOGGER.info("Getting Catalog Item: " + url);

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

        phase = jsonResponse.get("phase").getAsString();

        LOGGER.info("Request phase : "+phase);

        return phase;

    }


}
