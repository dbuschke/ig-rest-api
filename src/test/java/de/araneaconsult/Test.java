package de.araneaconsult;

import java.io.IOException;

import de.araneaconsult.idm.oauth.ResponseException;
import de.araneaconsult.idm.oauth.Token;
import io.swagger.client.ApiException;
import io.swagger.client.api.PolicyBroleApi;
import io.swagger.client.auth.OAuth;
import io.swagger.client.model.Burole;

public class Test {

    public void run() throws ApiException, IOException, ResponseException {
        de.araneaconsult.idm.oauth.OAuth oAuth = new de.araneaconsult.idm.oauth.OAuth(TestEnv.UA_URL, TestEnv.UA_OSP_CLIENTID, TestEnv.UA_OSP_SECRET);
        Token token = oAuth.requestToken(TestEnv.UA_USER, TestEnv.UA_PASS);

        OAuth oAuthAuthentication = new OAuth();




        PolicyBroleApi broleApi = new PolicyBroleApi();
        broleApi.getApiClient().getAuthentications().put("OSP", oAuthAuthentication);
        broleApi.getApiClient().setAccessToken(token.getAccessToken());

        Burole burole = new Burole();
        burole.setDescription("My first REST BRole");
        burole.setName("Gonna see what happens");
        broleApi.createBusinessRole(burole);
    }


    public static void main(String[] args) {
        try {
            Test t = new Test();
            t.run();
        } catch (ApiException ae) {
            System.err.println(ae.getCode() + ": " + ae.getResponseBody());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
        }
    }
}
