package com.wing.core.filter;

import com.wing.core.domain.MockRequest;
import com.wing.core.domain.MockResponse;

/**
 * Created by Administrator on 2017/7/23.
 *
 * 通过过滤器模式进行url处理
 */
public interface IFilter {
     boolean doFilter(MockRequest mockRequest, MockResponse mockResponse);
}
