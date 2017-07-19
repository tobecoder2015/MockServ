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

    @Value("${env}")
    private String env;


}
