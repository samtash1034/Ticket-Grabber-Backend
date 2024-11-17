package com.project.user.service.impl;


import com.project.common.service.BaseService;
import com.project.orm.mapper.UserMapper;
import com.project.orm.model.UserModel;
import com.project.user.response.user.UserDetailRes;
import com.project.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends BaseService implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetailRes getUserDetail(String userId) {
        UserModel model = userMapper.selectByPrimaryKey(userId);

        return convertSingleObject(model, UserDetailRes.class);
    }

}
