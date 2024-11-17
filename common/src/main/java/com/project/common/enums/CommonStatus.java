package com.project.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 核心通用訊息
 */
@Getter
@AllArgsConstructor
public enum CommonStatus {

    /**
     * 成功
     */
    SUCCESS("success"),

    /**
     * 異常
     */
    ERROR("error"),

    /**
     * 警告
     */
    WARNING("warning");

    private String status;

}
