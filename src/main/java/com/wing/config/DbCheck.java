package com.wing.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by qingshan.wqs on 2017/5/31.
 */
@Component
@Slf4j
public class DbCheck {

    @Resource
    private JdbcTemplate editorJdbcTemplate;

    @Scheduled(fixedRate = 1000*60*10)
    public void reportCurrentTime() {
        editorJdbcTemplate.execute("select 1");
        log.info("定时检查数据库连接，保持数据库连接不断");
    }
}
