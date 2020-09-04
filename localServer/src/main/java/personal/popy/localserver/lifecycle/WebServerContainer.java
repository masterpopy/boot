package personal.popy.localserver.lifecycle;

public interface WebServerContainer {
    String getScope();
    void setProperty(String key, String value);
    String getProperty(String key);

}
