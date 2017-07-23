package com.wing.core.filter;

import com.wing.core.domain.MockRequest;
import com.wing.core.domain.MockResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/23.
 */
public class FilterChain implements IFilter {

    public  static List<IFilter> filters = new ArrayList<IFilter>();

    public FilterChain addFilter(IFilter f) {
        filters.add(f);
        return this;
    }


    public FilterChain removeFilter(Class clz) {
        for (IFilter filter : filters) {
            if (filter.getClass() == clz) {
                filters.remove(filter);
            }
        }
        return this;
    }


    @Override
    public boolean doFilter(MockRequest mockRequest, MockResponse mockResponse) {
        for (IFilter filter : filters) {
            if (filter.doFilter(mockRequest, mockResponse)) {
                return true;
            }
        }
        return false;
    }
}
