package com.wing.core.domain;

import org.apache.commons.lang.StringUtils;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2017/7/23.
 */
public class MockResponse {

    static final SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);

    Map<String,String> headers=new HashMap<>();
    String code="HTTP/1.1 200";
    String body;

    public MockResponse(){
        headers.put("Date",sdf.format(new Date()));
    }

    public MockResponse body(String body){
        this.body=body;
        return this;
    }

    public MockResponse header(String key,String value){
        this.headers.put(key,value);
        return this;
    }

    public MockResponse code(String code){
        this.code=code;
        return this;
    }

    @Override
    public String toString(){
        StringBuilder sb=new StringBuilder();
        sb.append(this.code+"\n");
        Iterator iterator=this.headers.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry entry=(Map.Entry) iterator.next();
            sb.append(entry.getKey()+": "+entry.getValue()+"\n");
        }
        sb.append("\n");
        if(StringUtils.isNotBlank(body))
            sb.append(this.body);
        return sb.toString();
    }

    public  byte[] bytes(){
        return toString().getBytes(Charset.forName("utf-8"));
    }

}
