package utilities;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public enum ConfigReader {
    DB_CONFIG_FILE("db_config.json"),
    CONFIG_FILE("config.json");

    private String configFile;

    ConfigReader(String configFile) {
        this.configFile = configFile;
    }

    public JSONObject getConfigFileAsJsonObject() throws IOException, ParseException {
        InputStream inputStream = ConfigReader.class.getClassLoader().getResourceAsStream(configFile); // Get resource
        Object obj = new JSONParser().parse(new InputStreamReader(inputStream)); // Read file

        inputStream.close();
        return (JSONObject) obj;
    }

    public String getConfigFileKey(String key) throws IOException, ParseException {
        JSONObject jo = getConfigFileAsJsonObject();
        return jo.get(key).toString();
    }
}
