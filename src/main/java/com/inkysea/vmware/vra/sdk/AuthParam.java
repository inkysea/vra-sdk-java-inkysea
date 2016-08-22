package com.inkysea.vmware.vra.sdk;

/**
 * Created by kthieler on 7/28/16.
 */
public class AuthParam {
    private String serverUrl;
    private String userName;
    private String password;
    private String tenant;

    public AuthParam(String serverUrl, String userName, String password, String tenant){

        this.serverUrl = serverUrl;
        this.userName = userName;
        this.password = password;
        this.tenant = tenant;

    }

    public String getServerUrl() {
        return serverUrl;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getTenant() {
        return tenant;
    }
}
