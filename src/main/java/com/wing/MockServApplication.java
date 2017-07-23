package com.wing;

import com.wing.core.proxy.HttpProxyStart;
import com.wing.core.proxy.HttpsProxyStart;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MockServApplication {

	public static void main(String[] args) {
		SpringApplication.run(MockServApplication.class, args);
		new HttpProxyStart().run();
		new HttpsProxyStart().run();

	}
}
