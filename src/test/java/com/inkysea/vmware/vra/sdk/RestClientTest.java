package com.inkysea.vmware.vra.sdk;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
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

import static org.junit.Assert.*;

/**
 * Created by kthieler on 6/21/16.
 */
public class RestClientTest {

    Properties prop = new Properties();
    InputStream input = null;

    String vraURL = null;
    String userName = null;
    String password = null;
    String tenant = null;

    private static final Logger LOGGER = Logger.getLogger(RestClientTest.class.getName());



    public RestClientTest() {
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
    public void dummy() throws Exception {
        RestClient test = new RestClient (this.vraURL,this.userName,this.password,this.tenant );
        test.dummy();
        assertEquals("Dummy must return 1", 1, test.dummy());
    }

    @Test
    public void test_auth() throws IOException {

        RestClient test = new RestClient (this.vraURL,this.userName,this.password,this.tenant);
        assertNotNull("Token must not be null", test.AuthToken());
    }

    @Test
    public void test_get() throws IOException {
        RestClient test = new RestClient (this.vraURL,this.userName,this.password,this.tenant);
        test.AuthToken();

        // Test on known good URL
        String url = this.vraURL+"/catalog-service/api/consumer/resources/";
        assertNotNull("Token must not be null", test.Get(url));

    }


    @Test
    public void test_get_exceptions() throws IOException{

        LOGGER.info("Testing exceptions ");

        RestClient test = new RestClient (this.vraURL,this.userName,this.password,this.tenant);
        test.AuthToken();

        // Test on known good URL
        LOGGER.info("Testing known good URL ");
        String url = this.vraURL+"/catalog-service/api/consumer/resources/";
        assertNotNull("Token must not be null", test.Get(url));

        // Test for exception on bad URL
        LOGGER.info("Testing bad URL for ClientProtocolException");
        url = "Bad_URL";
        assertNull("Bad URL test should be null", test.Get(url));
    }


    @Test
    public void test_post() throws IOException{
        RestClient test = new RestClient (this.vraURL,this.userName,this.password,this.tenant);
        assertNotNull("Token must not be null", test.AuthToken());
    }


    @Test
    public void test_post_multipart() throws IOException{
        RestClient test = new RestClient (this.vraURL,this.userName,this.password,this.tenant);
        assertNotNull("Token must not be null", test.AuthToken());
        assertNotNull("Token must not be null", test.Post(this.vraURL , this.vraURL) );

        assertNotNull("Placeholder", null);

    }

    @Test
    public void test_post_multipart_exceptions() throws IOException{
        String url = null;
        String payload = null;

        RestClient test = new RestClient (this.vraURL,this.userName,this.password,this.tenant);
        assertNotNull("Token must not be null", test.AuthToken());


        // Test for exception on bad URL
        LOGGER.info("Testing bad URL for ClientProtocolException");
        url = "Bad_URL";
        payload = "payload";
        assertNull("Bad URL test should be null", test.Put(url,payload));


        assertNotNull("Placeholder", null);

    }


    @Test
    public void test_put() throws IOException{
        RestClient test = new RestClient (this.vraURL,this.userName,this.password,this.tenant);
        test.AuthToken();

        assertNotNull("Token must not be null", null);

    }


    @Test
    public void test_put_exceptions() throws IOException{
        RestClient test = new RestClient (this.vraURL,this.userName,this.password,this.tenant);

        // Test for exception on non authenticated
        LOGGER.info("Validating that non authorized returns");
        String url = this.vraURL;
        String payload = "payload";
        assertNotNull("Not authorized should not be null", test.Put(url,payload));

        test.AuthToken();

        // Test for exception on bad URL
        LOGGER.info("Testing bad URL for ClientProtocolException");
        url = "Bad_URL";
        payload = "payload";
        assertNull("Bad URL test should be null", test.Put(url,payload));



    }

}