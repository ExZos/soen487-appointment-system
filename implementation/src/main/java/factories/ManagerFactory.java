package factories;

import repository.interfaces.IManager;
import utilities.ConfigReader;

import java.lang.reflect.Constructor;

public enum ManagerFactory {
    TEMP("TempManagerImpl");

    private IManager manager;

    ManagerFactory(String configKey) {
        try {
            Class<?> cl = Class.forName(ConfigReader.getConfigFileKey(ConfigReader.CONFIG_FILE, configKey));
            Constructor<?> cons = cl.getConstructor();
            manager = (IManager) cons.newInstance();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public IManager getManager(){
        return manager;
    }
}
