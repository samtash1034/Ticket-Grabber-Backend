package com.project.common.controller;

import com.project.common.response.ApiPageRes;
import com.project.common.response.ApiRes;
import lombok.Data;

import java.util.HashMap;

@Data
public class BaseController {

    /**
     * 使用者 id
     */
    protected String userId;

    protected ApiRes handlePageResponse(Object data, int totalCount) {
        ApiPageRes pageResponse = new ApiPageRes();
        ApiRes ApiRes = new ApiRes();

        pageResponse.setData(data);
        pageResponse.setTotalCount(totalCount);

        ApiRes.setResult(pageResponse);

        return ApiRes;
    }

    /**
     * 處理資料回傳格式為 ApiRes
     *
     * @param data 要回傳的資料
     * @return
     */
    protected ApiRes handleResponse(Object data) {
        ApiRes ApiRes = new ApiRes();
        if (data != null) {
            ApiRes.setResult(data);
        } else {
            ApiRes.setResult(new HashMap<>());
        }

        return ApiRes;
    }

}
