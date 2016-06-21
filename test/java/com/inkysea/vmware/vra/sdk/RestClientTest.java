package com.inkysea.vmware.vra.sdk;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by kthieler on 6/21/16.
 */
public class RestClientTest {


    @Test
    public void dummy() throws Exception {
        RestClient test = new RestClient ();
        test.dummy();
        assertEquals("Dummy must return 1", 1, test.dummy());
    }

    @Test
    public void test_auth(){
        RestClient test = new RestClient ();
        assertNotNull("Token must not be null", test.AuthToken());
    }

    @Test
    public void test_get(){
        RestClient test = new RestClient ();
        test.AuthToken();
        test.Get();
        assertNotNull("Token must not be null", test.Get());
    }

    @Test
    public void test_post(){
        RestClient test = new RestClient ();
        test.AuthToken();
        test.Post();
        assertNotNull("Token must not be null", test.Post());
    }

    @Test
    public void test_put(){
        RestClient test = new RestClient ();
        test.AuthToken();
        test.Put();
        assertNotNull("Token must not be null", test.Put());
        
    }


}