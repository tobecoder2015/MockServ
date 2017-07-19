package com.wing.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface InnerMapper {


    @Select("select * from meta_user_check_pct")
    List<Map> checkConfig();


    @Select("select * from ddb_poiresult where taskId=(select id from tdb_indotask where taskName=#{taskName}) and state!=2")
    List<Map> toDelConfirmPois(@Param("taskName") String taskName);

    @Select("select id from tdb_indotask where taskName=#{taskName}")
    Integer getTaskId(@Param("taskName") String taskName);


    @Select("select * from ddb_poiresult where taskId=(select id from tdb_indotask where taskName=#{taskName})")
    List<Map> getDoorlockData(@Param("taskName") String taskName);



}
