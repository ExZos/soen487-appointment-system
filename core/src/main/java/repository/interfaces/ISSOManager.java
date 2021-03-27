package repository.interfaces;

import com.github.scribejava.core.model.OAuth2AccessToken;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface ISSOManager extends IManager {
    String getAuthorizationUrl();
    OAuth2AccessToken getAccessToken(String code) throws IOException, InterruptedException, ExecutionException;
    String getLoginEmail(OAuth2AccessToken accessToken) throws IOException;
}
