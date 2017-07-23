package com.wing.core.filter;

import com.wing.core.domain.MockRequest;
import com.wing.core.domain.MockResponse;

/**
 * Created by Administrator on 2017/7/23.
 */
public class UrlFilter implements IFilter {

    @Override
    public boolean doFilter(MockRequest mockRequest, MockResponse mockResponse) {
        if(mockRequest.getHost().equalsIgnoreCase("localhost")&&mockRequest.getPort().equals("8080")) {
            mockResponse.body("{\"name\":\"wqs\",\"age\":12}");
            return true;
        }
        return false;
    }
}
