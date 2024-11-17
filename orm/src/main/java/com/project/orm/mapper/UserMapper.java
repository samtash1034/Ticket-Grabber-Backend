package com.project.orm.mapper;

import com.project.orm.model.UserModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    UserModel selectByPrimaryKey(String id);

    UserModel selectByEmail(String email);

    void insert(UserModel model);
}
