package it.valeriovaudi.familybudget.accountservice.web.security;

import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

public class GlobalFrontChannelLogoutProvider {

    private final String postLogoutRedirectUri;
    private final String oidConnectDiscoveryEndPoint;

    public GlobalFrontChannelLogoutProvider(String postLogoutRedirectUri, String oidConnectDiscoveryEndPoint) {
        this.postLogoutRedirectUri = postLogoutRedirectUri;
        this.oidConnectDiscoveryEndPoint = oidConnectDiscoveryEndPoint;
    }

    public String getLogoutUrl() {
        var restTemplate = new RestTemplate();
        HashMap<String, String> forObject =
                restTemplate.getForObject(oidConnectDiscoveryEndPoint, HashMap.class);

        String logoutUrl = forObject.get("end_session_endpoint");

        return logoutUrl + "?post_logout_redirect_uri=" + postLogoutRedirectUri;
    }
}
