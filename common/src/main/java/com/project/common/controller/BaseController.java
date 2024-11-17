package com.project.common.controller;

import com.project.common.response.ApiRes;
import lombok.Data;

import java.util.HashMap;

@Data
public class BaseController {


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
