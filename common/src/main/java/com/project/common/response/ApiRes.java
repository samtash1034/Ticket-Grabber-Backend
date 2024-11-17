package com.project.common.response;

import lombok.Data;

@Data
public class ApiRes<T> {

    private String status;

    private Integer code;

    private String message;

    private T result;

}
