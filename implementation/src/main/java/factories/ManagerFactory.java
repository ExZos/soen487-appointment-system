package factories;

import repository.interfaces.IManager;
import utilities.ConfigReader;

import java.lang.reflect.Constructor;

public enum ManagerFactory {
    GoogleSSOManager("GoogleSSOManagerImpl"),
    AdminManager("AdminManagerImpl"),
    ResourceManager("ResourceManagerImpl"),
    UserManager("UserManagerImpl"),
    AppointmentManager("AppointmentManagerImpl"),
    CalendarManager("CalendarManagerImpl");

    private IManager manager;

    ManagerFactory(String configKey) {
        try {
            Class<?> cl = Class.forName(ConfigReader.CONFIG_FILE.getConfigFileKey(configKey));
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
