package com.su29029.server.dao;

import com.su29029.server.bean.*;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserDao {

    @Select("select * from users where username = #{username}")
    @Results({
            @Result(column = "username", property = "username"),
            @Result(column = "score", property = "score")
    })
    User findUserByUsername(@Param("username") String username);

    @Select("select * from users")
    @Results({
            @Result(column = "username", property = "username"),
            @Result(column = "score", property = "score")
    })
    List<User> findAllUser();

    @Select("select * from users where username = #{username} and password = #{password}")
    @Results({
            @Result(column = "username", property = "username"),
            @Result(column = "password", property = "password"),
            @Result(column = "score", property = "score")
    })
    User findUserByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    @Insert("insert into users (username,password,score) values (#{username}, #{password}, 0)")
    void insertUser(@Param("username") String username, @Param("password") String password);

    @Update("update users set password = #{password} where username = #{username}")
    void updateUser(@Param("password") String password, @Param("username") String username);

    @Delete("delete from users where username = #{username}")
    void deleteUser(@Param("username") String username);
}
