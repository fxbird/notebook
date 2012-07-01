package bean;

public class DBConfig {
    private String place;
    private String userName;
    private String password;
    private String dirver;
    private String winUrl;
    private String linuxUrl;
    private String logPath;

    public String getDirver() {
        return dirver;
    }

    public void setDirver(String dirver) {
        this.dirver = dirver;
    }

    public String getLinuxUrl() {
        return linuxUrl;
    }

    public void setLinuxUrl(String linuxUrl) {
        this.linuxUrl = linuxUrl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getWinUrl() {
        return winUrl;
    }

    public void setWinUrl(String winUrl) {
        this.winUrl = winUrl;
    }

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }
}
