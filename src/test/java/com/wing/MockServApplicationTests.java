package com.wing;

import com.wing.core.domain.MockJsonResponse;
import com.wing.core.domain.MockResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MockServApplicationTests {

	@Test
	public void contextLoads() {
		MockResponse mockResponse=new MockJsonResponse();
		System.out.println(mockResponse.toString());
	}

}
