package personal.popy.application.lifecycle;

public interface WebServerContainer {
    String getScope();
    void setProperty(String key, String value);
    String getProperty(String key);
    void addChild(WebServerContainer container);


}
