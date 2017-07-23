package com.wing;

import com.wing.core.filter.FilterChain;
import com.wing.core.filter.UrlFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


/**
 * 参考  http://www.java2s.com/Tutorial/Java/0490__Security/HTTPSServer.htm
 */
@Slf4j
@Component
public class HttpProxyFilter implements CommandLineRunner{

	@Override
	public void run(String... strings) throws Exception {
		log.info("开始加载url 过滤器");
		FilterChain.filters.add(new UrlFilter());
	}
}
