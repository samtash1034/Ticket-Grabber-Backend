package com.project.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommonCode {

    // === 系統共用訊息(20xx開頭) =====================================

    SUCCESS(20000, ""),

    // === 共用錯誤訊息(-40xx開頭) ====================================
    N40007(-40007, "[%s]"),
    N40008(-40008, "訂單處理中"),
    N40009(-40009, "搶票失敗"),

    /**
     * 用於未定義錯誤
     */
    ERROR(-99999, "%s");

    private final int code;
    private String msg;


}
