package com.project.user.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "article-service")
public interface ArticleServiceClient {

    @GetMapping("/articles")  // 對應 article-service 中的 /articles 路徑
    String getArticles(@RequestParam("category") String category);
}
