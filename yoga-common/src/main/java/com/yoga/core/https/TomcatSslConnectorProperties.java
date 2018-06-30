package com.yoga.core.https;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "https.server")
public class TomcatSslConnectorProperties {
    private Integer port;
    private Boolean ssl = true;
    private Boolean secure = true;
    private String scheme = "https";
    private String keystore;
    private String keystorePassword;

    public Integer getPort() {
        return port;
    }
    public void setPort(Integer port) {
        this.port = port;
    }
    public Boolean getSsl() {
        return ssl;
    }
    public void setSsl(Boolean ssl) {
        this.ssl = ssl;
    }
    public Boolean getSecure() {
        return secure;
    }
    public void setSecure(Boolean secure) {
        this.secure = secure;
    }
    public String getScheme() {
        return scheme;
    }
    public void setScheme(String scheme) {
        this.scheme = scheme;
    }
    public String getKeystore() {
        return keystore;
    }
    public void setKeystore(String keystore) {
        this.keystore = keystore;
    }
    public String getKeystorePassword() {
        return keystorePassword;
    }
    public void setKeystorePassword(String keystorePassword) {
        this.keystorePassword = keystorePassword;
    }

    public void configureConnector(Connector connector) {
        if (port != null) {
            connector.setPort(port);
        }
        if (secure != null) {
            connector.setSecure(secure);
        }
        if (scheme != null) {
            connector.setScheme(scheme);
        }
        if (ssl != null) {
            connector.setProperty("SSLEnabled", ssl.toString());
        }
        if (keystore != null) {
            connector.setProperty("keystoreFile", keystore);
            connector.setProperty("keystorePassword", keystorePassword);
            connector.setProperty("keystorePass", keystorePassword);
        }
    }
}
