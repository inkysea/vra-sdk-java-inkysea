package com.inkysea.vmware.vra.sdk;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by kthieler on 7/28/16.
 */
public class CatalogItemTest {

    Properties prop = new Properties();
    InputStream input = null;

    String vraURL = null;
    String userName = null;
    String password = null;
    String tenant = null;

    private static final Logger LOGGER = Logger.getLogger(RestClientTest.class.getName());


    public CatalogItemTest() {
        Handler consoleHandler = null;
        consoleHandler = new ConsoleHandler();
        LOGGER.addHandler(consoleHandler);
        consoleHandler.setLevel(Level.ALL);


        LOGGER.setLevel(Level.FINE);

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
    public void create() throws Exception {

    }

    @Test
    public void update() throws Exception {

    }

    @Test
    public void destroy() throws Exception {

    }

    @Test
    public void get() throws Exception {

        AuthParam auth = new AuthParam (this.vraURL,this.userName,this.password,this.tenant);
        CatalogItem cat = new CatalogItem(auth);

        assertNotNull("Token must not be null", cat.Get("CentOS_7"));

    }

    @Test
    public void getTemplate() throws Exception {

        AuthParam auth = new AuthParam (this.vraURL,this.userName,this.password,this.tenant);
        CatalogItem cat = new CatalogItem(auth);

        assertNotNull("Token must not be null", cat.GetTemplate("CentOS_7"));

    }

    @Test
    public void getCatalogID() throws Exception {

        AuthParam auth = new AuthParam (this.vraURL,this.userName,this.password,this.tenant);
        CatalogItem cat = new CatalogItem(auth);

        // Test on existing blueprint
        assertNotNull("String must not be null", cat.GetCatalogID("CentOS_7"));

        // Test on fake blueprint name
        assertNull("String must not be null", cat.GetCatalogID("NOPE"));


    }

    @Test
    public void Request() throws Exception {

        AuthParam auth = new AuthParam (this.vraURL,this.userName,this.password,this.tenant);
        CatalogItem cat = new CatalogItem(auth);

        String requestID = cat.Request("CentOS_7");

        // Test on existing blueprint
        assertNotNull("Failed to request catalog item", requestID);
        LOGGER.info("Returned Request ID : "+requestID);


        // Test on fake blueprint name
        assertNull("Failed request on non-existent catalog item", cat.Request("NOPE"));


    }


    @Test
    public void exists() throws Exception {

        AuthParam auth = new AuthParam (this.vraURL,this.userName,this.password,this.tenant);
        CatalogItem cat = new CatalogItem(auth);

        //  Test with a known to exist blueprint
        assertNotNull("Token must not be null", cat.Exists("CentOS_7"));

        //  Test with a blueprint that does not exist
        assertFalse("Token must not be null", cat.Exists("NOPE"));


    }


}