package com.inkysea.vmware.vra.sdk;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by kthieler on 6/30/16.
 */
public class Deployment extends Resources {

    private AuthParam params;
    private RestClient restclient;
    private String compositionServiceURL;
    private String catalogServiceURL;
    private static final Logger LOGGER = Logger.getLogger(RestClient.class.getName());

    private String deploymentRequestID = "";
    private String deploymentID = "";

    public Deployment(AuthParam params) {
        super(params);

        this.params = params;

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

    /*
    public Deployment(AuthParam params, String deploymentName) {

        this.params = params;

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
*/

    public boolean Create ( String catalogItemName ) {
        // Check if already assigned a deployment
        CatalogItem ci = new CatalogItem( this.params );

        if ( ! ci.Exists( catalogItemName ) ) {
            return false;
        }

        this.deploymentRequestID = ci.Request(catalogItemName);

        if ( this.deploymentRequestID == null ){
            return false;
        }else{
            Request req = new Request(this.params);

            boolean rcode = false;

            while ( rcode != true ){

                String s = req.Status(this.deploymentRequestID);
                if (s.equals("SUCCESSFUL")) {
                    LOGGER.info("Request completed successfully");
                    rcode = true;

                } else if (s.equals("FAILED")) {
                    LOGGER.info("Request Failed");
                    return false;

                } else if (s.equals("REJECTED")) {
                    LOGGER.info("Request Rejected");
                    return false;
                } else {
                    rcode = false;
                }

                try {
                    Thread.sleep(5 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            deploymentID = this.GetDeploymentID();

            return true;
        }

    }

    public String GetDeploymentID () {

        Resources resource = new Resources( this.params );

        this.deploymentID = resource.getParentResourceID( this.deploymentRequestID );

        return deploymentID;
    }

    public boolean Create ( CatalogItem catalogItem, JsonObject template ) {
        // Check if already assigned a deployment

        return false;
    }

    public boolean Update(JsonObject template) {
        return false;

    }

    public boolean Destroy() {

/*
        if ( this.deploymentID == null ){

            LOGGER.info("Deployment ID is null.  Please ensure Deployment object was Created");
            return false;
        }


        if( this.parentResourceID == null ) {
            System.out.println("Destroying Deployment");

            DeploymentResources();
            this.getParentResourceID();
        }

        String actionID = this.getDestroyAction();
        getBusinessGroup();
        getTenant();

        System.out.println("JSON Destroy "+ jsonString);


        JsonElement jsonDestroyElement = new JsonParser().parse(jsonString);
        JsonObject jsonDestroyObject = jsonDestroyElement.getAsJsonObject();

        JsonObject jsonResourceReb = jsonDestroyObject.getAsJsonObject("resourceRef");
        jsonResourceReb.addProperty("id", this.parentResourceID);

        JsonObject jsonResourceAction = jsonDestroyObject.getAsJsonObject("resourceActionRef");
        jsonResourceAction.addProperty("id", actionID);

        JsonObject jsonOrganizationAction = jsonDestroyObject.getAsJsonObject("organization");
        jsonOrganizationAction.addProperty("tenantRef", this.tenantId);
        jsonOrganizationAction.addProperty("tenantLabel", this.tenantId);
        jsonOrganizationAction.addProperty("subtenantRef", this.businessGroupId);





        System.out.println("JSON Destroy "+jsonDestroyObject.toString());

        request.PostRequest(jsonDestroyObject.toString());
*/
        return false;

    }

    public boolean Exists() {

        return false;
    }

    public List ListActions() {

        return Collections.emptyList();
    }

    public boolean Action () {

        return false;
    }

    public boolean Status () {

        return false;
    }

    public String getDeploymentRequestID ( ){
        return this.deploymentRequestID;
    }
}
