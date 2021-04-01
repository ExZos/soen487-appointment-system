package repository.interfaces;

import com.github.scribejava.core.model.OAuth2AccessToken;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface ISSOManager extends IManager {
    String getAuthorizationUrl(boolean isWebOrigin) throws IOException, ParseException;
    OAuth2AccessToken getAccessToken(String code, boolean isWebOrigin) throws IOException, ParseException, InterruptedException, ExecutionException;
    String getLoginEmail(OAuth2AccessToken accessToken) throws IOException;
}
