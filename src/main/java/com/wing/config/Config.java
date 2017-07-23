package com.wing.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created by qingshan.wqs on 2016/12/29.
 */
@Configuration
@Data
public class Config {

    @Value("${proxy.env}")
    private String env;


    @Value("${proxy.connect_retries}")
    public static int connectRetries = 5; // 尝试与目标主机连接次数

    @Value("${proxy.connect_pause}")
    public static int connectPause = 10; // 每次建立连接的间隔时间


    @Value("${proxy.listenPause}")
    public static int listen_pause = 50; // 监听连接间隔时间

    @Value("${proxy.timeout}")
    public static int timeout = 10000; // 每次尝试连接的最大时间


    @Value("${proxy.httpsPort}")
    public static int httpsPort = 9112; // https 代理端口


    @Value("${proxy.httpPort}")
    public static int httpPort = 9111; // http 端口

    @Value("${proxy.httpsPoolSize}")
    public static int httpsPoolSize = 10; // https 连接池

    @Value("${proxy.httpPoolSize}")
    public static int httpPoolSize = 10; // http 连接池

}
