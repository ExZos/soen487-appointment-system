package utilities;

public class UserAgentUtils {
    public static final String[] webUserAgentNames = {
            "Chrome",
            "Mozilla",
            "Safari",
            "AppleWebKit"
    };

    public static boolean isWebUserAgent(String userAgent) {
        for(String a: webUserAgentNames) {
            if(userAgent.contains(a))
                return true;
        }

        return false;
    }
}
