package com.project.user.controller;

import com.project.common.annotation.SkipTokenVerification;
import com.project.common.controller.BaseController;
import com.project.common.exception.BaseException;
import com.project.common.response.ApiRes;
import com.project.user.request.auth.LoginReq;
import com.project.user.request.auth.RegisterReq;
import com.project.user.response.auth.LoginRes;
import com.project.user.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@SkipTokenVerification
@Validated
@RestController
@RequestMapping("/api/user/auth")
public class AuthController extends BaseController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "註冊")
    public ApiRes<Object> register(@Valid @RequestBody RegisterReq req) {
        authService.register(req);

        return null;
    }

    @PostMapping("/login")
    @Operation(summary = "登入")
    public ApiRes<LoginRes> login(@Valid @RequestBody LoginReq req)
            throws BaseException, NumberFormatException, NoSuchAlgorithmException, InvalidKeySpecException {
        LoginRes loginRes = authService.login(req);

        return handleResponse(loginRes);
    }
}
