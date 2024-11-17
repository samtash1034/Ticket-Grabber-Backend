package com.project.common.enums;

import com.project.common.exception.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 核心通用訊息
 */
@Getter
@AllArgsConstructor
public enum CommonCode {

    // === 系統共用訊息(20xx開頭) =====================================

    SUCCESS(20000, ""),

    // === 共用錯誤訊息(-40xx開頭) ====================================

    /**
     * 缺少必填參數[%s] <br/>
     * 請改使用 {@link RequiredParameterMissingException}
     */
    N40001(-40001, "缺少必填參數[%s]"),
    /**
     * 請求參數格式錯誤[%s] <br/>
     * 請改使用 {@link RequestDataFormatException}
     */
    N40002(-40002, "格式錯誤[%s]"),
    /**
     * 無此資料[%s] <br/>
     * 請改使用 {@link DataNotFindException}
     */
    N40003(-40003, "無此資料[%s]"),
    /**
     * 資料已存在[%s] <br/>
     * 請改使用 {@link DataExistsException}
     */
    N40004(-40004, "資料已存在[%s]"),
    /**
     * 資料異常[%s] <br/>
     * 請改使用 {@link AbnormalDataException}
     */
    N40005(-40005, "資料異常[%s]"),

    N40006(-40006, "帳號已存在"),

    N40007(-40007, "[%s]"),
    N40008(-40008, "訂單處理中"),
    N40009(-40009, "搶票失敗"),

    // 回傳 status code => 401
    N40101(-40101, "token 驗證失敗"),
    N40102(-40102, "token 過期"),
    N40103(-40103, "帳號不存在"),
    N40104(-40104, "密碼錯誤"),
    // 回傳 status code => 403
    N40301(-40301, "沒有權限"),


    /**
     * 用於未定義錯誤
     */
    ERROR(-99999, "%s");

    private final int code;
    private String msg;

    public String makeMessage(Object[] args) {
        String formattedMsg = this.getMsg();
        if (formattedMsg != null && args != null) {
            formattedMsg = String.format(formattedMsg, args);
        } else if (formattedMsg != null && formattedMsg.contains("%s")) {
            formattedMsg = formattedMsg.replace("%s", "");
        }
        return formattedMsg;
    }

}
