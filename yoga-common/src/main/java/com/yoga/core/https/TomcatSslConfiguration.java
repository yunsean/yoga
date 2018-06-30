package com.yoga.core.https;

import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.apache.catalina.connector.Connector;

@Configuration
@PropertySource("classpath:/application.properties")
@EnableConfigurationProperties(TomcatSslConnectorProperties.class)
public class TomcatSslConfiguration extends WebMvcConfigurerAdapter {

    @Bean
    public EmbeddedServletContainerFactory servletContainer(TomcatSslConnectorProperties properties) {
        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
        if (properties.getKeystore() != null) {
            tomcat.addAdditionalTomcatConnectors(createSslConnector(properties));
        }
        return tomcat;
    }

    private Connector createSslConnector(TomcatSslConnectorProperties properties) {
        Connector connector = new Connector();
        properties.configureConnector(connector);
        return connector;
    }
}
