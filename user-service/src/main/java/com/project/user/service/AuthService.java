package com.project.user.service;

import com.project.user.request.auth.LoginReq;
import com.project.user.request.auth.RegisterReq;
import com.project.user.response.auth.LoginRes;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface AuthService {

    void register(RegisterReq req);

    LoginRes login(LoginReq req) throws NoSuchAlgorithmException, InvalidKeySpecException;
}
