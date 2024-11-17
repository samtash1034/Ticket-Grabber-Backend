package com.project.user.service;

import com.project.common.enums.CommonCode;
import com.project.common.exception.BaseException;
import com.project.common.service.BaseService;
import com.project.common.util.TimestampUtil;
import com.project.common.util.TokenUtil;
import com.project.common.util.UUIDUtil;
import com.project.orm.mapper.UserMapper;
import com.project.orm.model.UserModel;
import com.project.user.request.auth.LoginReq;
import com.project.user.request.auth.RegisterReq;
import com.project.user.response.auth.LoginRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Timestamp;

@Service
public class AuthServiceImpl extends BaseService implements AuthService{

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private TokenUtil tokenUtil;

    @Override
    @Transactional
    public void register(RegisterReq req) {
        UserModel userModel = userMapper.selectByEmail(req.getEmail());
        if(userModel != null){
            throw new BaseException(CommonCode.N40006);
        }

        UserModel model = convertToUserModel(req);

        userMapper.insert(model);
    }

    @Override
    public LoginRes login(LoginReq req) throws NoSuchAlgorithmException, InvalidKeySpecException {
        UserModel userModel = userMapper.selectByEmail(req.getEmail());

        validateAccountExists(userModel);

        validatePassword(req.getPassword(), userModel.getPassword());

        return setLoginRes(userModel);
    }

    private UserModel convertToUserModel(RegisterReq req) {
        UserModel model = convertSingleObject(req, UserModel.class);
        String uuid = UUIDUtil.generateUuid();
        Timestamp currentTimestamp = TimestampUtil.getCurrentTimestamp();
        model.setUserId(uuid);
        model.setPassword(passwordEncoder.encode(req.getPassword()));
        model.setCreateUser(uuid);
        model.setCreateTime(currentTimestamp);
        model.setUpdateUser(uuid);
        model.setUpdateTime(currentTimestamp);

        return model;
    }

    private void validateAccountExists(UserModel model) throws BaseException {
        if (model == null) {
            throw new BaseException(CommonCode.N40103);
        }
    }

    private void validatePassword(String rawPassword, String dbPassword) throws BaseException {
        if (!passwordEncoder.matches(rawPassword, dbPassword)) {
            throw new BaseException(CommonCode.N40104);
        }
    }

    private LoginRes setLoginRes(UserModel userModel) throws BaseException, NoSuchAlgorithmException, InvalidKeySpecException {
        LoginRes loginRes = new LoginRes();

        String token = tokenUtil.createToken(userModel.getEmail(), userModel.getUserId());
        loginRes.setToken(token);
        loginRes.setUserName(userModel.getName());

        return loginRes;
    }

}
