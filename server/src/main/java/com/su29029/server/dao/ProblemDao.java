package com.su29029.server.dao;

import com.su29029.server.bean.*;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import io.lettuce.core.dynamic.annotation.Param;

@Mapper
@Repository
public interface ProblemDao {

    @Select("select * from problems where id = #{id}")
    @Results({
        @Result(column = "id", property = "id"),
        @Result(column = "problemContent", property = "problemContent"),
        @Result(column = "problemScore", property = "problemScore"),
        @Result(column = "problemType", property = "problemType"),
        @Result(column = "problemAnswer", property = "problemAnswer"),
        @Result(column = "options", property = "options")
    })
    SelectProblem getSelectProblemById(@Param("id") int id);

    @Select("select * from problems where id = #{id}")
    @Results({
        @Result(column = "id", property = "id"),
        @Result(column = "problemContent", property = "problemContent"),
        @Result(column = "problemScore", property = "problemScore"),
        @Result(column = "problemAnswer", property = "problemAnswer"),
        @Result(column = "problemType", property = "problemType"),
    })
    OtherProblem getOtherProblemById(@Param("id") int id);

    @Select("select problemType from problems where id = #{id}")
    String checkProblemType(@Param("id") int id);

}
