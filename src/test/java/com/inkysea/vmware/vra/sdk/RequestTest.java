package com.inkysea.vmware.vra.sdk;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.*;

/**
 * Created by kthieler on 7/28/16.
 */
public class RequestTest {

    Properties prop = new Properties();
    InputStream input = null;

    String vraURL = null;
    String userName = null;
    String password = null;
    String tenant = null;

    private static final Logger LOGGER = Logger.getLogger(RestClientTest.class.getName());



    public RequestTest() {
        //Handler consoleHandler = null;
        //consoleHandler = new ConsoleHandler();
        //LOGGER.addHandler(consoleHandler);
        //consoleHandler.setLevel(Level.ALL);


        LOGGER.setLevel(Level.ALL);


        try {

            Path currentRelativePath = Paths.get("");
            String s = currentRelativePath.toAbsolutePath().toString();

            String filename = s+"/src/test/java/test.properties";


            File file = new File(filename);
            FileInputStream fileInput = new FileInputStream(file);
            Properties prop = new Properties();
            prop.load(fileInput);
            fileInput.close();

            this.vraURL = prop.getProperty("vRAURL");
            this.userName = prop.getProperty("userName");
            this.password = prop.getProperty("password");
            this.tenant = prop.getProperty("tenant");

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Test
    public void getBlueprintTemplate() throws Exception {
        assertNotNull("To be done", null);

    }

    @Test
    public void provisionBluePrint() throws Exception {
        assertNotNull("To be done", null);
    }

    @Test
    public void status() throws Exception {

        AuthParam auth = new AuthParam (this.vraURL,this.userName,this.password,this.tenant);
        CatalogItem cat = new CatalogItem(auth);

        String requestID = cat.Request("CentOS_7");

        // Test on existing blueprint
        assertNotNull("Failed to request catalog item", requestID);
        LOGGER.info("Returned Request ID : "+requestID);


        Request req = new Request(auth);
        assertNotNull("Received NULL for status", req.Status(requestID));

    }

    @Test
    public void getResourceView() throws Exception {
        assertNotNull("To be done", null);
    }

    @Test
    public void resources() throws Exception {
        assertNotNull("To be done", null);
    }

}