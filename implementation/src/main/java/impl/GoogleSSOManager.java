package impl;

import com.github.scribejava.apis.GoogleApi20;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfoplus;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import repository.interfaces.ISSOManager;
import utilities.ConfigReader;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class GoogleSSOManager implements ISSOManager {
    private String appName;
    private String clientId;
    private String clientSecret;
    private String appScopes;
    private String redirectURI;

    private OAuth20Service service;

    public GoogleSSOManager() throws IOException, ParseException {
        JSONObject jo = ConfigReader.GOOGLE_CREDS_FILE.getConfigFileAsJsonObject();
        appName = (String) jo.get("app_name");
        clientId = (String) jo.get("client_id");
        clientSecret = (String) jo.get("client_secret");
        appScopes = (String) jo.get("app_scopes");
    }

    public String getAuthorizationUrl(boolean isWebOrigin) throws IOException, ParseException {
        service = buildOAuth20Service(isWebOrigin);
        return service.getAuthorizationUrl();
    }

    public OAuth2AccessToken getAccessToken(String code, boolean isWebOrigin) throws IOException, ParseException, InterruptedException, ExecutionException {
        service = buildOAuth20Service(isWebOrigin);
        return service.getAccessToken(code);
    }

    public String getLoginEmail(OAuth2AccessToken accessToken) throws IOException {
        // In a rush so I'll use this even if it's deprecated
        Credential credential = new GoogleCredential().setAccessToken(accessToken.getAccessToken());

        Oauth2 oauth2 = new Oauth2.Builder(new NetHttpTransport(), new JacksonFactory(), credential)
                .setApplicationName(appName)
                .build();

        Userinfoplus userinfo = oauth2.userinfo()
                .get()
                .execute();

        return userinfo.getEmail();
    }

    private OAuth20Service buildOAuth20Service(boolean isWebOrigin) throws IOException, ParseException{
        if(isWebOrigin)
            redirectURI = ConfigReader.GOOGLE_CREDS_FILE.getConfigFileKey("web_redirect_uris");
        else
            redirectURI = ConfigReader.GOOGLE_CREDS_FILE.getConfigFileKey("default_redirect_uris");

        return new ServiceBuilder(clientId)
                .apiSecret(clientSecret)
                .defaultScope(appScopes)
                .callback(redirectURI)
                .build(GoogleApi20.instance());

    }
}
