package com.project.user.controller;


import com.project.common.annotation.SkipTokenVerification;
import com.project.common.controller.BaseController;
import com.project.common.response.ApiRes;
import com.project.user.client.ArticleServiceClient;
import com.project.user.response.user.UserDetailRes;
import com.project.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SkipTokenVerification
@RestController
@RequestMapping("/api/user")
public class UserController extends BaseController {

    @Autowired
    private ArticleServiceClient articleServiceClient;
    @Autowired
    private UserService userService;

    @GetMapping("/{userId}/articles")
    public String getUserArticles(@PathVariable String userId) {
        System.out.println("開始打 article service 囉！！");
        return articleServiceClient.getArticles("technology");
    }

    @GetMapping("/{userId}")
    @Operation(summary = "取得使用者詳細資料")
    public ApiRes<UserDetailRes> getUserDetail(@PathVariable String userId) {
        UserDetailRes res = userService.getUserDetail(userId);

        return handleResponse(res);
    }
}
