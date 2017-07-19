package com.wing.config;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by qingshan.wqs on 2016/12/29.
 */
@Configuration
public class WebConfig {


    // Set maxPostSize of embedded tomcat server to 20 megabytes (default is 2 MB)
    @Bean
    EmbeddedServletContainerCustomizer containerCustomizer() throws Exception {
        return (ConfigurableEmbeddedServletContainer container) -> {
            if (container instanceof TomcatEmbeddedServletContainerFactory) {
                TomcatEmbeddedServletContainerFactory tomcat = (TomcatEmbeddedServletContainerFactory) container;
                tomcat.addConnectorCustomizers(
                        (connector) -> {
                            connector.setMaxPostSize(50 * 1024 * 1024); // 50 MB
                        }
                );
            }
        };
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**");
            }
        };
    }


    @Value("${httpclient.connectTimeout}")
    private int connectTimeout;

    @Value("${httpclient.readTimeout}")
    private int readTimeout;

    @Value("${httpclient.maxConnection}")
    private int maxConnection;

    @Value("${httpclient.maxConnectionPerHost}")
    private int maxConnectionPerHost;

    @Bean
    public CloseableHttpAsyncClient asyncHttpClient() throws Exception {
        PoolingNHttpClientConnectionManager cm = new PoolingNHttpClientConnectionManager(new DefaultConnectingIOReactor(IOReactorConfig.DEFAULT));
        cm.setMaxTotal(maxConnection);
        cm.setDefaultMaxPerRoute(maxConnectionPerHost);
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(connectTimeout)
                .setSocketTimeout(readTimeout)
                .build();
        CloseableHttpAsyncClient build = HttpAsyncClients.custom()
                .setConnectionManager(cm)
                .setDefaultRequestConfig(config)
                .build();
        build.start();
        return build;
    }
}
