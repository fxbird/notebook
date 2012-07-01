package util;

import bean.DBConfig;
import com.xdg.util.MiscUtil;
import com.xdg.util.PropUtil;
import com.xdg.util.XmlUtil;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import other.Constant;

import java.util.HashMap;
import java.util.Map;

public class ConfigParser {
    private final static Log log = LogFactory.getLog(ConfigParser.class);
    private final String CONFIG_XML_PATH = "db.xml";

    private int backupIntvl;
    private String place;
    //    private List<DBConfig> dbConfigs=new ArrayList<DBConfig>();
    private Map<String, DBConfig> dbConfigs = new HashMap<String, DBConfig>();
    private static ConfigParser configParser;
    private boolean loaded;

    private ConfigParser(){}

    public void loadConfig() {
        place = PropUtil.getInstance(Constant.CONFIG_PROP_PATH).getString("place");

        SAXReader saxReader = new SAXReader();
        Document doc = null;
        try {
            doc = saxReader.read(this.getClass().getResourceAsStream("/" + CONFIG_XML_PATH)).getDocument();
        } catch (DocumentException e) {
            log.error(e);
            return;
        }

        backupIntvl = Integer.parseInt(XmlUtil.getElementValue(doc, "//backup-interval"));

        for (Element element : XmlUtil.selectElements(doc, "//config")) {
            DBConfig config = new DBConfig();
            config.setPlace(XmlUtil.getElementValue(element, "place"));
            config.setDirver(XmlUtil.getElementValue(element, "db/driver"));
            config.setUserName(XmlUtil.getElementValue(element, "db/username"));
            config.setPassword(XmlUtil.getElementValue(element, "db/password"));
            config.setWinUrl(XmlUtil.getElementValue(element, "db/win"));
            config.setLinuxUrl(XmlUtil.getElementValue(element, "db/linux"));

            dbConfigs.put(config.getPlace(), config);
        }

        loaded=true;

    }

    public boolean isLoaded() {
        return loaded;
    }

    public boolean isNotLoaded(){
        return !isLoaded();
    }

    public static ConfigParser getInstance(){
        if (configParser==null){
            return new ConfigParser();
        }else {
            return configParser;
        }
    }

    public DBConfig getCurrentConfig(){
        return dbConfigs.get(place);
    }

    public String getDriver(){
       return getCurrentConfig().getDirver();
    }

    public String getUserName(){
        return getCurrentConfig().getUserName();
    }

    public String getPassword(){
        return getCurrentConfig().getPassword();
    }

    public String getRealUrl(){
        if (MiscUtil.isWinOS()){
            return getWinUrl();
        }else {
            return getLinuxUrl();
        }
    }

    public String getWinUrl(){
        return getCurrentConfig().getWinUrl();
    }

    public String getLinuxUrl(){
        return getCurrentConfig().getLinuxUrl();
    }

    public int getBackupIntvl() {
        return backupIntvl;
    }

    public String getCurrentPlace() {
        return place;
    }


}
