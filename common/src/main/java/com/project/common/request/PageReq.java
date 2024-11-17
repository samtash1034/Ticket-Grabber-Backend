package com.project.common.request;

import lombok.Data;

@Data
public class PageReq {

    private Integer offset = 0;

    private Integer limit = 10;
}
