package com.wing.core.domain;

import com.wing.MockServApplication;
import org.springframework.boot.SpringApplication;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/23.
 */
public class MockJsonResponse extends MockResponse{


    public MockJsonResponse(){
        super();
        headers.put("Content-Type","application/json;charset=UTF-8");
        headers.put("Transfer-Encoding","chunked");
    }


}
