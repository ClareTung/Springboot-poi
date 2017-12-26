package cn.iponkan.mapper;

import cn.iponkan.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper{
    @Select("select * from user")
    public List<User> getAll();

}
